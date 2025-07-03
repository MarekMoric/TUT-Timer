package com.mendelu.xmoric.tuttimer

import android.app.Application
import android.content.Context
import com.mendelu.xmoric.tuttimer.di.daoModule
import com.mendelu.xmoric.tuttimer.di.databaseModule
import com.mendelu.xmoric.tuttimer.di.repositoryModule
import com.mendelu.xmoric.tuttimer.di.viewModelModule
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class TUTTimerApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@TUTTimerApplication)
            modules(listOf(
                databaseModule,
                daoModule,
                repositoryModule,
                viewModelModule
            ))
        }
    }

    companion object {
        lateinit var appContext: Context
            private set
    }
}