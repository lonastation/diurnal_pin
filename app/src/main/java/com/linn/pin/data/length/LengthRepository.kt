package com.linn.pin.data.length

import kotlinx.coroutines.flow.Flow

interface LengthRepository {
    suspend fun insert(length: Length)
    fun findAll(): Flow<List<Length>>
    fun findLast30(): Flow<List<Length>>
    fun findLast90(): Flow<List<Length>>
}