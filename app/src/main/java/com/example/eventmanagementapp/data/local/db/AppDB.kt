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

    /**
     * Provides access to the EventDAO for database operations.
     */
    abstract fun eventDAO(): EventDAO

    companion object {
        private const val DB_NAME = "Event.db"
        @Volatile
        private var instance: AppDB? = null

        /**
         * Returns a singleton instance of the AppDB. If the instance is not already created,
         * it initializes the Room database.
         */
        fun getAppDB(context: Context): AppDB? {
            if (instance == null) {
                //Used synchronized to ensure thread safety when creating the singleton instance.
                // This prevents multiple threads from creating multiple instances of the repository.
                synchronized(AppDB::class) {
                    if(instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            AppDB::class.java,
                            DB_NAME
                        )
                            .fallbackToDestructiveMigration() // Recreates the database if the schema is changed
                            .build()
                    }
                }
            }
            return instance
        }
    }
}
