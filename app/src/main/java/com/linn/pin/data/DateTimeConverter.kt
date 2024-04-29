package com.linn.pin.data


import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class DateTimeConverter {

    private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

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