package com.example.slapimage
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.app.AlertDialog
import androidx.activity.OnBackPressedCallback
import com.example.slapimage.fragments.ChatBotFragment
import com.example.slapimage.fragments.HomeFragment
import com.example.slapimage.fragments.PhotoFragment
import com.example.slapimage.fragments.PlayFragment
import com.example.slapimage.fragments.ProfileFragment


class MainActivity : AppCompatActivity() {

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Lock screen orientation to portrait
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Customize the splash screen behavior (optional)
       // splashScreen.setKeepOnScreenCondition { true } // Keep the splash screen visible until your app is ready

        // Initialize the BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Apply the ColorStateList to the icon tints
        bottomNavigationView.itemIconTintList = ContextCompat.getColorStateList(this, R.color.bottom_nav_icon_color)
        bottomNavigationView.itemTextColor = ContextCompat.getColorStateList(this,R.color.bottom_nav_icon_color)
        // Ensure all icons are always displayed
        bottomNavigationView.labelVisibilityMode = BottomNavigationView.LABEL_VISIBILITY_LABELED

        // Check for permissions
        checkPermissions()

        // Handle the back button press using OnBackPressedDispatcher
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Show the dialog when the back button is pressed
                showExitDialog()
            }
        }

        // Add the callback to the OnBackPressedDispatcher
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        // Set a listener for item selection
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.nav_search -> {
                    replaceFragment(PhotoFragment())
                    true
                }
                R.id.nav_play -> {
                    replaceFragment(PlayFragment())
                    true
                }
                R.id.nav_chatbot -> {
                    replaceFragment(ChatBotFragment())
                    true
                }
                R.id.nav_profile -> {
                    replaceFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }

        // Set the default fragment (e.g., HomeFragment)
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }

    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE_PERMISSIONS
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with video selection
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(this, "Permission denied. Cannot access video and cannot load AI model.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showExitDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Exit App")
        builder.setMessage("Are you sure you want to exit?")

        builder.setPositiveButton("Quit") { dialog, which ->
            // User clicked Quit button
            finish() // Close the activity
        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            // User cancelled the dialog
            dialog.dismiss() // Dismiss the dialog
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 100
    }
}