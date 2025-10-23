package com.linn.pin.data.length

import kotlinx.coroutines.flow.Flow

class OfflineLengthRepository(private val lengthDao: LengthDao) : LengthRepository {
    override suspend fun insert(length: Length) = lengthDao.insert(length)
    override fun findAll(): Flow<List<Length>> = lengthDao.findAll(60)
    override fun findLast30(): Flow<List<Length>> = lengthDao.findLast30()
    override fun findLast90(): Flow<List<Length>> = lengthDao.findLast90()
}