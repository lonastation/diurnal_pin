package com.linn.pin.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "girth")
data class Girth(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val time: Date,
    val number1: Double,
    val number2: Double
)