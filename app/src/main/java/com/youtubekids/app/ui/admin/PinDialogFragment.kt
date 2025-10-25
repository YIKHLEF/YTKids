package com.youtubekids.app.ui.admin

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.youtubekids.app.R
import com.youtubekids.app.databinding.DialogPinBinding
import com.youtubekids.app.utils.gone
import com.youtubekids.app.utils.visible

class PinDialogFragment(
    private val mode: PinMode,
    private val onPinConfirmed: (String) -> Unit
) : DialogFragment() {

    private var _binding: DialogPinBinding? = null
    private val binding get() = _binding!!

    private var firstPin: String? = null

    enum class PinMode {
        CREATE,
        VERIFY
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogPinBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupListeners()
    }

    private fun setupUI() {
        binding.pinDialogTitle.text = when (mode) {
            PinMode.CREATE -> getString(R.string.create_pin)
            PinMode.VERIFY -> getString(R.string.enter_pin)
        }

        binding.pinInputLayout.hint = when (mode) {
            PinMode.CREATE -> getString(R.string.create_pin)
            PinMode.VERIFY -> getString(R.string.enter_pin)
        }
    }

    private fun setupListeners() {
        binding.pinEditText.addTextChangedListener {
            binding.pinErrorText.gone()
        }

        binding.confirmButton.setOnClickListener {
            val pin = binding.pinEditText.text?.toString() ?: ""

            if (pin.length != 4) {
                showError("Le code PIN doit contenir 4 chiffres")
                return@setOnClickListener
            }

            when (mode) {
                PinMode.CREATE -> handleCreatePin(pin)
                PinMode.VERIFY -> handleVerifyPin(pin)
            }
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }

    private fun handleCreatePin(pin: String) {
        if (firstPin == null) {
            // First entry
            firstPin = pin
            binding.pinDialogTitle.text = getString(R.string.confirm_pin)
            binding.pinEditText.text?.clear()
        } else {
            // Confirmation
            if (pin == firstPin) {
                onPinConfirmed(pin)
                dismiss()
            } else {
                showError(getString(R.string.pin_mismatch))
                firstPin = null
                binding.pinDialogTitle.text = getString(R.string.create_pin)
                binding.pinEditText.text?.clear()
            }
        }
    }

    private fun handleVerifyPin(pin: String) {
        onPinConfirmed(pin)
    }

    private fun showError(message: String) {
        binding.pinErrorText.text = message
        binding.pinErrorText.visible()
    }

    fun showPinError(message: String) {
        showError(message)
        binding.pinEditText.text?.clear()
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
