package com.toandv.mytlu.local.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import org.joda.time.LocalDateTime

/**
 * @param semester học kỳ
 * @param dot đợt
 * @param code mã môn
 * @param name tên
 * @param st số tín
 * @param dateTime ngày tháng
 * @param time thời gian
 * @param sbd số báo danh
 * @param room phòng
 */
@Entity(
    tableName = "exam_timetables",
    primaryKeys = ["year", "semester", "dot", "code"],
    foreignKeys = [ForeignKey(
        entity = Semester::class,
        parentColumns = ["year", "semester"],
        childColumns = ["year", "semester"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class ExamTimetable(
    @Embedded
    val semester: Semester,

    val dot: String,

    @ColumnInfo(name = "ma_mon")
    val code: String,

    val name: String,

    val st: String,

    @ColumnInfo(name = "datetime")
    val dateTime: LocalDateTime,

    val time: String,

    val sbd: String,

    val room: String
)