package com.linn.pin.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.util.Date

@Dao
interface DingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ding: Ding)

    @Update
    suspend fun update(ding: Ding)

    @Query("select * from ding where year(createTime) = year(:yearMonth) " +
            "and month(createTime) = month(:yearMonth) order by createTime desc")
    fun findByYearMonth(yearMonth: LocalDateTime): Flow<List<Ding>>
}