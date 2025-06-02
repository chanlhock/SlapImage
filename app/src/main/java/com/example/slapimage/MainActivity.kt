/**
##################################################################################################################
# Simple and Feature Rich Android Apps with useful functions and utilities. Coded with the assistance of DeepSeek
# on Android Studio platform.
#
# Copyright (c) 2025 Bernard Chan
# chanlhock@gmail.com
#
# Date			Author          Notes
# 16/03/2025	Bernard Chan   Initial release
#
# SlapImage is licensed under the GNU General Public License v3.0
# Permissions of this strong copyleft license are conditioned on making
# available complete source code of licensed works and modifications,
# which include larger works using a licensed work, under the same
# license. Copyright and license notices must be preserved. Contributors
# provide an express grant of patent rights.
##################################################################################################################
**/
package com.example.slapimage
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.slapimage.fragments.ChatBotFragment
import com.example.slapimage.fragments.HomeFragment
import com.example.slapimage.fragments.PhotoFragment
import com.example.slapimage.fragments.PlayFragment
import com.example.slapimage.fragments.ProfileFragment
import com.example.slapimage.ibook.foobnix.android.utils.Dips
import com.example.slapimage.ibook.foobnix.android.utils.TxtUtils
import com.example.slapimage.ibook.foobnix.ext.CacheZipUtils
import com.example.slapimage.ibook.foobnix.pdf.info.AppsConfig
import com.example.slapimage.ibook.foobnix.pdf.info.IMG
import com.example.slapimage.ibook.foobnix.pdf.info.Prefs
import com.example.slapimage.ibook.foobnix.tts.TTSNotification
import com.example.slapimage.musicplayer.ApplicationClass
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val powerConnectionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Intent.ACTION_POWER_CONNECTED -> {
                    // Phone is plugged in, keep the screen on
                    window.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                }
                Intent.ACTION_POWER_DISCONNECTED -> {
                    // Phone is unplugged, allow the screen to turn off
                    window.clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                }
            }
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Lock screen orientation to portrait
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Initialize the BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Apply the ColorStateList to the icon tints
        bottomNavigationView.itemIconTintList =
            ContextCompat.getColorStateList(this, R.color.bottom_nav_icon_color)
        bottomNavigationView.itemTextColor =
            ContextCompat.getColorStateList(this, R.color.bottom_nav_icon_color)
        // Ensure all icons are always displayed
        bottomNavigationView.labelVisibilityMode = BottomNavigationView.LABEL_VISIBILITY_LABELED

        // Check for permissions
        checkPermissions()

        // Register the receiver
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }
        registerReceiver(powerConnectionReceiver, filter)

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

        // Librera
       /* val context = ApplicationClass.context
        AppsConfig.init(context)*/
        Dips.init(this)
  /*      Prefs.get().init(context)
        if (AppsConfig.IS_TEST_DEVICE) {
            val configuration = RequestConfiguration.Builder()
                .setTestDeviceIds(AppsConfig.testDevices)
                .build()
            MobileAds.setRequestConfiguration(configuration)
        }
        TTSNotification.initChannels(context)
        CacheZipUtils.init(context)
       // IMG.init(context)
        if (TxtUtils.isEmpty(AppsConfig.FLAVOR)) {
            throw RuntimeException("Application not configured correctly!")
        }*/

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+ (API level 33+)
            val permissions = mutableListOf<String>()

            if (checkSelfPermission(android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(android.Manifest.permission.READ_MEDIA_IMAGES)
            }
            if (checkSelfPermission(android.Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(android.Manifest.permission.READ_MEDIA_VIDEO)
            }
            if (checkSelfPermission(android.Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(android.Manifest.permission.READ_MEDIA_AUDIO)
            }

            if (permissions.isNotEmpty()) {
                requestPermissions(permissions.toTypedArray(), REQUEST_CODE_PERMISSIONS)
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // Android 6 to 12 (API level 23-32)
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
            val deniedPermissions = permissions.zip(grantResults.toTypedArray())
                .filter { it.second != PackageManager.PERMISSION_GRANTED }
                .map { it.first }

            if (deniedPermissions.isEmpty()) {
                // All permissions granted
            } else {
                // Some permissions denied
                Toast.makeText(this, "Permissions denied: ${deniedPermissions.joinToString(", ")}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister the receiver to avoid memory leaks
        unregisterReceiver(powerConnectionReceiver)
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 100
    }
}