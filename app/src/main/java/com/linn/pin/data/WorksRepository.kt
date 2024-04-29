package com.linn.pin.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface WorksRepository {
    suspend fun insertLog(work: Work)
    suspend fun updateLog(work: Work)
    fun getLogs(yearMonth: LocalDateTime): Flow<List<Work>>
}