package com.toandv.mytlu.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
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
}