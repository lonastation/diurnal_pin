package com.linn.pin.data.length

import kotlinx.coroutines.flow.Flow

interface LengthRepository {
    suspend fun insert(length: Length)
    fun findAll(): Flow<List<Length>>
    fun findNumberAtAm(): Flow<List<Length>>
    fun findNumberAtPm(): Flow<List<Length>>
}