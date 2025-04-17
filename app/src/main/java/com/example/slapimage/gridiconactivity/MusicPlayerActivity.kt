package com.example.slapimage.gridiconactivity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.slapimage.R
import java.util.Locale
import java.util.concurrent.TimeUnit

class MusicPlayerActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var runnable: Runnable
    private val handler = Handler(Looper.getMainLooper())
    private var isPlaying = false
    private var currentSongUri: Uri? = null
    private var currentSongName: String = ""

    // UI Components
    private lateinit var tvSongTitle: TextView
    private lateinit var tvCurrentTime: TextView
    private lateinit var tvTotalTime: TextView
    private lateinit var tvArtist: TextView
    private lateinit var seekBar: SeekBar
    private lateinit var btnPlayPause: ImageButton
    private lateinit var btnStop: ImageButton
    private lateinit var btnForward: ImageButton
    private lateinit var btnBackward: ImageButton
    private lateinit var btnSelectSong: Button
    private lateinit var ivAlbumArt: ImageView

    private val audioPickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let { uri ->
                currentSongUri = uri
                getSongInfo(uri)
                initializeMediaPlayer(uri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)

        // Initialize views with null checks
        //tvSongTitle = findViewById(R.id.tvSongTitle) ?: throw IllegalStateException("tvSongTitle not found")
        //tvArtist = findViewById(R.id.tvArtist) ?: throw IllegalStateException("tvArtist not found")

        initViews()

         // Initialize UI text
        findViewById<TextView>(R.id.tvPlayingFrom)?.text = getString(R.string.playing_from)
        findViewById<TextView>(R.id.tvSource)?.text = getString(R.string.device_storage)
        tvSongTitle.text = getString(R.string.no_song_selected)

        tvArtist.text = getString(R.string.unknown_artist)
        tvCurrentTime.text = getString(R.string.default_time)
        tvTotalTime.text = getString(R.string.default_time)
        btnSelectSong.text = getString(R.string.select_song)

        // Set accessibility content descriptions
        btnPlayPause.contentDescription = getString(R.string.play)
        btnStop.contentDescription = getString(R.string.stop)
        btnForward.contentDescription = getString(R.string.forward_10)
        btnBackward.contentDescription = getString(R.string.backward_10)
        setupListeners()


        checkPermissions()
    }

    private fun initViews() {
        tvSongTitle = findViewById(R.id.tvSongTitle)
        tvCurrentTime = findViewById(R.id.tvCurrentTime)
        tvTotalTime = findViewById(R.id.tvTotalTime)
        tvArtist = findViewById(R.id.tvArtist) // Initialize tvArtist here
        seekBar = findViewById(R.id.seekBar)
        btnPlayPause = findViewById(R.id.btnPlayPause)
        btnStop = findViewById(R.id.btnStop)
        btnForward = findViewById(R.id.btnForward)
        btnBackward = findViewById(R.id.btnBackward)
        btnSelectSong = findViewById(R.id.btnSelectSong)
        ivAlbumArt = findViewById(R.id.ivAlbumArt)
    }

    private fun setupListeners() {
        btnPlayPause.setOnClickListener { togglePlayPause() }
        btnStop.setOnClickListener { stopPlaying() }
        btnForward.setOnClickListener { seekForward() }
        btnBackward.setOnClickListener { seekBackward() }
        btnSelectSong.setOnClickListener { openFilePicker() }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_STORAGE_PERMISSION
            )
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "audio/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("audio/mpeg", "audio/mp3"))
        }
        audioPickerLauncher.launch(intent)
    }

    private fun getSongInfo(uri: Uri) {
        try {
            contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    val artistIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)

                    currentSongName = cursor.getString(nameIndex)
                    tvSongTitle.text = currentSongName

                    val artist = if (artistIndex != -1) {
                        cursor.getString(artistIndex).takeIf { it?.isNotBlank() == true }
                    } else null

                    tvArtist.text = artist ?: getString(R.string.unknown_artist)
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error reading song info", Toast.LENGTH_SHORT).show()
        }
    }


    private fun initializeMediaPlayer(uri: Uri) {
        try {
            if (::mediaPlayer.isInitialized) {
                mediaPlayer.release()
            }

            mediaPlayer = MediaPlayer.create(this, uri) ?: throw IllegalStateException("MediaPlayer creation failed")

            mediaPlayer.setOnCompletionListener {
                btnPlayPause.setImageResource(android.R.drawable.ic_media_play)
                btnPlayPause.contentDescription = getString(R.string.play)
                isPlaying = false
                handler.removeCallbacks(runnable)
            }

            seekBar.max = mediaPlayer.duration
            tvTotalTime.text = formatDuration(mediaPlayer.duration)

            runnable = object : Runnable {
                override fun run() {
                    try {
                        if (::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
                            val currentPos = mediaPlayer.currentPosition
                            seekBar.progress = currentPos
                            tvCurrentTime.text = formatDuration(currentPos)
                            handler.postDelayed(this, 1000)
                        }
                    } catch (e: IllegalStateException) {
                        // Log or handle media player errors
                        Log.e("MusicPlayer", "MediaPlayer error during progress update", e)
                    }
                }
            }

            handler.post(runnable)
        } catch (e: Exception) {
            Toast.makeText(this, "Error initializing player: ${e.message}", Toast.LENGTH_SHORT).show()
            Log.e("MusicPlayer", "Error initializing MediaPlayer", e)
        }
    }

    private fun togglePlayPause() {
        try {
            if (::mediaPlayer.isInitialized) {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.pause()
                    btnPlayPause.setImageResource(android.R.drawable.ic_media_play)
                    btnPlayPause.contentDescription = getString(R.string.play)
                    isPlaying = false
                } else {
                    mediaPlayer.start()
                    btnPlayPause.setImageResource(android.R.drawable.ic_media_pause)
                    btnPlayPause.contentDescription = getString(R.string.pause)
                    isPlaying = true
                    handler.postDelayed(runnable, 0)
                }
            } else {
                Toast.makeText(this, getString(R.string.no_song_selected), Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Playback error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopPlaying() {
        try {
            if (::mediaPlayer.isInitialized) {
                mediaPlayer.stop()
                mediaPlayer.prepare()
                mediaPlayer.seekTo(0)
                seekBar.progress = 0
                tvCurrentTime.text = getString(R.string.default_time)
                btnPlayPause.setImageResource(android.R.drawable.ic_media_play)
                btnPlayPause.contentDescription = getString(R.string.play)
                isPlaying = false
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Stop error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun seekForward() {
        if (::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
            val newPosition = mediaPlayer.currentPosition + 10000
            mediaPlayer.seekTo(newPosition.coerceAtMost(mediaPlayer.duration))
        }
    }

    private fun seekBackward() {
        if (::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
            val newPosition = mediaPlayer.currentPosition - 10000
            mediaPlayer.seekTo(newPosition.coerceAtLeast(0))
        }
    }

    private fun formatDuration(duration: Int): String {
        return if (duration == 0) {
            getString(R.string.default_time)
        } else {
            String.Companion.format(
                Locale.getDefault(),
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration.toLong()),
                TimeUnit.MILLISECONDS.toSeconds(duration.toLong()) % 60
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            if (::mediaPlayer.isInitialized) {
                mediaPlayer.release()
            }
            handler.removeCallbacks(runnable)
        } catch (e: Exception) {
            Log.e("MusicPlayer", "Error during cleanup", e)  // Now using the 'e' parameter
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, getString(R.string.permission_needed), Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val READ_STORAGE_PERMISSION = 101
    }
}