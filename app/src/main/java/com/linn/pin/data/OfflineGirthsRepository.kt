package com.linn.pin.data

import kotlinx.coroutines.flow.Flow

class OfflineGirthsRepository(private val girthDao: GirthDao) : GirthsRepository {
    override suspend fun insert(girth: Girth) = girthDao.insert(girth)
    override fun findAll(): Flow<List<Girth>> = girthDao.findAll()
}