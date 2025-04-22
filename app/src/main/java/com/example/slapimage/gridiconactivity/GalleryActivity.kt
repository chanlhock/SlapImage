package com.example.slapimage.gridiconactivity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.slapimage.gridiconactivity.FolderContentsActivity
import com.example.slapimage.PhotoFolder
import com.example.slapimage.adapters.PhotoFolderAdapter
import com.example.slapimage.R
import java.io.File

class GalleryActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: PhotoFolderAdapter
    private val STORAGE_PERMISSION_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_folders)

        // Initialize views
        recyclerView = findViewById(R.id.foldersRecyclerView)
        progressBar = findViewById(R.id.progressBar)

        // Setup RecyclerView with explicit types
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = PhotoFolderAdapter(this, emptyList()) { folder ->
            openFolder(folder)
        }
        recyclerView.adapter = adapter

        checkStoragePermission()
    }

    private fun checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            loadPhotoFolders()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadPhotoFolders()
            } else {
                Toast.makeText(
                    this,
                    "Permission denied. Can't access photos.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun loadPhotoFolders() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        Thread {
            val folders = scanForPhotoFolders()

            runOnUiThread {
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                adapter.updateData(folders)
            }
        }.start()
    }

    private fun scanForPhotoFolders(): List<PhotoFolder> {
        val uri = if (Build.MANUFACTURER.equals("HUAWEI", ignoreCase = true)) {
            MediaStore.Images.Media.getContentUri("external")
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val projection = arrayOf(
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.DATE_TAKEN
        )

        val selection = if (Build.MANUFACTURER.equals("HUAWEI", ignoreCase = true)) {
            "${MediaStore.Images.Media.BUCKET_ID} IS NOT NULL"
        } else {
            null
        }

        val cursor = contentResolver.query(
            uri,
            projection,
            selection,
            null,
            "${MediaStore.Images.Media.DATE_TAKEN} ASC"
        ) ?: return emptyList()

        val folderMap = mutableMapOf<String, PhotoFolder>()

        try {
            val bucketNameColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
            val bucketIdColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID)
            val dateTakenColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN)

            if (bucketNameColumn == -1 || dataColumn == -1 || bucketIdColumn == -1 || dateTakenColumn == -1) {
                return emptyList()
            }

            while (cursor.moveToNext()) {
                try {
                    val bucketName = cursor.getString(bucketNameColumn) ?: continue
                    val data = cursor.getString(dataColumn) ?: continue
                    val bucketId = cursor.getString(bucketIdColumn) ?: continue
                    val dateTaken = cursor.getLong(dateTakenColumn)

                    val folderPath = File(data).parent ?: continue

                    val folder = folderMap[bucketId] ?: PhotoFolder(
                        id = bucketId,
                        name = bucketName,
                        path = folderPath,
                        thumbnailPath = data,
                        photoCount = 0,
                        newestPhotoDate = dateTaken,
                        isSinglePhoto = false
                    ).also { folderMap[bucketId] = it }

                    folder.photoCount++
                    if (dateTaken > folder.newestPhotoDate) {
                        folder.newestPhotoDate = dateTaken
                        folder.thumbnailPath = data
                    }
                } catch (e: Exception) {
                    continue // Skip problematic entries
                }
            }
        } finally {
            cursor.close()
        }

        folderMap.values.forEach { folder ->
            if (folder.photoCount == 1) {
                folder.isSinglePhoto = true
            }
        }

        return folderMap.values.sortedBy { it.newestPhotoDate }
    }

    private fun openFolder(folder: PhotoFolder) {
        val intent = Intent(this, FolderContentsActivity::class.java).apply {
            putExtra("folder_path", folder.path)
            putExtra("folder_name", folder.name)
        }
        startActivity(intent)
    }
}