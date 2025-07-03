package com.mendelu.xmoric.tuttimer.database

import kotlinx.coroutines.flow.Flow

class ActivityLocalRepositoryImpl (private val activitiesDao: ActivitiesDao) : IActivityLocalRepository {
    override fun getAll(): Flow<List<Activity>> {
        return activitiesDao.getAll()
    }

    override suspend fun findById(id: Long): Activity {
        return activitiesDao.findById(id)
    }

    override suspend fun insert(activity: Activity): Long {
        return activitiesDao.insert(activity)
    }

    override suspend fun update(activity: Activity) {
        activitiesDao.update(activity)
    }

    override suspend fun delete(activity: Activity) {
        activitiesDao.delete(activity)
    }
}