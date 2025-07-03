package com.mendelu.xmoric.tuttimer.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mendelu.xmoric.tuttimer.authentication.AuthRepository
import com.mendelu.xmoric.tuttimer.authentication.AuthRepositoryImpl
import com.mendelu.xmoric.tuttimer.database.*
import org.koin.dsl.module

val repositoryModule = module {
    fun provideLocalActivityRepository(dao: ActivitiesDao): IActivityLocalRepository {
        return ActivityLocalRepositoryImpl(dao)
    }
    fun provideLocalResultRepository(dao: ResultsDao): IResultLocalRepository {
        return ResultLocalRepositoryImpl(dao)
    }

    fun provideAuthRepository(): AuthRepository = AuthRepositoryImpl(
        auth = Firebase.auth
    )

    single { provideLocalActivityRepository(get()) }
    single { provideLocalResultRepository(get()) }
    single { provideAuthRepository() }

}