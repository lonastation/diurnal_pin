package com.linn.pin.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "ding")
data class Ding(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val createTime: LocalDateTime
)