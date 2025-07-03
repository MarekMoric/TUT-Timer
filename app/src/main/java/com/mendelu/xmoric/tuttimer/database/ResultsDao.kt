package com.mendelu.xmoric.tuttimer.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ResultsDao {
    @Query("SELECT * FROM results")
    fun getAll(): Flow<List<Result>>

    @Query("SELECT * FROM results WHERE id = :id")
    suspend fun findById(id : Long): Result

    @Insert
    suspend fun insert(result: Result): Long

    @Delete
    suspend fun delete(result: Result)
}