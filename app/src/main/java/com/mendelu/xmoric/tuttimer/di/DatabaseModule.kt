package com.mendelu.xmoric.tuttimer.di

import com.mendelu.xmoric.tuttimer.TUTTimerApplication
import com.mendelu.xmoric.tuttimer.database.ActivityDatabase
import org.koin.dsl.module

val databaseModule = module {
    fun provideDatabase(): ActivityDatabase = ActivityDatabase.getDatabase(TUTTimerApplication.appContext)
    single {
        provideDatabase()
    }
}