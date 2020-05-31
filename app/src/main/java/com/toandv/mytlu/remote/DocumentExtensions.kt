package com.toandv.mytlu.remote

import android.content.res.Resources
import com.toandv.mytlu.R
import com.toandv.mytlu.local.entity.*
import com.toandv.mytlu.remote.domain.ClassTime
import com.toandv.mytlu.remote.domain.TimeTable
import com.toandv.mytlu.utils.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.util.regex.Matcher

/**
 * org.jsoup.nodes.Document Extensions functions for crawling data.
 * be created, written, maintained by @author
 * @author Vu Chi Cong, congvc62@wru.vn */

/**
 *  Parse data from document of this URL:
 *  http://dangky.tlu.edu.vn/cmcsoft.iu.web.info/StudentViewExamList.aspx
 *
 *  @return flow of [ExamTimetable] cold data stream
 */
@ExperimentalCoroutinesApi
fun Document.parseExamTableDataFlow(): Flow<ExamTimetable> {
    val formatter = DateTimeFormat.forPattern(INPUT_DATE_TIME_FORMAT)
    return takeIf { title() == ".: Lịch thi cá nhân sinh viên :." || baseUri() == URL_BASE + URL_EXAM_TIMETABLE }
        ?.run {
            val semester = with(select("""#drpSemester option[selected="selected"]""")) {
                val code = `val`()
                val text = text()
                Semester(text.substring(2), text[0].toString(), code)
            }
            val dot = select("#drpDotThi").text()
            select("#tblCourseList tr")
                ?.asFlow()
                ?.drop(1)
                ?.filter { it.childrenSize() == 10 }
                ?.map { element ->
                    val items = element.select("td")
                    val rawTime = items[5].text()
                    val matcher = """\d\d:\d\d""".toPattern().matcher(rawTime)
                    ExamTimetable(
                        semester,
                        dot,
                        items[1].text(),
                        items[2].text(),
                        items[3].text(),
                        formatter.parseLocalDateTime(
                            "%s %s".format(
                                items[4].text(),
                                matcher.nextOrNull() ?: "00:00"
                            )
                        ),
                        rawTime,
                        items[7].text(),
                        items[8].text()
                    )
                }
        }
        ?: wrongDocument(URL_EXAM_TIMETABLE, baseUri())
}

private fun wrongDocument(expectedURL: String, actualURL: String): Nothing =
    throw IllegalArgumentException("Wrong document! Should have been this url: $expectedURL, but was: $actualURL")

/**
 *  Parse data from document of this URL:
 *  http://dangky.tlu.edu.vn/CMCSoft.IU.Web.Info/StudentService/StudentTuition.aspx
 *
 *  @param resources application resource to retrieve string values
 *  @return flow of [Tuition] cold data stream
 */
@ExperimentalCoroutinesApi
fun Document.parseTuitionDataFlow(resources: Resources): Flow<Tuition> = flow {
    if (!(title() == ".: Học phí sinh viên :." || baseUri() == URL_BASE + URL_TUITION))
        throw wrongDocument(URL_TUITION, baseUri())

    getElementById("lblSoTaiKhoanNganHang")?.let {
        emit(
            Tuition(
                resources.getString(R.string.label_account_number),
                it.text().split(": ".toRegex())[1]
            )
        )
    }

    val elementsInfo: Elements? = select("#lblStudentAccount span")
    getElementById("lblStudentAccount")
        ?.text()    // "Phải đóng:  Đã đóng:  Đã rút:  Còn dư: "
        ?.split(""":\s*""".toRegex())   // [Phải đóng, Đã đóng, Đã rút, Còn dư, ]
        ?.filter(String::isNotEmpty) // [Phải đóng, Đã đóng, Đã rút, Còn dư]
        ?.let { elementsInfo?.map(Element::text)?.zip(it) } // [(40.300.000 -> Phải đóng), ..]
        ?.asFlow()
        ?.map { (amount, title) -> Tuition(title, amount) }
        ?.let { emitAll(it) }

    getElementById("lblStudent")
        ?.text()
        ?.trim()
        ?.split(" - ".toRegex())
        ?.let { (msv, ten, lop, nganh, khoa) ->
            emit(Tuition(resources.getString(R.string.label_msv), msv))
            emit(Tuition(resources.getString(R.string.label_name), ten))
            emit(Tuition(resources.getString(R.string.label_class), lop.removePrefix("Lớp ")))
            emit(Tuition(resources.getString(R.string.label_faculty), nganh.removePrefix("Ngành ")))
            emit(Tuition(resources.getString(R.string.label_courses), khoa))
        }

    selectFirst("#tblPaid")
        ?.select("tr")
        ?.asFlow()
        ?.filter { it.className() != "DataGridFixedHeader" && it.childrenSize() > 4 }
        ?.map {
            val item = it.select("td")
            Tuition(
                item[3].text(),
                item[4].text(),
                item[2].text(),
                2
            )
        }?.let { emitAll(it) }

    selectFirst("#divOut #tblPaid")
        ?.select("tr")
        ?.asFlow()
        ?.drop(1)
        ?.filter { it.childrenSize() == 4 }
        ?.map {
            val item = it.select("td")
            Tuition(
                item[2].text(),
                item[3].text(),
                item[1].text(),
                3
            )
        }?.let { emitAll(it) }
}

