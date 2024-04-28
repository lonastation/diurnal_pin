package com.linn.pin.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "ding")
data class Ding(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val time: Date
)