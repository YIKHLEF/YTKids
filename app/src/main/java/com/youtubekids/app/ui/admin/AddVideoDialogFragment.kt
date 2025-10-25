package com.youtubekids.app.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.youtubekids.app.R
import com.youtubekids.app.databinding.DialogAddVideoBinding
import com.youtubekids.app.utils.*
import kotlinx.coroutines.launch

class AddVideoDialogFragment : DialogFragment() {

    private var _binding: DialogAddVideoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AdminViewModel by viewModels({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAddVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        observeVideoAddState()
    }

    private fun setupListeners() {
        binding.urlEditText.addTextChangedListener {
            binding.errorText.gone()
        }

        binding.addButton.setOnClickListener {
            val url = binding.urlEditText.text?.toString()?.trim() ?: ""

            when {
                url.isYouTubePlaylistUrl() -> {
                    val playlistId = url.extractYouTubePlaylistId()
                    if (playlistId != null) {
                        viewModel.fetchPlaylistVideos(playlistId)
                    } else {
                        showError(getString(R.string.invalid_url))
                    }
                }
                url.isYouTubeVideoUrl() -> {
                    val videoId = url.extractYouTubeVideoId()
                    if (videoId != null) {
                        viewModel.fetchVideoDetails(videoId)
                    } else {
                        showError(getString(R.string.invalid_url))
                    }
                }
                else -> {
                    showError(getString(R.string.invalid_url))
                }
            }
        }

        binding.cancelAddButton.setOnClickListener {
            dismiss()
        }
    }

    private fun observeVideoAddState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.videoAddState.collect { state ->
                when (state) {
                    is VideoAddState.Idle -> {
                        hideLoading()
                        binding.previewSection.gone()
                    }
                    is VideoAddState.Loading -> {
                        showLoading()
                        binding.errorText.gone()
                        binding.previewSection.gone()
                    }
                    is VideoAddState.Preview -> {
                        hideLoading()
                        showPreview(state.video.title, state.video.thumbnailUrl, state.video.duration.formatDuration())

                        // Automatically add the video
                        viewModel.addVideo(state.video)
                    }
                    is VideoAddState.PlaylistPreview -> {
                        hideLoading()

                        if (state.videos.isNotEmpty()) {
                            val firstVideo = state.videos[0]
                            showPreview(
                                getString(R.string.playlist_detected, state.videos.size),
                                firstVideo.thumbnailUrl,
                                "${state.videos.size} vidéos"
                            )

                            // Automatically add all videos
                            viewModel.addVideos(state.videos)
                        }
                    }
                    is VideoAddState.Success -> {
                        hideLoading()
                        context?.showToast(
                            if (state.count == 1) {
                                getString(R.string.video_added)
                            } else {
                                getString(R.string.playlist_added, state.count)
                            }
                        )
                        viewModel.resetVideoAddState()
                        dismiss()
                    }
                    is VideoAddState.Error -> {
                        hideLoading()
                        showError(state.message)
                    }
                }
            }
        }
    }

    private fun showLoading() {
        binding.loadingProgress.visible()
        binding.addButton.isEnabled = false
    }

    private fun hideLoading() {
        binding.loadingProgress.gone()
        binding.addButton.isEnabled = true
    }

    private fun showPreview(title: String, thumbnailUrl: String, duration: String) {
        binding.previewSection.visible()
        binding.previewTitle.text = title
        binding.previewDuration.text = duration

        Glide.with(requireContext())
            .load(thumbnailUrl)
            .placeholder(R.color.background_light)
            .error(R.color.error)
            .into(binding.previewThumbnail)
    }

    private fun showError(message: String) {
        binding.errorText.text = message
        binding.errorText.visible()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}
