package com.example.slapimage.gridiconactivity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.slapimage.MainActivity
import com.example.slapimage.R

class ComingSoonActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comingsoon) // Set the layout for this activity

        // Set up the Toolbar as the ActionBar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Enable the Up button (back arrow)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Hide the default title
        supportActionBar?.setDisplayShowTitleEnabled(false)

}

    // Handle the Up button click
    override fun onSupportNavigateUp(): Boolean {
        // Navigate back to MainActivity (which hosts HomeFragment)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Close the current sub-activity
        return true
    }

}