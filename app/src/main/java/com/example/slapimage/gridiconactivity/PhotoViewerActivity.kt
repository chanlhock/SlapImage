package com.example.slapimage.gridiconactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.slapimage.R
import com.github.chrisbanes.photoview.PhotoView

class PhotoViewerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_viewer)

        val photoPath = intent.getStringExtra("photo_path") ?: return

        val photoView = findViewById<PhotoView>(R.id.photoView)
        Glide.with(this)
            .load(photoPath)
            .into(photoView)
    }
}