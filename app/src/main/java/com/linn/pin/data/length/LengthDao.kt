package com.linn.pin.data.length

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface LengthDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(length: Length)

    @Query("select * from length order by createTime desc")
    fun findAllDesc(): Flow<List<Length>>

    @Query("select * from length order by createTime asc")
    fun findAllAsc():  Flow<List<Length>>

    @Query(
        "select * from length where createTime >= :startDate " +
                " order by createTime asc"
    )
    fun findLatest(startDate: LocalDateTime): Flow<List<Length>>

    fun findLast30(): Flow<List<Length>> {
        val startDate = LocalDateTime.now().minusDays(30)
        return findLatest(startDate)
    }

    fun findLast90(): Flow<List<Length>> {
        val startDate = LocalDateTime.now().minusDays(90)
        return findLatest(startDate)
    }
}