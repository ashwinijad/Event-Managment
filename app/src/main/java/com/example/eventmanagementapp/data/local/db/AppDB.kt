package com.example.eventmanagementapp.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.eventmanagementapp.utils.Converters

@Database(entities = [EventEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDB : RoomDatabase() {

    abstract fun eventDAO(): EventDAO

    companion object {
        private const val DB_NAME = "Event.db"
        @Volatile
        private var instance: AppDB? = null

        fun getAppDB(context: Context): AppDB? {
            if (instance == null) {
                synchronized(AppDB::class) {
                    if(instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            AppDB::class.java,
                            DB_NAME
                        )
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return instance
        }
    }
}