package com.toandv.mytlu.remote

import android.content.res.Resources
import com.toandv.mytlu.R
import com.toandv.mytlu.local.entity.ExamTimetable
import com.toandv.mytlu.local.entity.Tuition
import com.toandv.mytlu.utils.INPUT_DATE_TIME_FORMAT
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import org.joda.time.format.DateTimeFormat
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

@ExperimentalCoroutinesApi
fun Document.parseExamTableDataFlow(): Flow<ExamTimetable> {
    val formatter = DateTimeFormat.forPattern(INPUT_DATE_TIME_FORMAT)
    return select("#tblCourseList tr")?.asFlow()
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
fun Document.parseTuitionData(resources: Resources): Flow<Tuition> = flow {
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