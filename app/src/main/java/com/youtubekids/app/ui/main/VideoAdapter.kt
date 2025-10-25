package com.youtubekids.app.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.youtubekids.app.R
import com.youtubekids.app.data.local.entities.VideoEntity
import com.youtubekids.app.databinding.ItemVideoGridBinding
import com.youtubekids.app.utils.formatDuration

class VideoAdapter(
    private val onVideoClick: (VideoEntity) -> Unit
) : ListAdapter<VideoEntity, VideoAdapter.VideoViewHolder>(VideoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVideoGridBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VideoViewHolder(binding, onVideoClick)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class VideoViewHolder(
        private val binding: ItemVideoGridBinding,
        private val onVideoClick: (VideoEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(video: VideoEntity) {
            binding.videoTitle.text = video.title
            binding.videoDuration.text = video.duration.formatDuration()

            // Load thumbnail with Glide
            Glide.with(binding.root.context)
                .load(video.thumbnailUrl)
                .placeholder(R.color.background_light)
                .error(R.color.error)
                .into(binding.videoThumbnail)

            // Set click listener
            binding.root.setOnClickListener {
                onVideoClick(video)
            }
        }
    }

    class VideoDiffCallback : DiffUtil.ItemCallback<VideoEntity>() {
        override fun areItemsTheSame(oldItem: VideoEntity, newItem: VideoEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: VideoEntity, newItem: VideoEntity): Boolean {
            return oldItem == newItem
        }
    }
}
