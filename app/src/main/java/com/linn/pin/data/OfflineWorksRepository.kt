package com.linn.pin.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class OfflineWorksRepository(private val workDao: WorkDao) : WorksRepository {

    override fun getLogs(yearMonth: LocalDateTime): Flow<List<Work>> = workDao.findByYearMonth(yearMonth)

    override suspend fun insertLog(work: Work) = workDao.insert(work)

    override suspend fun updateLog(work: Work) = workDao.update(work)
}