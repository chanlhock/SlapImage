package com.example.slapimage.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.slapimage.R
import com.example.slapimage.models.AIChatMessage // Add this import
import io.noties.markwon.Markwon

class ChatAdapter(
    private val markwon: Markwon
) : ListAdapter<AIChatMessage, ChatAdapter.ChatViewHolder>(DiffCallback()) {

    inner class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvSender: TextView = view.findViewById(R.id.tvSender)
        private val tvMessage: TextView = view.findViewById(R.id.tvMessage)

        fun bind(message: AIChatMessage) {
            tvSender.text = message.sender
            markwon.setMarkdown(tvMessage, message.message)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_message, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class DiffCallback : DiffUtil.ItemCallback<AIChatMessage>() {
        override fun areItemsTheSame(oldItem: AIChatMessage, newItem: AIChatMessage): Boolean {
            return oldItem.id == newItem.id // Compare by unique ID
        }

        override fun areContentsTheSame(oldItem: AIChatMessage, newItem: AIChatMessage): Boolean {
            return oldItem == newItem
        }
    }
}