/**
 *  Parse data from document of this URL:
 *  http://dangky.tlu.edu.vn/CMCSoft.IU.Web.Info/Reports/Form/StudentTimeTable.aspx
 *
 *  @return flow of [Schedule] cold data stream
 */
// Warning: do not delete both this line and ExplicitTypeArguments
@Suppress("RemoveExplicitTypeArguments")
@ExperimentalCoroutinesApi
fun Document.parseScheduleDataFlow(): Flow<Schedule> {
    return takeIf { title() == ".: Thời khóa biểu sinh viên :." || baseUri() == URL_BASE + URL_TIMETABLE }
        ?.select("#gridRegistered tr")
        ?.asFlow()
        ?.drop(1)
        ?.filter { it.childrenSize() == 10 && !it.child(0).text().isNullOrEmpty() }
        ?.map {
            val item = it.select("td")
            TimeTable(
                item[1].text(),
                item[2].text(),
                item[3].text(),
                item[4].text()
            )
        }?.transform<TimeTable, TimeTable> { timeTable: TimeTable ->
            if (timeTable.time.isMulti()) {
                timeTable.time.substring(3).split("Từ".toRegex()).asFlow().map {
                    timeTable.copy(time = it)
                }.let { emitAll(it) }
            } else {
                emit(timeTable)
            }
        }?.transform { timeTable: TimeTable ->
            val (name, code, time, classRoom) = timeTable
            time.getDayOfWeek().map {
                val (from, to, classTime) = it
                val status = when {
                    classTime.toLocalDate().isBefore(LocalDate.now()) -> 2
                    classTime.toLocalDate() == LocalDate.now() -> 1
                    else -> 0
                }
                Schedule(name, code, classRoom, classTime, from, to, status)
            }.let { emitAll(it) }
        }
        ?: wrongDocument(URL_TIMETABLE, baseUri())
}

private fun String.isMulti(): Boolean = substring(30, 33).matches("""\(\d\)""".toRegex())

private fun String.getDayOfWeek(): Flow<ClassTime> = flow {
    val formatter = DateTimeFormat.forPattern(INPUT_DATE_FORMAT)
    var from = LocalDate()
    var to = LocalDate()

    val mDay = """(Thứ \d)""".toPattern().matcher(this@getDayOfWeek)
    val mPeriod = """(tiết(.*?),(\d|\d\d) )""".toPattern().matcher(this@getDayOfWeek)
    val mDate = """(\d{2}/\d{2}/\d{4})""".toPattern().matcher(this@getDayOfWeek)

    if (mDate.find()) {
        from = formatter.parseLocalDate(mDate.group())
        if (mDate.find()) to = formatter.parseLocalDate(mDate.group())
    }

    while (mPeriod.find() && mDay.find()) {
        val sPeriods = mPeriod.group().substring(5).trim().split(",".toRegex())
        val dayOfWeek = mDay.group().substring(4).toIntOrNull()
        val toPeriod = sPeriods.lastOrNull()?.toIntOrNull()
        val fromPeriod = sPeriods.getOrNull(0)?.toInt()

        if (fromPeriod != null && toPeriod != null && dayOfWeek != null) {
            val startTime = getTimeFromClassHour(fromPeriod)

            var start = from
            val thisDay = start.withDayOfWeek(dayOfWeek - 1)
            start = if (start.isAfter(thisDay)) thisDay.plusWeeks(1) else thisDay

            while (start.isBefore(to)) {
                val dateTime = LocalDateTime(
                    start.year,
                    start.monthOfYear,
                    start.dayOfMonth,
                    startTime.hourOfDay,
                    startTime.minuteOfHour
                )
                emit(
                    ClassTime(
                        fromPeriod,
                        toPeriod,
                        dateTime
                    )
                )
                start = start.plusWeeks(1)
            }
        }
    }
}

@Deprecated(
    "Mapping giờ học sau, lưu tiết học",
    ReplaceWith(
        "Lưu tiết học bắt đầu và kết thúc, lúc nào gọi ở UI thì mới map sang giờ. " +
                "Do giờ giấc ở các cơ sở khác nhau thì có thể khác nhau."
    ),
    DeprecationLevel.WARNING
)
private fun getTimeFromClassHour(classHour: Int): LocalTime = when (classHour) {
    1 -> 7 to 0
    2 -> 7 to 55
    3 -> 8 to 55
    4 -> 9 to 45
    5 -> 10 to 40
    6 -> 11 to 35
    7 -> 12 to 55
    8 -> 13 to 50
    9 -> 14 to 45
    10 -> 15 to 40
    11 -> 16 to 35
    12 -> 17 to 30
    13 -> 18 to 50
    14 -> 19 to 45
    15 -> 20 to 40
    else -> 0 to 0
}.run { LocalTime(first, second) }

