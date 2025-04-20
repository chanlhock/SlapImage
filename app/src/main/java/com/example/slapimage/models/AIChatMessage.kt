package com.example.slapimage.models

import java.util.*

data class AIChatMessage(
    val sender: String,
    val message: String,
    val isAI: Boolean,
    val timestamp: Date = Date(),
    val id: String = UUID.randomUUID().toString()
) {
    companion object {
        fun createUserMessage(message: String, sender: String = "You") =
            AIChatMessage(sender, message, false)

        fun createAIMessage(message: String, sender: String = "AI Assistant") =
            AIChatMessage(sender, message, true)
    }
}