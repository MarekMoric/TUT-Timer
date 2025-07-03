package com.mendelu.xmoric.tuttimer.di

import com.mendelu.xmoric.tuttimer.database.ActivitiesDao
import com.mendelu.xmoric.tuttimer.database.ActivityDatabase
import com.mendelu.xmoric.tuttimer.database.ResultsDao
import org.koin.dsl.module

val daoModule = module {
    fun provideActivitiesDao(database: ActivityDatabase): ActivitiesDao = database.activitiesDao()
    fun provideResultsDao(database: ActivityDatabase): ResultsDao = database.resultsDao()
    single {
        provideActivitiesDao(get())
    }
    single {
        provideResultsDao(get())
    }
}