package com.mendelu.xmoric.tuttimer.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivitiesDao {
    @Query("SELECT * FROM activities")
    fun getAll(): Flow<List<Activity>>

    @Query("SELECT * FROM activities WHERE id = :id")
    suspend fun findById(id : Long): Activity

    @Insert
    suspend fun insert(activity: Activity): Long

    @Update
    suspend fun update(activity: Activity)

    @Delete
    suspend fun delete(activity: Activity)
}