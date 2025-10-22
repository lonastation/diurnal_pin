package com.linn.pin.data.length

import kotlinx.coroutines.flow.Flow

interface LengthRepository {
    suspend fun insert(length: Length)
    fun findAll(): Flow<List<Length>>
    fun findNumber1AtAm(): Flow<List<Length>>
    fun findNumber1AtPm(): Flow<List<Length>>
    fun findNumber2(): Flow<List<Length>>
}