package com.example.eventmanagementapp.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.eventmanagementapp.data.local.db.EventEntity
import com.example.eventmanagementapp.data.repository.AppRepository
import kotlinx.coroutines.launch


class MainViewModel(application: Application) : AndroidViewModel(application) {
    var mEvents: LiveData<MutableList<EventEntity>>
    private var mRepository: AppRepository? = null

    init {
        mRepository = AppRepository.getInstance(application.applicationContext)
        mEvents = mRepository?.mEvents!!
    }

    fun deleteAllEvents() {
        viewModelScope.launch {
            mRepository?.deleteAllEvents()
        }
    }

}