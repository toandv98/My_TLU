package com.toandv.mytlu.remote

import android.content.res.Resources
import com.toandv.mytlu.R
import com.toandv.mytlu.local.entity.DetailMark
import com.toandv.mytlu.local.entity.ExamTimetable
import com.toandv.mytlu.local.entity.Schedule
import com.toandv.mytlu.local.entity.Tuition
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

@ExperimentalCoroutinesApi
fun Document.parseExamTableDataFlow(): Flow<ExamTimetable> {
    val formatter = DateTimeFormat.forPattern(INPUT_DATE_TIME_FORMAT)
    return takeIf { title() == ".: Lịch thi cá nhân sinh viên :." }
        ?.select("#tblCourseList tr")
        ?.asFlow()
        ?.filter { it.className() != "DataGridFixedHeader" && it.childrenSize() > 8 }
        ?.map { element ->
            val items = element.select("td")
            val rawTime = items[5].text()
            val matcher = """\d\d:\d\d""".toPattern().matcher(rawTime)
            ExamTimetable(
                items[1].text(),
                items[2].text(),
                items[3].text(),
                formatter.parseLocalDateTime(
                    "%s %s".format(
                        items[4].text(),
                        if (matcher.find()) matcher.group() else "00:00"
                    )
                ),
                rawTime,
                items[7].text(),
                items[8].text()
            )
        } ?: flow { }
}

@ExperimentalCoroutinesApi
fun Document.parseTuitionDataFlow(resources: Resources): Flow<Tuition> = flow {
    if (title() != ".: Học phí sinh viên :.") return@flow

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
        ?.filter { it.className() != "DataGridFixedHeader" && it.childrenSize() > 3 }
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

@ExperimentalCoroutinesApi
fun Document.parseScheduleDataFlow(): Flow<Schedule> =
    takeIf { title() == ".: Thời khóa biểu sinh viên :." }
        ?.select("#gridRegistered tr")
        ?.asFlow()
        ?.filter {
            it.className() != "DataGridFixedHeader"
                    && it.childrenSize() > 4
                    && !it.child(0).text().isNullOrEmpty()
        }
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
        } ?: flow { }

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

private data class ClassTime(
    val fromPeriod: Int,
    val toPeriod: Int,
    val classTime: LocalDateTime
)

private data class TimeTable(
    val name: String,
    val code: String,
    val time: String,
    val room: String
) {
    val id = 0
}

fun Document.parseStudentMarkDataFlow(): Flow<DetailMark> = TODO()