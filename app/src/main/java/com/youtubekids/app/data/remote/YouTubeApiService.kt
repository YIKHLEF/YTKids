package com.youtubekids.app.data.remote

import com.youtubekids.app.data.remote.models.PlaylistResponse
import com.youtubekids.app.data.remote.models.VideoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeApiService {

    @GET("videos")
    suspend fun getVideoDetails(
        @Query("part") part: String = "snippet,contentDetails",
        @Query("id") videoId: String,
        @Query("key") apiKey: String
    ): Response<VideoResponse>

    @GET("playlistItems")
    suspend fun getPlaylistItems(
        @Query("part") part: String = "snippet,contentDetails",
        @Query("playlistId") playlistId: String,
        @Query("maxResults") maxResults: Int = 50,
        @Query("pageToken") pageToken: String? = null,
        @Query("key") apiKey: String
    ): Response<PlaylistResponse>
}
