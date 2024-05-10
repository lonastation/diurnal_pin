package com.linn.pin.data.girth

import kotlinx.coroutines.flow.Flow

interface GirthsRepository {
    suspend fun insert(girth: Girth)
    fun findAll(): Flow<List<Girth>>
    fun findNumber1AtAm(): Flow<List<Girth>>
    fun findNumber1AtPm(): Flow<List<Girth>>
    fun findNumber2(): Flow<List<Girth>>
}