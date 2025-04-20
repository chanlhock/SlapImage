package com.example.slapimage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.slapimage.databinding.ItemChatBotMessageBinding
import com.example.slapimage.databinding.ItemChatUserMessageBinding
import android.text.method.LinkMovementMethod
import androidx.core.content.ContextCompat

class GeminiChatAdapter : ListAdapter<ChatMessage, RecyclerView.ViewHolder>(ChatDiffCallback()) {

    companion object {
        private const val VIEW_TYPE_USER = 1
        private const val VIEW_TYPE_BOT = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_USER -> {
                val binding = ItemChatUserMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                UserMessageViewHolder(binding)
            }
            else -> {
                val binding = ItemChatBotMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                BotMessageViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UserMessageViewHolder -> holder.bind(getItem(position))
            is BotMessageViewHolder -> holder.bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).isUser) VIEW_TYPE_USER else VIEW_TYPE_BOT
    }

    inner class UserMessageViewHolder(private val binding: ItemChatUserMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: ChatMessage) {
            binding.messageText.text = message.text
            binding.timestampText.text = formatTimestamp(message.timestamp)
        }
    }

    /*
    inner class BotMessageViewHolder(private val binding: ItemChatBotMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: ChatMessage) {
            binding.messageText.text = message.text
            binding.timestampText.text = formatTimestamp(message.timestamp)
        }
    }
*/
    /*
    inner class BotMessageViewHolder(private val binding: ItemChatBotMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: ChatMessage) {
            binding.messageText.apply {
                // Safely apply formatted text
                text = try {
                    message.formattedText ?: message.text
                } catch (e: Exception) {
                    // Fallback to plain text if span application fails
                    message.text
                }

                // Only enable links if we have formatted text
                movementMethod = if (message.formattedText != null) {
                    LinkMovementMethod.getInstance()
                } else {
                    null
                }
            }

            binding.timestampText.text = formatTimestamp(message.timestamp)
        }
    }
    */

    inner class BotMessageViewHolder(private val binding: ItemChatBotMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: ChatMessage) {
            binding.messageText.apply {
                // Enable paragraph spacing
                setLineSpacing(1.1f, 1.2f) // Adjust line spacing
                text = message.formattedText ?: message.text

                // Make links clickable if formatted
                movementMethod = if (message.formattedText != null) {
                    LinkMovementMethod.getInstance()
                } else {
                    null
                }
            }
            binding.timestampText.text = formatTimestamp(message.timestamp)
        }
    }

    private fun formatTimestamp(timestamp: Long): String {
        // Implement your timestamp formatting logic here
        return "Just now"
    }
}

class ChatDiffCallback : DiffUtil.ItemCallback<ChatMessage>() {
    override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
        return oldItem.timestamp == newItem.timestamp
    }

    override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
        return oldItem == newItem
    }
}