package com.youtubekids.app.utils

object Constants {
    // YouTube API
    const val YOUTUBE_BASE_URL = "https://www.googleapis.com/youtube/v3/"

    // Settings Keys
    const val SETTING_PIN_CODE = "pin_code"
    const val SETTING_DISPLAY_MODE = "display_mode"
    const val SETTING_THEME = "theme"
    const val SETTING_FIRST_LAUNCH = "first_launch"

    // Display Modes
    const val DISPLAY_MODE_GRID_2X2 = "grid_2x2"
    const val DISPLAY_MODE_GRID_3X3 = "grid_3x3"
    const val DISPLAY_MODE_LIST = "list"

    // Video Source Types
    const val SOURCE_TYPE_SINGLE = "single"
    const val SOURCE_TYPE_PLAYLIST = "playlist"

    // Intent Extras
    const val EXTRA_VIDEO_ID = "extra_video_id"
    const val EXTRA_VIDEO_TITLE = "extra_video_title"

    // Default PIN (for first launch)
    const val DEFAULT_PIN = "0000"

    // Admin Access
    const val ADMIN_LONG_PRESS_DURATION = 3000L // 3 seconds

    // Pin Attempt Limits
    const val MAX_PIN_ATTEMPTS = 3
    const val PIN_LOCKOUT_DURATION = 30000L // 30 seconds
}
