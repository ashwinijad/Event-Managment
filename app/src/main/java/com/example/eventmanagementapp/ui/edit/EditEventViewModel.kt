package com.example.eventmanagementapp.ui.edit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.eventmanagementapp.data.local.db.EventEntity
import com.example.eventmanagementapp.data.repository.AppRepository
import kotlinx.coroutines.launch

class EditEventViewModel(application: Application) : AndroidViewModel(application) {
    // LiveData to hold the event data
    var mLiveData: MutableLiveData<EventEntity?> = MutableLiveData()

    // Repository instance to handle data operations
    private var mRepository: AppRepository? = null

    init {
        // Initializes the repository
        mRepository = AppRepository.getInstance(application)
    }

    /**
     * Loads event data based on the event ID and updates the LiveData.
     */
    fun loadData(eventId: Int) {
        viewModelScope.launch {
            val event = mRepository?.getEventById(eventId)
            mLiveData.postValue(event)
        }
    }

    /**
     * Saves a new event to the repository.
     */
    fun saveEvent(event: EventEntity) {
        viewModelScope.launch {
            mRepository?.insertEvent(event)
        }
    }

    /**
     * Updates an existing event in the repository.
     */
    fun updateEvent(event: EventEntity) {
        viewModelScope.launch {
            mRepository?.updateEvent(event)
        }
    }

    /**
     * Deletes the current event from the repository.
     */
    fun deleteEvent() {
        viewModelScope.launch {
            mRepository?.deleteEvent(mLiveData.value!!)
        }
    }
}


