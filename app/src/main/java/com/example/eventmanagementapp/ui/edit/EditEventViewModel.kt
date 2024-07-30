package com.example.eventmanagementapp.ui.edit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.eventmanagementapp.data.local.db.EventEntity
import com.example.eventmanagementapp.data.repository.AppRepository
import kotlinx.coroutines.launch

class EditEventViewModel(application: Application) : AndroidViewModel(application) {
    var mLiveData: MutableLiveData<EventEntity?> = MutableLiveData()
    private var mRepository: AppRepository? = null

    init {
        mRepository = AppRepository.getInstance(application)
    }

    fun loadData(eventId: Int) {
        viewModelScope.launch {
            val event = mRepository?.getEventById(eventId)
            mLiveData.postValue(event)
        }
    }

    fun saveEvent(event: EventEntity) {
        viewModelScope.launch {
            mRepository?.insertEvent(event)
        }
    }

    fun updateEvent(event: EventEntity) {
        viewModelScope.launch {
            mRepository?.updateEvent(event)
        }
    }

    fun deleteEvent() {
        viewModelScope.launch {
            mRepository?.deleteEvent(mLiveData.value!!)
        }
    }
}

