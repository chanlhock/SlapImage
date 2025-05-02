package com.example.slapimage.fragments

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.slapimage.R
import com.example.slapimage.adapters.ChatAdapter
import com.example.slapimage.databinding.FragmentChatbotBinding
import com.example.slapimage.models.AIChatMessage
import com.example.slapimage.viewmodels.AIChatViewModel
import io.noties.markwon.Markwon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import android.content.Context
import androidx.core.content.ContextCompat
import android.provider.OpenableColumns

class ChatBotFragment : Fragment() {
    private var _binding: FragmentChatbotBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AIChatViewModel by viewModels()
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var markwon: Markwon

    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                lifecycleScope.launch {
                    try {
                        // First ensure vocab file exists
                        prepareVocabFile(requireContext())
                        // Then load the model
                        loadModelFromUri(uri)
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatbotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        markwon = Markwon.create(requireContext())
        setupChatRecyclerView()
        setupUIListeners()
        setupObservers()

        // Create the callback
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitConfirmationDialog()
                // Navigate back to HomeFragment
                //parentFragmentManager.popBackStack()
            }
        }

        // Register the callback with the correct dispatcher
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
    }

    private fun setupChatRecyclerView() {
        chatAdapter = ChatAdapter(markwon)
        binding.rvChatMessages.apply {
            layoutManager = LinearLayoutManager(requireContext()).apply {
                stackFromEnd = true
            }
            adapter = chatAdapter
        }
    }

    private fun setupUIListeners() {
        binding.btnSelectModel.setOnClickListener { openFilePicker() }
        binding.btnSend.setOnClickListener { sendMessage() }
        binding.btnSend.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.dark_blue))
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.chatMessages.collect { messages ->
                    chatAdapter.submitList(messages)
                    if(messages.isNotEmpty()) {
                        binding.rvChatMessages.smoothScrollToPosition(messages.size - 1)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isProcessing.collect { isProcessing ->
                    binding.progressBar.visibility = if (isProcessing) View.VISIBLE else View.GONE
                    binding.btnSend.isEnabled = !isProcessing && viewModel.isModelLoaded
                }
            }
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("application/octet-stream", "model/onnx"))
        }
        filePickerLauncher.launch(intent)
    }

    private fun sendMessage() {
        val userInput = binding.etUserInput.text.toString().trim()
        if (userInput.isEmpty()) return

        viewModel.addChatMessage(AIChatMessage(getString(R.string.you), userInput, false))
        binding.etUserInput.text.clear()

        lifecycleScope.launch {
            viewModel.setProcessing(true)
            try {
                val response = withContext(Dispatchers.IO) {
                    viewModel.getAIResponse(userInput)
                }
                viewModel.addChatMessage(
                    AIChatMessage(
                        getString(R.string.ai),
                        formatAIResponse(response),
                        true
                    )
                )
            } catch (e: Exception) {
                viewModel.addChatMessage(
                    AIChatMessage(
                        getString(R.string.ai),
                        "Error (AI Response): ${e.message}",
                        true
                    )
                )
            } finally {
                viewModel.setProcessing(false)
            }
        }
    }

    private fun formatAIResponse(response: String): String {
        return response.split("\n")
            .filter { it.isNotBlank() }
            .joinToString("\n\n") {
                when {
                    it.startsWith("# ") -> "**${it.substring(2)}**"
                    it.startsWith("## ") -> "*${it.substring(3)}*"
                    it.startsWith("- ") -> "• ${it.substring(2)}"
                    else -> it
                }
            }
    }

    private fun showExitConfirmationDialog() {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.exit_chat_title)
            .setMessage(R.string.exit_chat_message)
            .setPositiveButton(R.string.exit) { _, _ -> parentFragmentManager.popBackStack() }
            .setNegativeButton(R.string.cancel, null)
            .show()
        // Access and set text color for the buttons
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(
            ContextCompat.getColor(requireContext(), R.color.dark_blue)
        )
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(
            ContextCompat.getColor(requireContext(), R.color.dark_blue)
        )
    }

    private suspend fun prepareVocabFile(context: Context): File = withContext(Dispatchers.IO) {
        val vocabFile = File(context.cacheDir, "vocab.txt")
        if (!vocabFile.exists()) {
            try {
                context.assets.open("vocab.txt").use { input ->
                    FileOutputStream(vocabFile).use { output ->
                        input.copyTo(output)
                    }
                }
            } catch (e: Exception) {
                throw IllegalStateException("Failed to prepare vocabulary file", e)
            }
        }
        return@withContext vocabFile
    }


    private fun loadModelFromUri(uri: Uri) {
        lifecycleScope.launch {
            try {
                binding.tvModelStatus.text = getString(R.string.processing)
                binding.btnSend.isEnabled = false

                // Get the original filename from the URI
                val originalFileName = getFileNameFromUri(uri)
                val cacheFileName = if (originalFileName != null) {
                    "cache_$originalFileName.onnx"  // Append original filename
                } else {
                    "temp_model.onnx"  // Fallback if filename can't be retrieved
                }

                // Copy the model file to cache
                val inputStream = requireContext().contentResolver.openInputStream(uri)
                val modelFile = File(requireContext().cacheDir, cacheFileName).apply {
                    inputStream?.use { it.copyTo(outputStream()) }
                }
                // Load model with context
                viewModel.loadModel(requireContext(), modelFile.absolutePath)

                binding.tvModelStatus.text = getString(R.string.model_loaded, modelFile.name)
                binding.btnSend.isEnabled = true
                Toast.makeText(requireContext(), "Model loading. Wait for system completion message.", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                binding.tvModelStatus.text = getString(R.string.model_not_loaded)
                binding.btnSend.isEnabled = false
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Helper function to get filename from URI
    private fun getFileNameFromUri(uri: Uri): String? {
        return when (uri.scheme) {
            "content" -> {
                requireContext().contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIndex >= 0 && cursor.moveToFirst()) {
                        cursor.getString(nameIndex)
                    } else {
                        null
                    }
                }
            }
            "file" -> uri.lastPathSegment
            else -> null
        }?.substringBeforeLast(".onnx")?.substringBeforeLast(".") // Remove extension if present
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}