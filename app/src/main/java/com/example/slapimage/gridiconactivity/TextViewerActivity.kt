package com.example.slapimage.gridiconactivity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.slapimage.R
import java.io.BufferedReader
import java.io.InputStreamReader

class TextViewerActivity : AppCompatActivity() {
    private lateinit var textView: TextView

    // Supported MIME types and extensions
    private val supportedMimeTypes = listOf(
        "text/plain",
        "text/x-kotlin",
        "text/javascript",
        "application/javascript",
        "text/x-java",
        "text/x-c++src",
        "text/x-python",
        "application/xml",
        "text/xml",
        "application/json"
    )

    private val supportedExtensions = listOf(
        "txt", "kt", "kts", "js", "java", "cpp", "c", "h", "py",
        "xml", "json", "html", "css", "md", "gradle", "properties", "sh"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_viewer)

        textView = findViewById(R.id.text_view)
        setupToolbar()

        // Handle the incoming intent
        handleIncomingIntent()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.toolbar_title)
        }
        toolbar.setNavigationOnClickListener { finish() }
    }

    private fun handleIncomingIntent() {
        val uri = intent?.data ?: run {
            showError("No file provided")
            finish()
            return
        }

        Log.d("TextViewer", "Received URI: $uri")
        handleFileSelection(uri) // Now properly calling the function
    }

    private fun handleFileSelection(uri: Uri) {
        if (!isFileSupported(uri)) {
            showError("File type not supported")
            finish()
            return
        }

        try {
            // Take persistent permission
            contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            contentResolver.openInputStream(uri)?.use { inputStream ->
                val text = BufferedReader(InputStreamReader(inputStream)).use { it.readText() }
                runOnUiThread {
                    textView.text = text
                    updateToolbarTitle(uri)
                }
            } ?: showError("Could not open file")
        } catch (e: SecurityException) {
            showError("Permission denied. Please try selecting the file again.")
            Log.e("TextViewer", "SecurityException", e)
        } catch (e: Exception) {
            showError("Error reading file: ${e.localizedMessage}")
            Log.e("TextViewer", "Error reading file", e)
        }
    }

    private fun isFileSupported(uri: Uri): Boolean {
        // First check by MIME type
        val mimeType = contentResolver.getType(uri)?.lowercase()
        if (mimeType != null && supportedMimeTypes.any { mimeType.contains(it) }) {
            return true
        }

        // Then check by file extension
        val fileName = uri.lastPathSegment ?: return false
        val extension = fileName.substringAfterLast('.', "").lowercase()
        return extension in supportedExtensions
    }

    private fun updateToolbarTitle(uri: Uri) {
        val fileName = uri.lastPathSegment ?: "untitled"
        supportActionBar?.title = fileName
    }

    private fun showError(message: String) {
        runOnUiThread {
            textView.text = message
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            Log.e("TextViewer", message)
        }
    }
}