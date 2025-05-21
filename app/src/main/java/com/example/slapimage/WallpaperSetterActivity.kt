package com.example.slapimage

import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.io.IOException
import com.example.slapimage.databinding.ActivityWallpaperSetterBinding
import androidx.appcompat.widget.Toolbar

class WallpaperSetterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWallpaperSetterBinding
    private val wallpaperManager by lazy { WallpaperManager.getInstance(this) }

    // Define your wallpaper file paths here
    private val wallpaper1Path by lazy {
        File(Environment.getExternalStorageDirectory(), "wallpapers/wallpaper1.jpg").absolutePath
    }

    private val wallpaper2Path by lazy {
        File(Environment.getExternalStorageDirectory(), "wallpapers/wallpaper2.jpg").absolutePath
    }

    // Permission request launcher
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(this, getString(R.string.wallpaper_permission_granted), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, getString(R.string.wallpaper_permission_required), Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWallpaperSetterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupUI()
        loadWallpaperPreviews()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.wallpaper_setter)
        }
        toolbar.setNavigationOnClickListener {
            // Navigate back to MainActivity (which hosts HomeFragment)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            super.finish() }
    }
    private fun setupUI() {
        binding.btnSetWallpaper1.setOnClickListener {
            setWallpaper(wallpaper1Path)
        }

        binding.btnSetWallpaper2.setOnClickListener {
            setWallpaper(wallpaper2Path)
        }
    }

    private fun loadWallpaperPreviews() {
        // Load previews in background to avoid UI freezing
        Thread {
            val bitmap1 = loadBitmapFromFile(wallpaper1Path)
            val bitmap2 = loadBitmapFromFile(wallpaper2Path)

            runOnUiThread {
                if (bitmap1 != null) {
                    binding.wallpaperPreview1.setImageBitmap(bitmap1)
                } else {
                    showErrorDialog(getString(R.string.wallpaper_file_not_found))
                }

                if (bitmap2 != null) {
                    binding.wallpaperPreview2.setImageBitmap(bitmap2)
                } else {
                    showErrorDialog(getString(R.string.wallpaper_file_not_found))
                }
            }
        }.start()
    }

    private fun setWallpaper(imagePath: String) {
        binding.progressIndicator.isVisible = true

        Thread {
            try {
                val bitmap = loadBitmapFromFile(imagePath)
                if (bitmap != null) {
                    wallpaperManager.setBitmap(bitmap)
                    runOnUiThread {
                        binding.progressIndicator.isVisible = false
                        Toast.makeText(
                            this,
                            R.string.wallpaper_set_success,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    runOnUiThread {
                        binding.progressIndicator.isVisible = false
                        showErrorDialog(getString(R.string.wallpaper_file_not_found))
                    }
                }
            } catch (e: IOException) {
                runOnUiThread {
                    binding.progressIndicator.isVisible = false
                    showErrorDialog(getString(R.string.wallpaper_set_failed))
                }
            }
        }.start()
    }

    private fun loadBitmapFromFile(path: String): Bitmap? {
        return try {
            BitmapFactory.decodeFile(path)
        } catch (e: Exception) {
            null
        }
    }

    private fun showErrorDialog(message: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.error)
            .setMessage(message)
            .setPositiveButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    // Handle the Up button click
    override fun onSupportNavigateUp(): Boolean {
        // Navigate back to MainActivity (which hosts HomeFragment)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Close the current sub-activity
        // Enable the Up button (back arrow)
        return true
    }

    companion object {
        // Helper function to ensure wallpapers are available
        fun ensureWallpaperFilesExist(context: Context) {
            val wallpapersDir = File(context.getExternalFilesDir(null), "wallpapers")
            if (!wallpapersDir.exists()) {
                wallpapersDir.mkdirs()
                // In a real app, you would copy your wallpaper files from assets/res to this directory
                // For production, you might download them from a server or let users select their own
            }
        }
    }
}