package com.youtubekids.app.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "videos",
    indices = [Index(value = ["video_id"], unique = true)]
)
data class VideoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "video_id")
    val videoId: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "thumbnail_url")
    val thumbnailUrl: String,

    @ColumnInfo(name = "duration")
    val duration: Int, // Duration in seconds

    @ColumnInfo(name = "position")
    val position: Int, // Display order

    @ColumnInfo(name = "date_added")
    val dateAdded: Long, // Timestamp

    @ColumnInfo(name = "source_type")
    val sourceType: String, // "single" or "playlist"

    @ColumnInfo(name = "playlist_id")
    val playlistId: String? = null
)
