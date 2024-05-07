package com.linn.pin.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface WorksRepository {
    suspend fun insertLog()
    suspend fun updateLog(work: Work)
    fun logs(yearMonth: LocalDateTime): Flow<List<Work>>
}