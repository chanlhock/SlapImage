package com.example.slapimage.fragments

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.slapimage.AccessibleImageView
import com.example.slapimage.AccessibleVideoView
import com.example.slapimage.R
import com.example.slapimage.gridiconactivity.SwipeDirection
import com.example.slapimage.gridiconactivity.addSwipeListener
import android.graphics.Color
import androidx.activity.OnBackPressedCallback
import com.example.slapimage.BuildConfig

class PlayFragment : Fragment() {
    private lateinit var btnPlayVideo: Button
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var ivLogo: AccessibleImageView
    private lateinit var tvMessage: TextView
    private var selectedVideoUri: Uri? = null
    private var isLoopingEnabled: Boolean = true // Default to looping enabled
    private var mediaPlayer: MediaPlayer? = null // Store MediaPlayer instance
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var isShowing3Items = true // Flag to track current display state
    private lateinit var seekBar: SeekBar
    private lateinit var controlsContainer: LinearLayout
    private val hideControlsRunnable = Runnable { hideControlButtons() }
    private val handler = Handler(Looper.getMainLooper())
    private val hideControlsDelay = 3000L // 3 seconds
    private lateinit var btnPlayPause: Button
    private lateinit var btnForward: Button
    private lateinit var btnBackward: Button
    private lateinit var videoView: AccessibleVideoView

