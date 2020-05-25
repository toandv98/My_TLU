package com.toandv.mytlu.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.toandv.mytlu.utils.PATTERN_DATE_FORMAT
import com.toandv.mytlu.utils.PATTERN_TIME_FORMAT
import org.joda.time.LocalDateTime

@Entity(tableName = "exam_timetables")
data class ExamTimetable(

    val code: String,

    val name: String,

    val st: String,

    @ColumnInfo(name = "datetime")
    val dateTime: LocalDateTime,

    val time: String,

    val sbd: String,

    val room: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    override fun toString(): String {
        return "%s\n%s\n%s\n%s\n%s\n%s".format(
            name,
            dateTime.toString(PATTERN_DATE_FORMAT),
            dateTime.toString(PATTERN_TIME_FORMAT),
            time,
            room,
            sbd
        )
    }
}