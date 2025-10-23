package com.linn.pin.data.length

import kotlinx.coroutines.flow.Flow

class OfflineLengthRepository(private val lengthDao: LengthDao) : LengthRepository {
    override suspend fun insert(length: Length) = lengthDao.insert(length)
    override fun findAll(): Flow<List<Length>> = lengthDao.findAll(60)
    override fun findNumberAtAm(): Flow<List<Length>> = lengthDao.findNumberAtAm(30)
    override fun findNumberAtPm(): Flow<List<Length>> = lengthDao.findNumberAtPm(30)
}