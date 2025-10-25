package com.youtubekids.app.data.remote.models

import com.google.gson.annotations.SerializedName

// Video Response Models
data class VideoResponse(
    @SerializedName("items")
    val items: List<VideoItem>?
)

data class VideoItem(
    @SerializedName("id")
    val id: String,
    @SerializedName("snippet")
    val snippet: VideoSnippet,
    @SerializedName("contentDetails")
    val contentDetails: ContentDetails
)

data class VideoSnippet(
    @SerializedName("title")
    val title: String,
    @SerializedName("thumbnails")
    val thumbnails: Thumbnails
)

data class Thumbnails(
    @SerializedName("default")
    val default: Thumbnail?,
    @SerializedName("medium")
    val medium: Thumbnail?,
    @SerializedName("high")
    val high: Thumbnail?,
    @SerializedName("standard")
    val standard: Thumbnail?,
    @SerializedName("maxres")
    val maxres: Thumbnail?
)

data class Thumbnail(
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: Int?,
    @SerializedName("height")
    val height: Int?
)

data class ContentDetails(
    @SerializedName("duration")
    val duration: String // ISO 8601 format (e.g., "PT4M13S")
)

// Playlist Response Models
data class PlaylistResponse(
    @SerializedName("items")
    val items: List<PlaylistItem>?,
    @SerializedName("nextPageToken")
    val nextPageToken: String?
)

data class PlaylistItem(
    @SerializedName("snippet")
    val snippet: PlaylistSnippet,
    @SerializedName("contentDetails")
    val contentDetails: PlaylistContentDetails
)

data class PlaylistSnippet(
    @SerializedName("title")
    val title: String,
    @SerializedName("thumbnails")
    val thumbnails: Thumbnails,
    @SerializedName("resourceId")
    val resourceId: ResourceId
)

data class ResourceId(
    @SerializedName("videoId")
    val videoId: String
)

data class PlaylistContentDetails(
    @SerializedName("videoId")
    val videoId: String
)
