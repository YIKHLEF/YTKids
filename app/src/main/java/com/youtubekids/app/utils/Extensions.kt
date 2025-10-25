package com.youtubekids.app.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import java.util.regex.Pattern

/**
 * Shows a toast message
 */
fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

/**
 * Shows a long toast message
 */
fun Context.showLongToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

/**
 * Sets view visibility to VISIBLE
 */
fun View.visible() {
    visibility = View.VISIBLE
}

/**
 * Sets view visibility to GONE
 */
fun View.gone() {
    visibility = View.GONE
}

/**
 * Sets view visibility to INVISIBLE
 */
fun View.invisible() {
    visibility = View.INVISIBLE
}

/**
 * Extracts YouTube video ID from various URL formats
 */
fun String.extractYouTubeVideoId(): String? {
    val patterns = listOf(
        "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*",
        "^[a-zA-Z0-9_-]{11}$"
    )

    for (pattern in patterns) {
        val compiledPattern = Pattern.compile(pattern)
        val matcher = compiledPattern.matcher(this)
        if (matcher.find()) {
            return matcher.group()
        }
    }
    return null
}

/**
 * Extracts YouTube playlist ID from URL
 */
fun String.extractYouTubePlaylistId(): String? {
    val pattern = Pattern.compile("(?:list=)([a-zA-Z0-9_-]+)")
    val matcher = pattern.matcher(this)
    return if (matcher.find()) {
        matcher.group(1)
    } else {
        null
    }
}

/**
 * Checks if the string is a YouTube video URL
 */
fun String.isYouTubeVideoUrl(): Boolean {
    return this.extractYouTubeVideoId() != null
}

/**
 * Checks if the string is a YouTube playlist URL
 */
fun String.isYouTubePlaylistUrl(): Boolean {
    return this.contains("list=") && this.extractYouTubePlaylistId() != null
}

/**
 * Converts ISO 8601 duration to seconds
 * Format: PT#H#M#S (e.g., PT4M13S = 4 minutes 13 seconds)
 */
fun String.parseDurationToSeconds(): Int {
    var totalSeconds = 0
    val pattern = Pattern.compile("PT(?:(\\d+)H)?(?:(\\d+)M)?(?:(\\d+)S)?")
    val matcher = pattern.matcher(this)

    if (matcher.matches()) {
        val hours = matcher.group(1)?.toIntOrNull() ?: 0
        val minutes = matcher.group(2)?.toIntOrNull() ?: 0
        val seconds = matcher.group(3)?.toIntOrNull() ?: 0

        totalSeconds = hours * 3600 + minutes * 60 + seconds
    }

    return totalSeconds
}

/**
 * Formats seconds to readable duration (e.g., 4:13)
 */
fun Int.formatDuration(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val seconds = this % 60

    return if (hours > 0) {
        String.format("%d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%d:%02d", minutes, seconds)
    }
}
