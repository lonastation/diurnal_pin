package com.linn.pin.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface DingsRepository {
    suspend fun insertDing(ding: Ding)
    suspend fun updateDing(ding: Ding)
    fun getDings(yearMonth: LocalDateTime): Flow<List<Ding>>
}