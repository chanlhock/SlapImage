package com.example.slapimage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GeminiChatViewModel : ViewModel() {
    // Replace with your actual API key (store securely in production)
    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.0-flash",
        apiKey = "AIzaSyBghu7r7ZYlemjYn2APxGxiBQufhj8eBsM"
    )

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(listOf(
        ChatMessage(
            text = "Hello! I'm Gemini. How can I help you today?",
            isUser = false,
            timestamp = System.currentTimeMillis()
        )
    ))
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun sendMessage(message: String) {
        _isLoading.value = true

        // Add user message immediately
        _chatMessages.update { messages ->
            messages + ChatMessage(
                text = message,
                isUser = true,
                timestamp = System.currentTimeMillis()
            )
        }

        viewModelScope.launch {
            try {
                val response = generativeModel.generateContent(message)

                _chatMessages.update { messages ->
                    messages + ChatMessage(
                        text = response.text ?: "I couldn't understand that. Please try again.",
                        isUser = false,
                        timestamp = System.currentTimeMillis()
                    )
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun errorMessageShown() {
        _errorMessage.value = null
    }
}

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long
)