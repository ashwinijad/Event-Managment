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
        mDB = AppDB.getAppDB(context)
        mEvents = getAllEvents()
    }

    companion object {
        private var instance: AppRepository? = null
        fun getInstance(context: Context): AppRepository {
            if (instance == null)
                instance = AppRepository(context)
            return instance as AppRepository
        }
    }

    private fun getAllEvents(): LiveData<MutableList<EventEntity>>? {
        return mDB?.eventDAO()?.getAll()
    }

    suspend fun deleteAllEvents() {
        withContext(Dispatchers.IO) {
            mDB?.eventDAO()?.deleteAll()
        }
    }

    suspend fun getEventById(eventId: Int): EventEntity? {
        return withContext(Dispatchers.IO) {
            mDB?.eventDAO()?.getEventById(eventId)
        }
    }

    suspend fun insertEvent(event: EventEntity) {
        withContext(Dispatchers.IO) {
            mDB?.eventDAO()?.insertEvent(event)
        }
    }

    suspend fun updateEvent(event: EventEntity) {
        withContext(Dispatchers.IO) {
            mDB?.eventDAO()?.update(event)
        }
    }

    suspend fun deleteEvent(event: EventEntity) {
        withContext(Dispatchers.IO) {
            mDB?.eventDAO()?.deleteEvent(event)
        }
    }
}