    // Register a launcher for the video picker intent
    private val videoPickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            selectedVideoUri = result.data?.data
            if (selectedVideoUri == null) {
                Log.e("VideoURI", "Selected video URI is null.")
                //Toast.makeText(this, "Failed to get video URI.", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("VideoURI", "Selected video URI: $selectedVideoUri")
                tvMessage.visibility = View.GONE
                playVideo()
            }
        } else {
            Log.e("VideoPicker", "Failed to pick video. Result code: ${result.resultCode}")
            //Toast.makeText(this, "Failed to pick video.", Toast.LENGTH_SHORT).show()
        }
    }

    // Two separate GIF lists
    private val gifList3 = listOf(
        R.drawable.gif3,
        R.drawable.gif6
    )

    private val gifList5 = when {
        BuildConfig.MINIMAL_BUILDSIZE == true -> listOf(
            R.drawable.gif3,
            R.drawable.gif6,
            R.drawable.gif4
        )
        else -> listOf(
            R.drawable.gif3,
            R.drawable.gif6,
            R.drawable.gif4,
            R.drawable.gif5
        )
    }
    private var currentGifIndex = 0
    private var currentGifList = gifList3 // Start with 3 items

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_play, container, false)
        // Initialize constraintLayout
        constraintLayout = view.findViewById(R.id.constraintLayout) // Replace with your actual ID

        // Initialize the ImageView
        ivLogo = view.findViewById(R.id.ivLogo)
        ivLogo.visibility = View.VISIBLE
        tvMessage = view.findViewById(R.id.tvMessage)
        tvMessage.visibility = View.VISIBLE

        // Initialize views
        videoView = view.findViewById(R.id.videoView)
        btnPlayVideo = view.findViewById(R.id.btnPlayVideo)
        // Initialize all buttons
        btnPlayPause = view.findViewById(R.id.btnPlayPause)
        btnForward = view.findViewById(R.id.btnForward)
        btnBackward = view.findViewById(R.id.btnBackward)
        // Initialize buttons with resource strings
        btnPlayPause.text = getString(R.string.play)
        btnForward.text = getString(R.string.forward)
        btnBackward.text = getString(R.string.backward)

        // Set content descriptions for accessibility
        btnPlayPause.contentDescription = getString(R.string.play_pause_content_description)
        btnForward.contentDescription = getString(R.string.forward_content_description)
        btnBackward.contentDescription = getString(R.string.backward_content_description)
        videoView.contentDescription = getString(R.string.video_content_description)
        // Set click listeners
        btnPlayPause.setOnClickListener { togglePlayPause() }
        btnForward.setOnClickListener { seekForward() }
        btnBackward.setOnClickListener { seekBackward() }

        // Initialize scale gesture detector
        scaleGestureDetector = ScaleGestureDetector(requireContext(), ScaleListener())

        ivLogo.setOnTouchListener { v, event ->
            scaleGestureDetector.onTouchEvent(event)

            // Handle click for accessibility
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    v.performClick()
                }
            }
            true
        }
        // Set up the scale listener
        ivLogo.onScaleListener = {
            // Toggle between 3 and 5 items
            isShowing3Items = !isShowing3Items
            currentGifList = if (isShowing3Items) gifList3 else gifList5
            currentGifIndex = 0
            loadGif(currentGifList[currentGifIndex])

            // Show feedback to user
            Toast.makeText(
                requireContext(),
                if (isShowing3Items) "Easter Eggs turned off" else "Showing Easter Eggs",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Load initial GIF
        loadGif(currentGifList[currentGifIndex])

        // Load initial GIF
        //loadGif(gifList[currentGifIndex])

        // Set up swipe detection
        ivLogo.addSwipeListener(requireContext()) { direction ->
            when (direction) {
                SwipeDirection.LEFT -> showNextGif()
                SwipeDirection.RIGHT -> showPreviousGif()
            }
        }

        // Button to play video
        btnPlayVideo.setOnClickListener {
            openVideoPicker()
        }

        // Create the callback
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Navigate back to HomeFragment
                parentFragmentManager.popBackStack()
            }
        }

        // Register the callback with the correct dispatcher
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )

        // Initialize new views
        seekBar = view.findViewById(R.id.seekBar)
        controlsContainer = view.findViewById(R.id.controlsContainer)

        // Set up the touch listener properly
        videoView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Handle touch down if needed
                    true
                }
                MotionEvent.ACTION_UP -> {
                    // Handle touch up
                    toggleControlsVisibility()
                    v.performClick()  // This is crucial for accessibility
                    true
                }
                else -> false
            }
        }
        controlsContainer.isVisible = false // Using KTX extension

        // Important: Make the view clickable
        videoView.isClickable = true
        videoView.setOnPreparedListener { mp ->
            mediaPlayer = mp
            mp.isLooping = isLoopingEnabled
            adjustVideoViewSize(mp.videoWidth, mp.videoHeight)

            // Initialize buttons
            btnPlayPause.isEnabled = true
            btnForward.isEnabled = true
            btnBackward.isEnabled = true

            // Start with play button in correct state
            btnPlayPause.text = if (mp.isPlaying) "Pause" else "Play"

            mp.start()
            resetHideControlsTimer()
        }
        // Set up seek bar listener
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

                if (fromUser) {
                    videoView.seekTo(progress)
                    resetHideControlsTimer()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                handler.removeCallbacks(hideControlsRunnable)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                resetHideControlsTimer()
            }
        })
        // Update seek bar periodically
        val updateSeekBar = object : Runnable {
            override fun run() {
                if (videoView.isPlaying) {
                    seekBar.progress = videoView.currentPosition
                }
                handler.postDelayed(this, 1000) // Update every second
            }
        }
        handler.post(updateSeekBar)

        // Inflate the layout for this fragment
        return view
    }

    private fun showControlButtons() {
        controlsContainer.isVisible = true
        updatePlayPauseButton()
        resetHideControlsTimer()
    }

    private fun hideControlButtons() {
        controlsContainer.isVisible = false
    }

    private fun toggleControlsVisibility() {
        if (controlsContainer.isVisible) {
            hideControlButtons()
        } else {
            showControlButtons()  // Now properly used here
        }
    }


    private fun resetHideControlsTimer() {
        handler.removeCallbacks(hideControlsRunnable)
        handler.postDelayed(hideControlsRunnable, hideControlsDelay)
    }

    private fun updatePlayPauseButton() {
        btnPlayPause.text = if (videoView.isPlaying) "Pause" else "Play"
    }

    private fun togglePlayPause() {
        if (videoView.isPlaying) {
            videoView.pause()
            btnPlayPause.text = getString(R.string.play)
        } else {
            videoView.start()
            btnPlayPause.text = getString(R.string.pause)
            resetHideControlsTimer()
        }
        // Update content description as well
        btnPlayPause.contentDescription = if (videoView.isPlaying) {
            getString(R.string.pause)
        } else {
            getString(R.string.play)
        }
    }

    private fun seekForward() {
        val newPosition = (videoView.currentPosition + 5000).coerceAtMost(videoView.duration)
        videoView.seekTo(newPosition)
        resetHideControlsTimer()
    }

    private fun seekBackward() {
        val newPosition = (videoView.currentPosition - 5000).coerceAtLeast(0)
        videoView.seekTo(newPosition)
        resetHideControlsTimer()
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScaleEnd(detector: ScaleGestureDetector) {
            // Toggle between 3 and 5 items when pinch gesture ends
            isShowing3Items = !isShowing3Items
            currentGifList = if (isShowing3Items) gifList3 else gifList5
            currentGifIndex = 0
            loadGif(currentGifList[currentGifIndex])
            Toast.makeText(requireContext(),
                if (isShowing3Items) "Showing 3 GIFs" else "Showing 5 GIFs",
                Toast.LENGTH_SHORT).show()
        }
    }


    private fun loadGif(gifResource: Int) {
        Glide.with(requireContext())
            .asGif()
            .load(gifResource)
            .into(ivLogo)
    }

    private fun showNextGif() {
        currentGifIndex = (currentGifIndex + 1) % currentGifList.size
        loadGif(currentGifList[currentGifIndex])
    }

    private fun showPreviousGif() {
        currentGifIndex = (currentGifIndex - 1 + currentGifList.size) % currentGifList.size
        loadGif(currentGifList[currentGifIndex])
    }

    //private fun showNextGif() {
    //    currentGifIndex = (currentGifIndex + 1) % gifList.size
    //    loadGif(gifList[currentGifIndex])
   // }

    //private fun showPreviousGif() {
    //    currentGifIndex = (currentGifIndex - 1 + gifList.size) % gifList.size
    //    loadGif(gifList[currentGifIndex])
    //}

    private fun openVideoPicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            //Intent.setType = "video/mp4"
            type = "video/*" // This will allow all video types, not only MP4
        }
        videoPickerLauncher.launch(intent)
    }

    private fun playVideo() {
        if (selectedVideoUri == null) {
            Log.e("VideoPlayback", "Selected video URI is null.")
            Toast.makeText(requireContext(), "No video selected.", Toast.LENGTH_SHORT).show()
            return
        }
        if (selectedVideoUri != null) {
            if (verifyFileAccessibility(selectedVideoUri!!)) {
                try {
                    videoView.setVideoURI(selectedVideoUri)
                    videoView.visibility = View.VISIBLE
                    videoView.setOnPreparedListener { mp ->
                        Log.d("VideoPlayback", "Video prepared. Starting playback.")
                        // Enable looping

                        mediaPlayer = mp
                        mp.isLooping = isLoopingEnabled
                        // Get video dimensions
                        val videoWidth = mp.videoWidth
                        val videoHeight = mp.videoHeight
                        Log.d("VideoDimensions", "Video width: $videoWidth, height: $videoHeight")

                        // Adjust VideoView size to fit screen width
                        adjustVideoViewSize(videoWidth, videoHeight)
                        // Initialize seek bar
                        seekBar.max = mp.duration
                        seekBar.progress = 0
                        constraintLayout.setBackgroundColor(Color.BLACK)
                        // Start playing the video
                        mp.start()
                    }
                    hideButtons()
                    // Set up the OnCompletionListener (optional, for extra safety)
                    videoView.setOnCompletionListener {
                        Log.d("VideoPlayback", "Video completed. Restarting playback.")

                        if (isLoopingEnabled) {
                            btnPlayPause.text = getString(R.string.play)
                        }
                        seekBar.progress = seekBar.max
                          //  videoView.start() // Restart the video if looping is enabled
                        //}

                    }

                    videoView.setOnErrorListener { _, what, extra ->
                        Log.e("VideoPlayback", "Failed to play video. Error: what=$what, extra=$extra")
                        Toast.makeText(requireContext(), "Failed to play video. Error: $what, $extra", Toast.LENGTH_SHORT).show()
                        false
                    }
                    //hideButtons()
                } catch (e: Exception) {
                    Log.e("VideoPlayback", "Exception while playing video: ${e.message}")
                    Toast.makeText(requireContext(), "Failed to play video: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.e("VideoPlayback", "Selected video URI is null.")
                Toast.makeText(requireContext(), "No video selected.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.e("VideoPlayback", "Selected video URI is null.")
            Toast.makeText(requireContext(), "No video selected.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun verifyFileAccessibility(uri: Uri): Boolean {
        return try {
            requireContext().contentResolver.openFileDescriptor(uri, "r")?.use {
                Log.d("FileAccess", "File is accessible: $uri")
                true
            } == true
        } catch (e: Exception) {
            Log.e("FileAccess", "File is not accessible: ${e.message}")
            false
        }
    }

    private fun adjustVideoViewSize(videoWidth: Int, videoHeight: Int) {
        // Get screen dimensions
        val screenWidth: Int
        val screenHeight: Int
        // Access windowManager from the parent activity
        val windowManager = requireActivity().windowManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Use WindowMetrics for API level 30 and higher
            val windowMetrics = windowManager.currentWindowMetrics
            screenWidth = windowMetrics.bounds.width()
            screenHeight = windowMetrics.bounds.height()
        } else {
            // Use DisplayMetrics for API levels below 30
            val displayMetrics = DisplayMetrics()
            @Suppress("DEPRECATION")
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            screenWidth = displayMetrics.widthPixels
            screenHeight = displayMetrics.heightPixels
        }

        // Calculate aspect ratio of the video
        val videoAspectRatio = videoWidth.toFloat() / videoHeight.toFloat()

        // Calculate new dimensions for the VideoView
        val newWidth = screenWidth // Fit to screen width
        val newHeight = (screenWidth / videoAspectRatio).toInt() // Maintain aspect ratio

        // Set VideoView dimensions
        val layoutParams = videoView.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.width = newWidth
        layoutParams.height = newHeight
        videoView.layoutParams = layoutParams

        // Center the VideoView on the screen
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.connect(
            videoView.id,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP,
            0
        )
        constraintSet.connect(
            videoView.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM,
            0
        )
        constraintSet.connect(
            videoView.id,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START,
            0
        )
        constraintSet.connect(
            videoView.id,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END,
            0
        )
        constraintSet.applyTo(constraintLayout)
    }

    private fun hideButtons() {
        // Hide the ImageView
        ivLogo.visibility = View.GONE
        btnPlayVideo.visibility = View.GONE
    }
    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(hideControlsRunnable)
    }

}