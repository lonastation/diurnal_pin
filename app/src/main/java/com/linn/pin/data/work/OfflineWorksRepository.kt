package com.linn.pin.data.work

import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class OfflineWorksRepository(private val workDao: WorkDao) : WorksRepository {
    override suspend fun insertLog() {
        workDao.insert(Work(createTime = LocalDateTime.now()))
    }


    override suspend fun updateLog(work: Work) {
        workDao.update(work)
    }

    override fun logs(yearMonth: LocalDateTime): Flow<List<Work>> {
        val pattern = yearMonth.format(DateTimeFormatter.ofPattern("yyyy-MM")) + "%"
        return workDao.findByYearMonth(pattern)
    }

}