/**
 * parse [SubjectWithMarks] from the [Document] of this URL:
 * http://dangky.tlu.edu.vn/CMCSoft.IU.Web.info/StudentMark.aspx
 *
 * @return [Flow] of [SubjectWithMarks] from the document
 */
@ExperimentalCoroutinesApi
fun Document.parseSubjectWithMarksFlow(): Flow<SubjectWithMarks> =
    takeIf { title() == ".: Bảng điểm :." || baseUri() == URL_BASE + URL_MARK }
        ?.select("#tblStudentMark tr")
        ?.asFlow()
        ?.filter { it.className() != "DataGridFixedHeader" && it.children().size > 13 }
        ?.map<Element, SubjectWithMarks> {
            val (maMon, ten, soTinChi, lan) = it.children().subList(1, 5).map(Element::text)
            val markPattern = """(\d+\.\d+|\d+)""".toPattern()
            val (quaTrinh, thi, tongKet) = it.children().subList(10, 13).map { child ->
                markPattern.matcher(child.text())
            }
            val diemChu = """[A-F]""".toPattern().matcher(it.child(13).text())
            require(lan.toInt() > 0)
            val marks = mutableListOf<Mark>()
            for (i: Int in 1..lan.toInt()) {
                marks.add(
                    Mark(
                        maMon, i,
                        quaTrinh.nextOrNull()?.toFloat() ?: 0f,
                        thi.nextOrNull()?.toFloat() ?: 0f,
                        tongKet.nextOrNull()?.toFloat() ?: 0f,
                        diemChu.nextOrNull()?.get(0) ?: ' '
                    )
                )
            }
            SubjectWithMarks(Subject(maMon, ten, soTinChi.toInt()), marks)
        } ?: wrongDocument(URL_MARK, baseUri())

private fun Matcher.nextOrNull(): String? = if (find()) group() else null

/**
 * parse [SummarySemester] from [Document] of this URL:
 * http://dangky.tlu.edu.vn/CMCSoft.IU.Web.info/StudentMark.aspx
 *
 * @return [Flow] of [SummarySemester] cold data stream
 */
@ExperimentalCoroutinesApi
fun Document.parseSummarySemesterFlow(): Flow<SummarySemester> =
    takeIf { title() == ".: Bảng điểm :." }
        ?.select("#grdResult tr")
        ?.asFlow()
        ?.drop(1)
        ?.map { element ->
            val indexes = setOf(0, 1, 4, 10)
            val (year, semester, cAverage, average) = indexes.map { element.child(it).text() }
            SummarySemester(
                year,
                semester,
                cAverage,
                average
            )
        } ?: wrongDocument(URL_MARK, baseUri())

/**
 * parse [PractiseSemester] from [Document] of this URL:
 * http://dangky.tlu.edu.vn/CMCSoft.IU.Web.Info/StudentService/PractiseMarkAndStudyWarning.aspx
 *
 * @return [Flow] of [PractiseSemester] cold data stream
 */
@ExperimentalCoroutinesApi
fun Document.parsePractiseMarkFlow(): Flow<PractiseSemester> =
    takeIf { title() == ".: Điểm rèn luyện và xử lý học vụ :." || baseUri() == URL_BASE + URL_PRACTISE }
        ?.selectFirst("#tblPaid")
        ?.select("tr")
        ?.asFlow()
        ?.drop(1)
        ?.filter { it.children().size == 5 }
        ?.map {
            val (_, year, semester, mark, grade) = it.children().map(Element::text)
            PractiseSemester(
                year,
                semester,
                mark,
                grade
            )
        } ?: wrongDocument(URL_PRACTISE, baseUri())

/**
 * parse [Semester] from [Document] of either this URL:
 * http://dangky.tlu.edu.vn/cmcsoft.iu.web.info/StudentViewExamList.aspx
 * or:
 * http://dangky.tlu.edu.vn/CMCSoft.IU.Web.Info/Reports/Form/StudentTimeTable.aspx
 *
 * @return [Flow] of [Semester] cold data stream
 */
fun Document.parseSemesterFlow(): Flow<Semester> =
    takeIf {
        title() == ".: Lịch thi cá nhân sinh viên :." || baseUri() == URL_BASE + URL_EXAM_TIMETABLE
                || title() == ".: Thời khóa biểu sinh viên :." || baseUri() == URL_BASE + URL_TIMETABLE
    }?.select("#drpSemester option")?.asFlow()
        ?.map {
            val code = it.`val`()
            val text = it.text()
            Semester(text.substring(2), text[0].toString(), code)
        } ?: wrongDocument("$URL_EXAM_TIMETABLE or $URL_TIMETABLE", baseUri())