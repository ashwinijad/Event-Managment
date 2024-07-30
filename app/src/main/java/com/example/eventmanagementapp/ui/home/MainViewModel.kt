package com.example.eventmanagementapp.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.eventmanagementapp.data.local.db.EventEntity
import com.example.eventmanagementapp.data.repository.AppRepository
import kotlinx.coroutines.launch


class MainViewModel(application: Application) : AndroidViewModel(application) {
    // LiveData that observes the list of events
    var mEvents: LiveData<MutableList<EventEntity>>

    // Repository instance to handle data operations
    private var mRepository: AppRepository? = null

    init {
        // Initializes the repository and observes the event list
        mRepository = AppRepository.getInstance(application.applicationContext)
        mEvents = mRepository?.mEvents!!
    }

    /**
     * Deletes all events by calling the repository's deleteAllEvents method.
     */
    fun deleteAllEvents() {
        viewModelScope.launch {
            mRepository?.deleteAllEvents()
        }
    }
}
