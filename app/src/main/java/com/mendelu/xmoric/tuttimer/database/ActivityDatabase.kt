package com.mendelu.xmoric.tuttimer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Activity::class, Result::class], version = 7, exportSchema = false)
abstract class ActivityDatabase : RoomDatabase()  {

    abstract fun activitiesDao(): ActivitiesDao
    abstract fun resultsDao(): ResultsDao

    companion object {

        private var INSTANCE: ActivityDatabase? = null

        fun getDatabase(context: Context): ActivityDatabase {
            if (INSTANCE == null) {
                synchronized(ActivityDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            ActivityDatabase::class.java, "activity_database"
                        ).fallbackToDestructiveMigration().build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}