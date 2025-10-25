package com.youtubekids.app.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.youtubekids.app.R
import com.youtubekids.app.data.local.entities.VideoEntity
import com.youtubekids.app.databinding.ItemAdminVideoBinding
import com.youtubekids.app.utils.formatDuration

class AdminVideoAdapter(
    private val onDeleteClick: (VideoEntity) -> Unit
) : ListAdapter<VideoEntity, AdminVideoAdapter.AdminVideoViewHolder>(VideoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminVideoViewHolder {
        val binding = ItemAdminVideoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AdminVideoViewHolder(binding, onDeleteClick)
    }

    override fun onBindViewHolder(holder: AdminVideoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AdminVideoViewHolder(
        private val binding: ItemAdminVideoBinding,
        private val onDeleteClick: (VideoEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(video: VideoEntity) {
            binding.adminVideoTitle.text = video.title
            binding.adminVideoDuration.text = video.duration.formatDuration()

            // Load thumbnail
            Glide.with(binding.root.context)
                .load(video.thumbnailUrl)
                .placeholder(R.color.background_light)
                .error(R.color.error)
                .into(binding.adminVideoThumbnail)

            // Delete button
            binding.deleteButton.setOnClickListener {
                onDeleteClick(video)
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
