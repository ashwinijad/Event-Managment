package com.example.eventmanagementapp.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.eventmanagementapp.data.local.db.AppDB
import com.example.eventmanagementapp.data.local.db.EventEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository(context: Context) {

    private var mDB: AppDB? = null
    var mEvents: LiveData<MutableList<EventEntity>>? = null

    init {
        // Initializes the AppDB instance and retrieves all events.
        mDB = AppDB.getAppDB(context)
        mEvents = getAllEvents()
    }

    companion object {
        private var instance: AppRepository? = null

        /**
         * Provides a singleton instance of AppRepository.
         * Initializes the repository if it hasn't been created yet.
         */
        fun getInstance(context: Context): AppRepository {
            if (instance == null)
                instance = AppRepository(context)
            return instance as AppRepository
        }
    }

    /**
     * Retrieves all events from the database as LiveData.
     */
    private fun getAllEvents(): LiveData<MutableList<EventEntity>>? {
        return mDB?.eventDAO()?.getAll()
    }

    /**
     * Deletes all events from the database. This operation is performed on the IO dispatcher.
     */
    suspend fun deleteAllEvents() {
        withContext(Dispatchers.IO) {
            mDB?.eventDAO()?.deleteAll()
        }
    }

    /**
     * Retrieves a specific event by its ID. This operation is performed on the IO dispatcher.
     */
    suspend fun getEventById(eventId: Int): EventEntity? {
        return withContext(Dispatchers.IO) {
            mDB?.eventDAO()?.getEventById(eventId)
        }
    }

    /**
     * Inserts a new event into the database. This operation is performed on the IO dispatcher.
     */
    suspend fun insertEvent(event: EventEntity) {
        withContext(Dispatchers.IO) {
            mDB?.eventDAO()?.insertEvent(event)
        }
    }

    /**
     * Updates an existing event in the database. This operation is performed on the IO dispatcher.
     */
    suspend fun updateEvent(event: EventEntity) {
        withContext(Dispatchers.IO) {
            mDB?.eventDAO()?.update(event)
        }
    }

    /**
     * Deletes a specific event from the database. This operation is performed on the IO dispatcher.
     */
    suspend fun deleteEvent(event: EventEntity) {
        withContext(Dispatchers.IO) {
            mDB?.eventDAO()?.deleteEvent(event)
        }
    }
}
