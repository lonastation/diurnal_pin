package com.linn.pin.data.work

import kotlinx.coroutines.flow.Flow

interface WorksRepository {
    suspend fun insertLog()
    suspend fun updateLog(work: Work)
    fun logs(count: Int): Flow<List<Work>>
}