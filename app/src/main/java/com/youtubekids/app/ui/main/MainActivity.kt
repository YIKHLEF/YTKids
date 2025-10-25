package com.youtubekids.app.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.youtubekids.app.databinding.ActivityMainBinding
import com.youtubekids.app.ui.admin.AdminActivity
import com.youtubekids.app.ui.player.PlayerActivity
import com.youtubekids.app.utils.Constants
import com.youtubekids.app.utils.gone
import com.youtubekids.app.utils.visible
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var videoAdapter: VideoAdapter

    private var adminAccessStartTime = 0L
    private var isAdminAccessPressed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupAdminAccess()
        observeVideos()
        observeLoading()
    }

    private fun setupRecyclerView() {
        videoAdapter = VideoAdapter { video ->
            openVideoPlayer(video.videoId, video.title)
        }

        binding.videosRecyclerView.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = videoAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupAdminAccess() {
        // Hidden admin access: long press on top-right corner
        binding.adminAccessArea.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    adminAccessStartTime = System.currentTimeMillis()
                    isAdminAccessPressed = true

                    // Check after delay if still pressed
                    lifecycleScope.launch {
                        delay(Constants.ADMIN_LONG_PRESS_DURATION)
                        if (isAdminAccessPressed) {
                            openAdminPanel()
                            isAdminAccessPressed = false
                        }
                    }
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    isAdminAccessPressed = false
                }
            }
            true
        }
    }

    private fun observeVideos() {
        lifecycleScope.launch {
            viewModel.videos.collect { videos ->
                if (videos.isEmpty()) {
                    binding.videosRecyclerView.gone()
                    binding.emptyStateLayout.visible()
                } else {
                    binding.videosRecyclerView.visible()
                    binding.emptyStateLayout.gone()
                    videoAdapter.submitList(videos)
                }
            }
        }
    }

    private fun observeLoading() {
        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                if (isLoading) {
                    binding.loadingIndicator.visible()
                } else {
                    binding.loadingIndicator.gone()
                }
            }
        }
    }

    private fun openVideoPlayer(videoId: String, title: String) {
        val intent = Intent(this, PlayerActivity::class.java).apply {
            putExtra(Constants.EXTRA_VIDEO_ID, videoId)
            putExtra(Constants.EXTRA_VIDEO_TITLE, title)
        }
        startActivity(intent)
    }

    private fun openAdminPanel() {
        val intent = Intent(this, AdminActivity::class.java)
        startActivity(intent)
    }
}
