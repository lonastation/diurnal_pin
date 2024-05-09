package com.linn.pin.data.girth

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GirthDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(girth: Girth)

    @Query("select * from girth order by createTime desc limit :limit")
    fun findAll(limit: Int): Flow<List<Girth>>
}