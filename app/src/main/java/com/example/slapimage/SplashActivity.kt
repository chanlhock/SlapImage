package com.example.slapimage

import android.annotation.SuppressLint
import android.content.Intent
import android.app.ActivityOptions
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.OnBackPressedCallback

@SuppressLint("SourceLockedOrientationActivity")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        // Lock screen orientation to portrait
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Find the TextView by its ID
        val splashicon = findViewById<ImageView>(R.id.splash_icon)
        splashicon.visibility = View.GONE
        // Modern back press handling
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Current implementation blocks back button during splash
            }
        })

        Handler(Looper.getMainLooper()).postDelayed({
            splashicon.visibility = View.VISIBLE
        }, 500)
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToMain()
        }, 2000)
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        val options = ActivityOptions.makeCustomAnimation(
            this,
            R.anim.fade_in,
            R.anim.fade_out
        )
        startActivity(intent, options.toBundle())
        finish()
    }
}