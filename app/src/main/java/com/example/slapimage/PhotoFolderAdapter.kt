package com.example.slapimage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class PhotoFolderAdapter(
    private val context: Context,
    private var folders: List<PhotoFolder>,
    private val onFolderClick: (PhotoFolder) -> Unit
) : RecyclerView.Adapter<PhotoFolderAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.folderThumbnail)
        val folderName: TextView = view.findViewById(R.id.folderName)
        val photoCount: TextView = view.findViewById(R.id.photoCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_photo_folder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val folder = folders[position]
        val requestOptions = RequestOptions().centerCrop()

        // Clear previous image to prevent recycling issues
        Glide.with(context).clear(holder.imageView)

        // Load thumbnail based on folder content
        when {
            folder.photoCount == 0 -> {
                holder.imageView.setImageResource(R.drawable.ic_folder)
            }
            folder.isSinglePhoto || folder.photoCount == 1 -> {
                Glide.with(context)
                    .load(folder.thumbnailPath)
                    .apply(requestOptions)
                    .placeholder(R.drawable.ic_folder) // Show folder icon while
                    .error(R.drawable.ic_folder) // Show folder icon if error occurs
                    .into(holder.imageView)
            }
            else -> {
                Glide.with(context)
                    .load(folder.thumbnailPath)
                    .apply(requestOptions)
                    .into(holder.imageView)
            }
        }

        holder.folderName.text = folder.name
        holder.photoCount.text = when {
            folder.photoCount == 0 -> "Empty"
            folder.photoCount == 1 -> "1 photo"
            else -> "${folder.photoCount} photos"
        }

        holder.itemView.setOnClickListener {
            onFolderClick(folder)
        }
    }

    override fun getItemCount() = folders.size

    fun updateData(newFolders: List<PhotoFolder>) {
        folders = newFolders
        notifyDataSetChanged()
    }
}