package com.youtubekids.app.ui.admin

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.youtubekids.app.R
import com.youtubekids.app.databinding.ActivityAdminBinding
import com.youtubekids.app.utils.gone
import com.youtubekids.app.utils.showToast
import com.youtubekids.app.utils.visible
import kotlinx.coroutines.launch

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private val viewModel: AdminViewModel by viewModels()
    private lateinit var adminVideoAdapter: AdminVideoAdapter

    private var pinDialog: PinDialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        observePinState()
        observeVideos()
        setupFab()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        adminVideoAdapter = AdminVideoAdapter { video ->
            showDeleteConfirmation(video)
        }

        binding.adminVideosRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@AdminActivity)
            adapter = adminVideoAdapter
        }
    }

    private fun setupFab() {
        binding.fabAddVideo.setOnClickListener {
            showAddVideoDialog()
        }
    }

    private fun observePinState() {
        lifecycleScope.launch {
            viewModel.pinState.collect { state ->
                when (state) {
                    is PinState.Creating -> {
                        showPinDialog(PinDialogFragment.PinMode.CREATE)
                    }
                    is PinState.Verifying -> {
                        showPinDialog(PinDialogFragment.PinMode.VERIFY)
                    }
                    is PinState.Verified -> {
                        pinDialog?.dismiss()
                        pinDialog = null
                    }
                    is PinState.Error -> {
                        pinDialog?.showPinError(state.message)
                        viewModel.resetPinError()
                    }
                    is PinState.Idle -> {
                        // Do nothing
                    }
                }
            }
        }
    }

    private fun observeVideos() {
        lifecycleScope.launch {
            viewModel.videos.collect { videos ->
                if (videos.isEmpty()) {
                    binding.emptyStateText.visible()
                    binding.adminVideosRecyclerView.gone()
                } else {
                    binding.emptyStateText.gone()
                    binding.adminVideosRecyclerView.visible()
                    adminVideoAdapter.submitList(videos)
                }
            }
        }
    }

    private fun showPinDialog(mode: PinDialogFragment.PinMode) {
        if (pinDialog != null) return

        pinDialog = PinDialogFragment(mode) { pin ->
            when (mode) {
                PinDialogFragment.PinMode.CREATE -> viewModel.createPin(pin)
                PinDialogFragment.PinMode.VERIFY -> viewModel.verifyPin(pin)
            }
        }

        pinDialog?.isCancelable = false
        pinDialog?.show(supportFragmentManager, "PinDialog")
    }

    private fun showAddVideoDialog() {
        val dialog = AddVideoDialogFragment()
        dialog.show(supportFragmentManager, "AddVideoDialog")
    }

    private fun showDeleteConfirmation(video: com.youtubekids.app.data.local.entities.VideoEntity) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.delete_video))
            .setMessage("Supprimer \"${video.title}\" ?")
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.deleteVideo(video)
                showToast(getString(R.string.video_deleted))
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }

    override fun onBackPressed() {
        // Prevent back navigation if PIN not verified
        lifecycleScope.launch {
            val state = viewModel.pinState.value
            if (state is PinState.Verified) {
                super.onBackPressed()
            }
        }
    }
}
