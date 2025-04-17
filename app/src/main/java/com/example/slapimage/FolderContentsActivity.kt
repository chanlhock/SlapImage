package com.example.slapimage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class FolderContentsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: PhotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder_contents)

        val folderPath = intent.getStringExtra("folder_path") ?: return
        val folderName = intent.getStringExtra("folder_name") ?: ""

        title = folderName

        recyclerView = findViewById(R.id.photosRecyclerView)
        progressBar = findViewById(R.id.progressBar)

        recyclerView.layoutManager = GridLayoutManager(this, 3)
        adapter = PhotoAdapter(this, emptyList()) { photoPath ->
            openPhoto(photoPath)
        }
        recyclerView.adapter = adapter

        loadPhotos(folderPath)
    }

    private fun loadPhotos(folderPath: String) {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        Thread {
            val photos = scanForPhotos(folderPath)

            runOnUiThread {
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                adapter.updateData(photos)
            }
        }.start()
    }

    private fun scanForPhotos(folderPath: String): List<String> {
        val folder = File(folderPath)
        val contents = mutableListOf<String>()

        // Add all photos from current folder
        folder.listFiles()
            ?.filter { it.isFile && isImageFile(it) }
            ?.forEach { contents.add(it.absolutePath) }

        // Add subfolders that contain photos (only if current folder has photos)
        if (contents.isNotEmpty()) {
            folder.listFiles()
                ?.filter { it.isDirectory }
                ?.forEach { subfolder ->
                    subfolder.listFiles()
                        ?.firstOrNull { it.isFile && isImageFile(it) }
                        ?.let { contents.add(subfolder.absolutePath) }
                }
        }

        return contents.sortedByDescending { path ->
            try {
                File(path).let { file ->
                    if (file.isDirectory) {
                        file.listFiles()
                            ?.filter { it.isFile && isImageFile(it) }
                            ?.maxOfOrNull { it.lastModified() } ?: 0L
                    } else {
                        file.lastModified()
                    }
                }
            } catch (e: SecurityException) {
                0L
            }
        }
    }

    private fun isImageFile(file: File): Boolean {
        val name = file.name.lowercase()
        return name.endsWith(".jpg") || name.endsWith(".jpeg") ||
                name.endsWith(".png") || name.endsWith(".gif") ||
                name.endsWith(".bmp") || name.endsWith(".webp")
    }

    private fun openPhoto(photoPath: String) {
        val intent = Intent(this, PhotoViewerActivity::class.java).apply {
            putExtra("photo_path", photoPath)
        }
        startActivity(intent)
    }
}