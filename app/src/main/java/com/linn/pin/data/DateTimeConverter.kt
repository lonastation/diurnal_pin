package com.linn.pin.data


import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


class DateTimeConverter {

    private val dateFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)

    @TypeConverter
    fun fromString(value: String?): LocalDateTime? {
        if (value != null) {
            return LocalDateTime.parse(value, dateFormatter)
        }
        return null
    }

    @TypeConverter
    fun fromLocalDateTime(date: LocalDateTime?): String? {
        return date?.format(dateFormatter)
    }
}