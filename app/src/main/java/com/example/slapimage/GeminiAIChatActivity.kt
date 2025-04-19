package com.example.slapimage

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.slapimage.databinding.ActivityGeminiChatBinding
import com.google.android.material.snackbar.Snackbar
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

class GeminiAIChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGeminiChatBinding
    private val viewModel: GeminiChatViewModel by viewModels()
    private lateinit var chatAdapter: GeminiChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGeminiChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupListeners()
        setupObservers()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        chatAdapter = GeminiChatAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@GeminiAIChatActivity)
            adapter = chatAdapter
            setHasFixedSize(true)
            itemAnimator = null // We'll handle animations manually
        }
    }

    private fun setupListeners() {
        binding.sendButton.setOnClickListener {
            val inputText = binding.inputEditText.text.toString().trim()
            if (inputText.isNotEmpty()) {
                viewModel.sendMessage(inputText)
                binding.inputEditText.text?.clear()
                binding.recyclerView.smoothScrollToPosition(chatAdapter.itemCount - 1)
            }
        }

        binding.inputEditText.setOnEditorActionListener { _, _, _ ->
            binding.sendButton.performClick()
            true
        }
    }

    private fun setupObservers() {
        // For chat messages
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.chatMessages.collectLatest { messages ->
                    chatAdapter.submitList(messages.toMutableList()) {
                        // Scroll to bottom when new message arrives
                        if (messages.isNotEmpty()) {
                            binding.recyclerView.post {
                                binding.recyclerView.smoothScrollToPosition(messages.size - 1)
                            }
                        }
                    }
                }
            }
        }

        // For loading state
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collect { isLoading ->
                    binding.progressBar.isVisible = isLoading
                    binding.sendButton.isEnabled = !isLoading

                    val animation = if (isLoading) {
                        AnimationUtils.loadAnimation(this@GeminiAIChatActivity, R.anim.rotate_continuous)
                    } else {
                        null
                    }
                    binding.sendButton.animation = animation
                    animation?.start()
                }
            }
        }

        // For error messages
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorMessage.collect { error ->
                    error?.let {
                        Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                        viewModel.errorMessageShown()
                    }
                }
            }
        }
    }
}