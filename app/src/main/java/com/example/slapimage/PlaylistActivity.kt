package com.example.slapimage
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class PlaylistActivity : AppCompatActivity() {

    // Views
    private lateinit var recyclerView: RecyclerView
    private lateinit var nowPlayingLayout: LinearLayout
    private lateinit var nowPlayingTitle: TextView
    private lateinit var nowPlayingArtist: TextView
    private lateinit var nowPlayingPlayPause: ImageView

    // Data
    private val songList = mutableListOf<Song>()
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var adapter: SongAdapter
    private var isPlaying = false
    private var currentSongIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        // Initialize views
        initViews()

        // Setup RecyclerView
        adapter = SongAdapter(songList) { position ->
            playSong(position)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Initialize MediaPlayer
        mediaPlayer = MediaPlayer().apply {
            setOnCompletionListener { playNextSong() }
        }

        // Check permissions and load playlist
        if (checkStoragePermission()) {
            loadPlaylist()
        }
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerView)
        nowPlayingLayout = findViewById(R.id.nowPlayingLayout)
        nowPlayingTitle = findViewById(R.id.nowPlayingTitle)
        nowPlayingArtist = findViewById(R.id.nowPlayingArtist)
        nowPlayingPlayPause = findViewById(R.id.nowPlayingPlayPause)

        findViewById<ImageButton>(R.id.backButton).setOnClickListener { finish() }
        findViewById<ImageButton>(R.id.addButton).setOnClickListener { openFilePicker() }
        findViewById<Button>(R.id.playAllButton).setOnClickListener { playAllSongs() }
        findViewById<Button>(R.id.shuffleButton).setOnClickListener { shuffleSongs() }
        nowPlayingPlayPause.setOnClickListener { togglePlayPause() }
    }


    private fun checkStoragePermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
            false
        }
    }

    private fun loadPlaylist() {
        val file = File(filesDir, "playlist.txt")
        if (!file.exists()) {
            file.createNewFile()
            return
        }

        try {
            FileReader(file).use { reader ->
                songList.clear()
                reader.readLines().forEach { line ->
                    if (line.isNotBlank()) {
                        val parts = line.split("|")
                        when (parts.size) {
                            4 -> songList.add(Song(parts[0], parts[1], parts[2], parts[3]))
                            3 -> songList.add(Song(parts[0], parts[1], parts[2], "")) // Legacy format
                        }
                    }
                }
                runOnUiThread {
                    adapter.notifyDataSetChanged()
                    updateSongCountDisplay()
                }
            }
        } catch (e: Exception) {
            showToast("Error loading playlist")
            Log.e("Playlist", "Error loading playlist", e)
        }
    }

    private fun savePlaylist() {
        val file = File(filesDir, "playlist.txt")
        try {
            FileWriter(file).use { writer ->
                songList.forEach { song ->
                    writer.write("${song.title}|${song.artist}|${song.duration}|${song.filePath}\n")
                }
            }
        } catch (e: Exception) {
            showToast("Error saving playlist")
            Log.e("Playlist", "Error saving playlist", e)
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "audio/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(intent, PICK_AUDIO_REQUEST)
    }

    private fun playAllSongs() {
        if (songList.isNotEmpty()) {
            playSong(0)
        } else {
            showToast("Playlist is empty")
        }
    }

    private fun shuffleSongs() {
        if (songList.isNotEmpty()) {
            playSong((0 until songList.size).random())
        } else {
            showToast("Playlist is empty")
        }
    }

    private fun playSong(position: Int) {
        if (position !in songList.indices) return

        currentSongIndex = position
        val song = songList[position]

        try {
            mediaPlayer.reset()
            if (song.filePath.isNotEmpty()) {
                mediaPlayer.setDataSource(song.filePath)
                mediaPlayer.prepare()
                mediaPlayer.start()

                updateNowPlaying(song)
                nowPlayingLayout.visibility = View.VISIBLE
                isPlaying = true
                nowPlayingPlayPause.setImageResource(R.drawable.ic_pause)
            } else {
                showToast("Song file not found")
            }
        } catch (e: Exception) {
            showToast("Error playing song")
            Log.e("Playlist", "Error playing song", e)
        }
    }

    private fun updateNowPlaying(song: Song) {
        nowPlayingTitle.text = song.title
        nowPlayingArtist.text = song.artist
    }

    private fun playNextSong() {
        if (currentSongIndex < songList.size - 1) {
            playSong(currentSongIndex + 1)
        } else {
            isPlaying = false
            nowPlayingPlayPause.setImageResource(R.drawable.ic_play)
        }
    }

    private fun togglePlayPause() {
        if (isPlaying) {
            mediaPlayer.pause()
            isPlaying = false
            nowPlayingPlayPause.setImageResource(R.drawable.ic_play)
        } else if (currentSongIndex != -1) {
            mediaPlayer.start()
            isPlaying = true
            nowPlayingPlayPause.setImageResource(R.drawable.ic_pause)
        }
    }

    private fun updateSongCountDisplay() {
        val songCountTextView: TextView = findViewById(R.id.songCountTextView)
        val totalSeconds = songList.sumOf { song ->
            val parts = song.duration.split(":")
            when (parts.size) {
                2 -> parts[0].toInt() * 60 + parts[1].toInt()
                else -> 0
            }
        }
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        songCountTextView.text = "${songList.size} Songs · $minutes:${seconds.toString().padStart(2, '0')}"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_AUDIO_REQUEST && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                val filePath = getPathFromUri(uri) ?: uri.toString()
                val title = uri.lastPathSegment?.substringBeforeLast(".") ?: "Unknown Song"
                songList.add(Song(title, "Unknown Artist", "0:00", filePath))
                adapter.notifyItemInserted(songList.size - 1)
                savePlaylist()
                updateSongCountDisplay()
            }
        }
    }

    private fun getPathFromUri(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Audio.Media.DATA)
        return contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
            } else null
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadPlaylist()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    inner class SongAdapter(
        private val songs: List<Song>,
        private val onItemClick: (Int) -> Unit
    ) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

        inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val title: TextView = itemView.findViewById(R.id.songTitle)
            val artist: TextView = itemView.findViewById(R.id.songArtist)
            val duration: TextView = itemView.findViewById(R.id.songDuration)

            init {
                itemView.setOnClickListener { onItemClick(adapterPosition) }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_song, parent, false)
            return SongViewHolder(view)
        }

        override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
            val song = songs[position]
            holder.title.text = song.title
            holder.artist.text = song.artist
            holder.duration.text = song.duration
        }

        override fun getItemCount(): Int = songs.size
    }

    data class Song(
        val title: String,
        val artist: String,
        val duration: String,
        val filePath: String
    )

    companion object {
        private const val PICK_AUDIO_REQUEST = 1001
        private const val STORAGE_PERMISSION_CODE = 1002
    }
}