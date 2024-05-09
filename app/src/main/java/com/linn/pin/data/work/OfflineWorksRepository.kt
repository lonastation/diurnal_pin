package com.linn.pin.data.work

import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class OfflineWorksRepository(private val workDao: WorkDao) : WorksRepository {
    override suspend fun insertLog() {
        workDao.insert(Work(createTime = LocalDateTime.now()))
    }

    override suspend fun updateLog(work: Work) {
        workDao.update(work)
    }

    override fun logs(count: Int): Flow<List<Work>> {
        return workDao.findByLimit(count)
    }

}