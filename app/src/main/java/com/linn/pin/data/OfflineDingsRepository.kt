package com.linn.pin.data

import kotlinx.coroutines.flow.Flow
import java.util.Date

class OfflineDingsRepository(private val dingDao: DingDao) : DingsRepository {

    override fun getDings(yearMonth: Date): Flow<List<Ding>> = dingDao.findByYearMonth(yearMonth)

    override suspend fun insertDing(ding: Ding) = dingDao.insert(ding)

    override suspend fun updateDing(ding: Ding) = dingDao.update(ding)
}