package com.example.slapimage.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.slapimage.R
import java.io.File

class PhotoAdapter(
    private val context: Context,
    private var photos: List<String>,
    private val onPhotoClick: (String) -> Unit
) : RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.photoThumbnail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_photo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val path = photos[position]
        val file = File(path)

        if (file.isDirectory) {
            // Show folder icon
            Glide.with(context)
                .load(R.drawable.ic_folder)
                .into(holder.imageView)
        } else {
            // Show image thumbnail - modern approach
            Glide.with(context)
                .load(path)
                .thumbnail(
                    Glide.with(context)
                        .load(path)
                        .override(100) // Small thumbnail size
                )
                .into(holder.imageView)
        }

        holder.itemView.setOnClickListener {
            onPhotoClick(path)
        }
    }

    override fun getItemCount() = photos.size

    fun updateData(newPhotos: List<String>) {
        photos = newPhotos
        notifyDataSetChanged()
    }
}