package com.linn.pin.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "life")
data class Life(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val createTime: LocalDateTime,
    val number1: Double,
    val number2: Double
)