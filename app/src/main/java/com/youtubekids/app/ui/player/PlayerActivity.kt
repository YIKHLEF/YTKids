package com.youtubekids.app.ui.player

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.youtubekids.app.databinding.ActivityPlayerBinding
import com.youtubekids.app.utils.Constants
import com.youtubekids.app.utils.gone
import com.youtubekids.app.utils.visible

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private var videoId: String? = null
    private var videoTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Force landscape orientation for better viewing experience
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        // Hide system UI for fullscreen experience
        hideSystemUI()

        // Get video data from intent
        videoId = intent.getStringExtra(Constants.EXTRA_VIDEO_ID)
        videoTitle = intent.getStringExtra(Constants.EXTRA_VIDEO_TITLE)

        setupPlayer()
        setupBackButton()
    }

    private fun setupPlayer() {
        if (videoId == null) {
            showError()
            return
        }

        lifecycle.addObserver(binding.youtubePlayerView)

        // Configure player options to disable related videos and controls
        val iFramePlayerOptions = IFramePlayerOptions.Builder()
            .controls(1) // Show player controls
            .rel(0) // Disable related videos
            .ivLoadPolicy(3) // Disable video annotations
            .ccLoadPolicy(0) // Disable closed captions by default
            .build()

        binding.youtubePlayerView.enableAutomaticInitialization = false

        binding.youtubePlayerView.initialize(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                videoId?.let { id ->
                    youTubePlayer.loadVideo(id, 0f)
                }
            }

            override fun onError(youTubePlayer: YouTubePlayer, error: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants.PlayerError) {
                super.onError(youTubePlayer, error)
                showError()
            }
        }, iFramePlayerOptions)
    }

    private fun setupBackButton() {
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun showError() {
        binding.youtubePlayerView.gone()
        binding.errorMessage.visible()
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.youtubePlayerView.release()
        // Reset orientation
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}
