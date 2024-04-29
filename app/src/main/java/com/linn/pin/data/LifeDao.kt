package com.linn.pin.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LifeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(life: Life)

    @Query("select * from life order by createTime desc")
    suspend fun findAll(): Flow<List<Life>>
}