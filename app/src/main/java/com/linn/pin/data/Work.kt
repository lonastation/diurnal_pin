package com.linn.pin.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "work")
data class Work(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val createTime: LocalDateTime
)