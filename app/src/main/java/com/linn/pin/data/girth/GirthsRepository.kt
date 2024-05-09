package com.linn.pin.data.girth

import kotlinx.coroutines.flow.Flow

interface GirthsRepository {
    suspend fun insert(girth: Girth)
    fun findAll(): Flow<List<Girth>>
}