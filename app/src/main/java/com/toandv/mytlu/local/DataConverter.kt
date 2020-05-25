package com.toandv.mytlu.local

import androidx.room.TypeConverter
import org.joda.time.LocalDateTime

class DataConverter {
    @TypeConverter
    fun fromString(time: String?): LocalDateTime? {
        return time?.let { LocalDateTime(it) }
    }

    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? {
        return dateTime?.toString()
    }
}