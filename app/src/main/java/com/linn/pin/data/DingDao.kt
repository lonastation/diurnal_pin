package com.linn.pin.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface DingDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(ding: Ding)

    @Update
    suspend fun update(ding: Ding)

    @Query("select * from ding where year(time) = year(:yearMonth) " +
            "and month(time) = month(:yearMonth) order by time desc")
    fun findByYearMonth(yearMonth: Date): Flow<List<Ding>>
}