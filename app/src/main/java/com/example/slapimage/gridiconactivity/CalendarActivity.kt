package com.example.slapimage.gridiconactivity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.slapimage.MainActivity
import com.example.slapimage.R

class CalendarActivity : AppCompatActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Lock screen orientation to portrait
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_calendar) // Set the layout for this activity

        // Set up the Toolbar as the ActionBar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Find the TextView by its ID
        val textView = findViewById<TextView>(R.id.toolbar_title)

        // Set the text for the TextView (proper localized version)
        textView.text = getString(R.string.calendar)

        // Enable the Up button (back arrow)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Hide the default title
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val calendarView = findViewById<CalendarView>(R.id.calendarView)

        // Set a listener for date changes
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = "$dayOfMonth/${month + 1}/$year"
            Toast.makeText(this, "Selected Date: $selectedDate", Toast.LENGTH_SHORT).show()
        }

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

}