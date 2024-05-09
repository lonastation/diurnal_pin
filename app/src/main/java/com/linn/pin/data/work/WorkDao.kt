package com.linn.pin.data.work

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(work: Work)

    @Update
    suspend fun update(work: Work)

    @Query("select * from work order by createTime desc limit :limit")
    fun findByLimit(limit: Int): Flow<List<Work>>
}