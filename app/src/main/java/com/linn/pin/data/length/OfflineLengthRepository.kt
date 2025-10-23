package com.linn.pin.data.length

import kotlinx.coroutines.flow.Flow

class OfflineLengthRepository(private val lengthDao: LengthDao) : LengthRepository {
    override suspend fun insert(length: Length) = lengthDao.insert(length)
    override fun findAllDesc(): Flow<List<Length>> = lengthDao.findAllDesc()
    override fun findAllAsc(): Flow<List<Length>> = lengthDao.findAllAsc()
    override fun findLast30(): Flow<List<Length>> = lengthDao.findLast30()
    override fun findLast90(): Flow<List<Length>> = lengthDao.findLast90()
}