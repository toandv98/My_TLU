package com.toandv.mytlu.data.local.entity

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

    override fun toString(): String {
        return "%s\n%s\n%s\n%s\n%d - %d".format(
            name,
            time.toString(PATTERN_DATE_FORMAT),
            time.toString(PATTERN_TIME_FORMAT),
            room,
            fromPeriod,
            toPeriod
        )
    }
}