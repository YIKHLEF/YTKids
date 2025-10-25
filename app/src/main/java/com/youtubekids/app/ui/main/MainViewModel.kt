package com.youtubekids.app.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.youtubekids.app.data.local.AppDatabase
import com.youtubekids.app.data.local.entities.VideoEntity
import com.youtubekids.app.data.repository.RetrofitInstance
import com.youtubekids.app.data.repository.VideoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val repository = VideoRepository(
        database.videoDao(),
        database.settingsDao(),
        RetrofitInstance.youTubeApiService
    )

    val videos = repository.getAllVideos()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        checkFirstLaunch()
    }

    private fun checkFirstLaunch() {
        viewModelScope.launch {
            val isFirstLaunch = repository.isFirstLaunch()
            if (isFirstLaunch) {
                // First launch handling can be done in the activity
            }
        }
    }
}
