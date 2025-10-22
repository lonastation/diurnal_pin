package com.linn.pin.data.length

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "length")
data class Length(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val createTime: LocalDateTime,
    val number1: Double,
    val number2: Double
)