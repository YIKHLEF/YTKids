package com.youtubekids.app.ui.admin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.youtubekids.app.data.local.AppDatabase
import com.youtubekids.app.data.local.entities.VideoEntity
import com.youtubekids.app.data.repository.RetrofitInstance
import com.youtubekids.app.data.repository.VideoRepository
import com.youtubekids.app.utils.Constants
import com.youtubekids.app.utils.SecurityUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class PinState {
    object Idle : PinState()
    object Creating : PinState()
    object Verifying : PinState()
    object Verified : PinState()
    data class Error(val message: String) : PinState()
}

sealed class VideoAddState {
    object Idle : VideoAddState()
    object Loading : VideoAddState()
    data class Preview(val video: VideoEntity) : VideoAddState()
    data class PlaylistPreview(val videos: List<VideoEntity>) : VideoAddState()
    data class Success(val count: Int) : VideoAddState()
    data class Error(val message: String) : VideoAddState()
}

class AdminViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val repository = VideoRepository(
        database.videoDao(),
        database.settingsDao(),
        RetrofitInstance.youTubeApiService
    )

    val videos = repository.getAllVideos()

    private val _pinState = MutableStateFlow<PinState>(PinState.Idle)
    val pinState: StateFlow<PinState> = _pinState.asStateFlow()

    private val _videoAddState = MutableStateFlow<VideoAddState>(VideoAddState.Idle)
    val videoAddState: StateFlow<VideoAddState> = _videoAddState.asStateFlow()

    private var pinSalt: String? = null

    init {
        checkPinSetup()
    }

    private fun checkPinSetup() {
        viewModelScope.launch {
            val storedPin = repository.getSetting(Constants.SETTING_PIN_CODE)
            val storedSalt = repository.getSetting("pin_salt")

            if (storedPin == null || storedSalt == null) {
                _pinState.value = PinState.Creating
            } else {
                pinSalt = storedSalt
                _pinState.value = PinState.Verifying
            }
        }
    }

    fun createPin(pin: String) {
        viewModelScope.launch {
            val salt = SecurityUtils.generateSalt()
            val hashedPin = SecurityUtils.hashPin(pin, salt)

            repository.saveSetting(Constants.SETTING_PIN_CODE, hashedPin)
            repository.saveSetting("pin_salt", salt)
            repository.setFirstLaunchComplete()

            _pinState.value = PinState.Verified
        }
    }

    fun verifyPin(pin: String) {
        viewModelScope.launch {
            val storedHash = repository.getSetting(Constants.SETTING_PIN_CODE)
            val salt = repository.getSetting("pin_salt")

            if (storedHash != null && salt != null) {
                if (SecurityUtils.verifyPin(pin, storedHash, salt)) {
                    _pinState.value = PinState.Verified
                } else {
                    _pinState.value = PinState.Error("Code PIN incorrect")
                }
            } else {
                _pinState.value = PinState.Error("Erreur de configuration")
            }
        }
    }

    fun fetchVideoDetails(videoId: String) {
        viewModelScope.launch {
            _videoAddState.value = VideoAddState.Loading
            val result = repository.fetchVideoDetails(videoId)

            _videoAddState.value = if (result.isSuccess) {
                VideoAddState.Preview(result.getOrNull()!!)
            } else {
                VideoAddState.Error(result.exceptionOrNull()?.message ?: "Erreur lors de la récupération")
            }
        }
    }

    fun fetchPlaylistVideos(playlistId: String) {
        viewModelScope.launch {
            _videoAddState.value = VideoAddState.Loading
            val result = repository.fetchPlaylistVideos(playlistId)

            _videoAddState.value = if (result.isSuccess) {
                val videos = result.getOrNull()!!
                VideoAddState.PlaylistPreview(videos)
            } else {
                VideoAddState.Error(result.exceptionOrNull()?.message ?: "Erreur lors de la récupération")
            }
        }
    }

    fun addVideo(video: VideoEntity) {
        viewModelScope.launch {
            repository.insertVideo(video)
            _videoAddState.value = VideoAddState.Success(1)
        }
    }

    fun addVideos(videos: List<VideoEntity>) {
        viewModelScope.launch {
            repository.insertVideos(videos)
            _videoAddState.value = VideoAddState.Success(videos.size)
        }
    }

    fun deleteVideo(video: VideoEntity) {
        viewModelScope.launch {
            repository.deleteVideo(video)
        }
    }

    fun deleteAllVideos() {
        viewModelScope.launch {
            repository.deleteAllVideos()
        }
    }

    fun resetVideoAddState() {
        _videoAddState.value = VideoAddState.Idle
    }

    fun resetPinError() {
        viewModelScope.launch {
            val storedPin = repository.getSetting(Constants.SETTING_PIN_CODE)
            if (storedPin == null) {
                _pinState.value = PinState.Creating
            } else {
                _pinState.value = PinState.Verifying
            }
        }
    }
}
