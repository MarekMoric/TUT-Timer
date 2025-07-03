package com.mendelu.xmoric.tuttimer.database

import kotlinx.coroutines.flow.Flow

interface IResultLocalRepository {
    fun getAll(): Flow<List<Result>>
    suspend fun findById(id : Long): Result
    suspend fun insert(result: Result): Long
    suspend fun delete(result: Result)
}