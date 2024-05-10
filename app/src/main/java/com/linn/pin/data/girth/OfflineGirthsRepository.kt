package com.linn.pin.data.girth

import kotlinx.coroutines.flow.Flow

class OfflineGirthsRepository(private val girthDao: GirthDao) : GirthsRepository {
    override suspend fun insert(girth: Girth) = girthDao.insert(girth)
    override fun findAll(): Flow<List<Girth>> = girthDao.findAll(60)
    override fun findNumber1AtAm(): Flow<List<Girth>> = girthDao.findNumber1AtAm(30)
    override fun findNumber1AtPm(): Flow<List<Girth>> = girthDao.findNumber1AtPm(30)
    override fun findNumber2(): Flow<List<Girth>> = girthDao.findNumber2(30)
}