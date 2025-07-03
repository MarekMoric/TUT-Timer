package com.mendelu.xmoric.tuttimer.database

import kotlinx.coroutines.flow.Flow

class ResultLocalRepositoryImpl (private val resultsDao: ResultsDao) : IResultLocalRepository {
    override fun getAll(): Flow<List<Result>> {
        return resultsDao.getAll()
    }

    override suspend fun findById(id: Long): Result {
        return resultsDao.findById(id)
    }

    override suspend fun insert(result: Result): Long {
        return resultsDao.insert(result)
    }

    override suspend fun delete(result: Result) {
        resultsDao.delete(result)
    }
}