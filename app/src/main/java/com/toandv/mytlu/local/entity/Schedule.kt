package com.toandv.mytlu.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.toandv.mytlu.utils.PATTERN_DATE_FORMAT
import com.toandv.mytlu.utils.PATTERN_TIME_FORMAT
import org.joda.time.LocalDateTime

@Entity(tableName = "schedule")
data class Schedule(
    val name: String,
    val code: String,
    val room: String,
    @ColumnInfo(name = "datetime")
    val time: LocalDateTime,
    val fromPeriod: Int,
    val toPeriod: Int,
    val status: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}