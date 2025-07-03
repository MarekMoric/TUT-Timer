package com.mendelu.xmoric.tuttimer.database

import kotlinx.coroutines.flow.Flow

interface IActivityLocalRepository {
    fun getAll(): Flow<List<Activity>>
    suspend fun findById(id : Long): Activity
    suspend fun insert(activity: Activity): Long
    suspend fun update(activity: Activity)
    suspend fun delete(activity: Activity)
}