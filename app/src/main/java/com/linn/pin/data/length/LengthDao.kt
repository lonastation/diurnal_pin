package com.linn.pin.data.length

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LengthDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(length: Length)

    @Query("select * from length order by createTime desc limit :limit")
    fun findAll(limit: Int): Flow<List<Length>>

    @Query(
        "select * from length where substr(createTime, 12, 8) <= '12:00:00' " +
                " order by createTime desc limit :limit"
    )
    fun findNumberAtAm(limit: Int): Flow<List<Length>>

    @Query(
        "select * from length where substr(createTime, 12, 8) > '12:00:00' " +
                " order by createTime desc limit :limit"
    )
    fun findNumberAtPm(limit: Int): Flow<List<Length>>
}