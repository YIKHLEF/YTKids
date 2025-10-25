package com.youtubekids.app.data.repository

import com.youtubekids.app.BuildConfig
import com.youtubekids.app.data.local.dao.SettingsDao
import com.youtubekids.app.data.local.dao.VideoDao
import com.youtubekids.app.data.local.entities.SettingEntity
import com.youtubekids.app.data.local.entities.VideoEntity
import com.youtubekids.app.data.remote.YouTubeApiService
import com.youtubekids.app.utils.Constants
import com.youtubekids.app.utils.parseDurationToSeconds
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class VideoRepository(
    private val videoDao: VideoDao,
    private val settingsDao: SettingsDao,
    private val youTubeApiService: YouTubeApiService
) {

    // Video operations
    fun getAllVideos(): Flow<List<VideoEntity>> = videoDao.getAllVideos()

    suspend fun getVideoById(id: Long): VideoEntity? = videoDao.getVideoById(id)

    suspend fun insertVideo(video: VideoEntity): Long = videoDao.insertVideo(video)

    suspend fun insertVideos(videos: List<VideoEntity>): List<Long> = videoDao.insertVideos(videos)

    suspend fun deleteVideo(video: VideoEntity) = videoDao.deleteVideo(video)

    suspend fun deleteAllVideos() = videoDao.deleteAllVideos()

    suspend fun updateVideoPositions(videos: List<VideoEntity>) = videoDao.updateVideoPositions(videos)

    suspend fun getNextPosition(): Int {
        val maxPosition = videoDao.getMaxPosition()
        return (maxPosition ?: -1) + 1
    }

    // Settings operations
    suspend fun getSetting(key: String): String? {
        return settingsDao.getSetting(key)?.value
    }

    fun getSettingFlow(key: String): Flow<SettingEntity?> {
        return settingsDao.getSettingFlow(key)
    }

    suspend fun saveSetting(key: String, value: String) {
        settingsDao.insertSetting(SettingEntity(key, value))
    }

    suspend fun deleteSetting(key: String) {
        settingsDao.deleteSetting(key)
    }

    suspend fun deleteAllSettings() {
        settingsDao.deleteAllSettings()
    }

    // YouTube API operations
    suspend fun fetchVideoDetails(videoId: String): Result<VideoEntity> {
        return try {
            val response = youTubeApiService.getVideoDetails(
                videoId = videoId,
                apiKey = BuildConfig.YOUTUBE_API_KEY
            )

            if (response.isSuccessful && response.body()?.items?.isNotEmpty() == true) {
                val videoItem = response.body()!!.items!![0]
                val snippet = videoItem.snippet
                val contentDetails = videoItem.contentDetails

                // Get the best quality thumbnail
                val thumbnailUrl = snippet.thumbnails.maxres?.url
                    ?: snippet.thumbnails.standard?.url
                    ?: snippet.thumbnails.high?.url
                    ?: snippet.thumbnails.medium?.url
                    ?: snippet.thumbnails.default?.url
                    ?: ""

                val duration = contentDetails.duration.parseDurationToSeconds()
                val nextPosition = getNextPosition()

                val videoEntity = VideoEntity(
                    videoId = videoId,
                    title = snippet.title,
                    thumbnailUrl = thumbnailUrl,
                    duration = duration,
                    position = nextPosition,
                    dateAdded = System.currentTimeMillis(),
                    sourceType = Constants.SOURCE_TYPE_SINGLE
                )

                Result.success(videoEntity)
            } else {
                Result.failure(Exception("Video not found or unavailable"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchPlaylistVideos(playlistId: String): Result<List<VideoEntity>> {
        return try {
            val videos = mutableListOf<VideoEntity>()
            var nextPageToken: String? = null
            var currentPosition = getNextPosition()

            do {
                val response = youTubeApiService.getPlaylistItems(
                    playlistId = playlistId,
                    pageToken = nextPageToken,
                    apiKey = BuildConfig.YOUTUBE_API_KEY
                )

                if (response.isSuccessful && response.body()?.items?.isNotEmpty() == true) {
                    val playlistItems = response.body()!!.items!!

                    for (item in playlistItems) {
                        val videoId = item.contentDetails.videoId

                        // Fetch details for each video to get duration
                        val videoDetailsResult = fetchVideoDetailsForPlaylist(videoId, playlistId, currentPosition)
                        if (videoDetailsResult.isSuccess) {
                            videos.add(videoDetailsResult.getOrNull()!!)
                            currentPosition++
                        }
                    }

                    nextPageToken = response.body()!!.nextPageToken
                } else {
                    break
                }
            } while (nextPageToken != null)

            if (videos.isNotEmpty()) {
                Result.success(videos)
            } else {
                Result.failure(Exception("Playlist is empty or unavailable"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun fetchVideoDetailsForPlaylist(
        videoId: String,
        playlistId: String,
        position: Int
    ): Result<VideoEntity> {
        return try {
            val response = youTubeApiService.getVideoDetails(
                videoId = videoId,
                apiKey = BuildConfig.YOUTUBE_API_KEY
            )

            if (response.isSuccessful && response.body()?.items?.isNotEmpty() == true) {
                val videoItem = response.body()!!.items!![0]
                val snippet = videoItem.snippet
                val contentDetails = videoItem.contentDetails

                val thumbnailUrl = snippet.thumbnails.maxres?.url
                    ?: snippet.thumbnails.standard?.url
                    ?: snippet.thumbnails.high?.url
                    ?: snippet.thumbnails.medium?.url
                    ?: snippet.thumbnails.default?.url
                    ?: ""

                val duration = contentDetails.duration.parseDurationToSeconds()

                val videoEntity = VideoEntity(
                    videoId = videoId,
                    title = snippet.title,
                    thumbnailUrl = thumbnailUrl,
                    duration = duration,
                    position = position,
                    dateAdded = System.currentTimeMillis(),
                    sourceType = Constants.SOURCE_TYPE_PLAYLIST,
                    playlistId = playlistId
                )

                Result.success(videoEntity)
            } else {
                Result.failure(Exception("Video not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // First launch check
    suspend fun isFirstLaunch(): Boolean {
        val firstLaunch = getSetting(Constants.SETTING_FIRST_LAUNCH)
        return firstLaunch == null || firstLaunch == "true"
    }

    suspend fun setFirstLaunchComplete() {
        saveSetting(Constants.SETTING_FIRST_LAUNCH, "false")
    }
}
