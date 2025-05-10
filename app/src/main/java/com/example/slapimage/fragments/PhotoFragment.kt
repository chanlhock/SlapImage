package com.example.slapimage.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.slapimage.BuildConfig
import com.example.slapimage.R
import kotlin.math.abs

class PhotoFragment : Fragment() {
    private lateinit var imageView: ImageView
    private lateinit var btnDisplayPhoto: Button
    private lateinit var ivLogo: ImageView
    private var selectedImageUri: Uri? = null
    /*private val photoResources: List<Int> by lazy {
        resources.obtainTypedArray(R.array.photo_resources).run {
            val list = (0 until length()).map { getResourceId(it, 0) }
            recycle()
            list
        }
    }*/
    private val photoResources: List<Int> by lazy {
        // Determine which array to use based on BuildConfig
        val arrayResId = if (BuildConfig.MINIMAL_BUILDSIZE) {
            R.array.photo_resources_minimal
        } else {
            R.array.photo_resources
        }

        // Load the appropriate array
        resources.obtainTypedArray(arrayResId).run {
            val list = (0 until length()).map { getResourceId(it, 0) }
            recycle()
            list
        }
    }
    private var currentPosition = 0
    private var isLimitedMode = true // Start in limited mode (7 photos)
    private val maxLimitedPosition = 6 // 0-6 = 7 photos
    private var lastPinchTime = 0L
    private val pinchCooldown = 1000L // 1 second cooldown between pinch actions

    // Register a launcher for the gallery intent
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            selectedImageUri = result.data?.data
            // Update the ImageView with the selected image
            selectedImageUri?.let { uri ->
                //imageView.setImageURI(uri)
                Glide.with(requireContext())
                    .load(uri)
                    .into(imageView)
                imageView.visibility = View.VISIBLE
                // Hide the button after the image is loaded
                btnDisplayPhoto.visibility = View.GONE
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_photo, container, false)

        // Initialize the ImageView
        ivLogo = view.findViewById(R.id.ivLogo)
        ivLogo.visibility = View.VISIBLE

        // Initialize views
        imageView = view.findViewById(R.id.imageView)
        btnDisplayPhoto = view.findViewById(R.id.btnDisplayPhoto)

        // Button to display photo
        btnDisplayPhoto.setOnClickListener {
            openGallery()
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

        // Restore the button visibility based on whether an image is already selected
        if (selectedImageUri != null) {
            // Display the selected image
            imageView.setImageURI(selectedImageUri)
            imageView.visibility = ImageView.VISIBLE
            btnDisplayPhoto.visibility = View.GONE
        }else{
            btnDisplayPhoto.visibility = View.VISIBLE
        }

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load initial photo
        updatePhoto()

        // Set up gesture detectors
        val gestureDetector =
            GestureDetector(requireContext(), object : GestureDetector.SimpleOnGestureListener() {
                override fun onFling(
                    e1: MotionEvent?,
                    e2: MotionEvent,
                    velocityX: Float,
                    velocityY: Float
                ): Boolean {
                    val diffX = e2.x - (e1?.x ?: 0f)
                    if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) showPreviousPhoto() else showNextPhoto()
                        return true
                    }
                    return false
                }

                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    view.performClick()
                    return true
                }
            })



        val scaleGestureDetector = ScaleGestureDetector(
            requireContext(),
            object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                private var initialSpan = 0f
                private var isPinchHandled = false

                override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
                    initialSpan = detector.currentSpan
                    isPinchHandled = false
                    return true
                }

                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    if (isPinchHandled) return false

                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastPinchTime < pinchCooldown) return false

                    val scaleFactor = detector.currentSpan / initialSpan
                    if (abs(scaleFactor - 1) > 0.4) { // More significant pinch required (40% change)
                        isPinchHandled = true
                        lastPinchTime = currentTime
                        togglePhotoMode()
                        return true
                    }
                    return false
                }

                override fun onScaleEnd(detector: ScaleGestureDetector) {
                    isPinchHandled = false
                }
            })

        view.setOnTouchListener { v, event ->
            scaleGestureDetector.onTouchEvent(event)
            if (!scaleGestureDetector.isInProgress) {
                gestureDetector.onTouchEvent(event)
            }

            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    if (!gestureDetector.isLongpressEnabled && !scaleGestureDetector.isInProgress) {
                        v.performClick()
                    }
                }
            }
            true
        }

    }

    private fun togglePhotoMode() {
        isLimitedMode = !isLimitedMode
        val message = if (isLimitedMode) {
            // When switching to limited mode, adjust position if needed
            if (currentPosition > maxLimitedPosition) {
                currentPosition = maxLimitedPosition
            }
            "Easter Eggs turned off (pinch to turn back on)"
        } else {
            "Showing Easter Eggs (pinch to turn off)"
        }
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        updatePhoto()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        selectedImageUri?.let { uri ->
            outState.putString("selectedImageUri", uri.toString())
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let { bundle ->
            val uriString = bundle.getString("selectedImageUri")
            if (!uriString.isNullOrEmpty()) {
                selectedImageUri = uriString.toUri() // Use the KTX extension function
                selectedImageUri?.let { uri ->
                    imageView.setImageURI(uri)
                    imageView.visibility = View.VISIBLE
                    // Hide buttons
                    btnDisplayPhoto.visibility = View.GONE
                }
            }else{
                // Show buttons
                btnDisplayPhoto.visibility = View.VISIBLE
            }
        }
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private fun showNextPhoto() {
        val maxPosition = if (isLimitedMode) maxLimitedPosition else photoResources.size - 1
        currentPosition = (currentPosition + 1) % (maxPosition + 1)
        updatePhoto()
    }

    private fun showPreviousPhoto() {
        val maxPosition = if (isLimitedMode) maxLimitedPosition else photoResources.size - 1
        currentPosition = if (currentPosition == 0) maxPosition else currentPosition - 1
        updatePhoto()
    }


    private fun updatePhoto() {
        ivLogo.setImageResource(photoResources[currentPosition])
    }

    companion object {
        private const val SWIPE_THRESHOLD = 100
        private const val SWIPE_VELOCITY_THRESHOLD = 100
    }
}