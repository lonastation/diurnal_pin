package com.linn.pin.data.length

import kotlinx.coroutines.flow.Flow

class OfflineLengthRepository(private val lengthDao: LengthDao) : LengthRepository {
    override suspend fun insert(length: Length) = lengthDao.insert(length)
    override fun findAll(): Flow<List<Length>> = lengthDao.findAll(60)
    override fun findNumber1AtAm(): Flow<List<Length>> = lengthDao.findNumber1AtAm(30)
    override fun findNumber1AtPm(): Flow<List<Length>> = lengthDao.findNumber1AtPm(30)
    override fun findNumber2(): Flow<List<Length>> = lengthDao.findNumber2(30)
}