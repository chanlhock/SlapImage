package com.example.slapimage
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.slapimage.fragments.ChatBotFragment
import com.example.slapimage.fragments.HomeFragment
import com.example.slapimage.fragments.PhotoFragment
import com.example.slapimage.fragments.PlayFragment
import com.example.slapimage.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class PlaceholderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placeholder) // Set the layout for this
        // Initialize the BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        // Set label visibility mode
        //bottomNavigationView.labelVisibilityMode = BottomNavigationView.LABEL_VISIBILITY_LABELED

        // Apply the ColorStateList to the icon tints
        bottomNavigationView.itemIconTintList = ContextCompat.getColorStateList(this, R.color.bottom_nav_icon_color)
        bottomNavigationView.itemTextColor = ContextCompat.getColorStateList(this,R.color.bottom_nav_icon_color)
        // Ensure all icons are always displayed
        bottomNavigationView.labelVisibilityMode = BottomNavigationView.LABEL_VISIBILITY_LABELED

        // Initialize constraintLayout
        //constraintLayout = findViewById(R.id.constraintLayout) // Replace with your actual ID
        // Set a listener for item selection
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Navigate to HomeFragment
                    val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.fragment_container, HomeFragment())
                    fragmentTransaction.commit()
                    true
                }
                R.id.nav_search -> {
                    // Navigate to HomeFragment
                    val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.fragment_container, PhotoFragment())
                    fragmentTransaction.commit()
                    true
                }
                R.id.nav_play -> {
                    // Navigate to HomeFragment
                    val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.fragment_container, PlayFragment())
                    fragmentTransaction.commit()
                    true
                }
                R.id.nav_chatbot -> {
                    // Navigate to HomeFragment
                    val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.fragment_container, ChatBotFragment())
                    fragmentTransaction.commit()
                    true
                }
                R.id.nav_profile -> {
                    // Navigate to HomeFragment
                    val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.fragment_container, ProfileFragment())
                    fragmentTransaction.commit()
                    true
                }
                else -> false
            }
        }

        Log.d("CalendarActivity", "Starting fragment transaction")

        // Replace the HomeFragment container with your PhotoFragment
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, PhotoFragment()) // Replace with your fragment
        fragmentTransaction.commit()
        // Set the Photo button as selected
        bottomNavigationView.selectedItemId = R.id.nav_search
        // Handle fragment back stack (optional)
        supportFragmentManager.addOnBackStackChangedListener {
            // Update the selected item based on the current fragment
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
            when (currentFragment) {
                is HomeFragment -> bottomNavigationView.selectedItemId = R.id.nav_home
                is PhotoFragment -> bottomNavigationView.selectedItemId = R.id.nav_search
                is PlayFragment -> bottomNavigationView.selectedItemId = R.id.nav_play
                is ChatBotFragment -> bottomNavigationView.selectedItemId = R.id.nav_chatbot
                is ProfileFragment -> bottomNavigationView.selectedItemId = R.id.nav_profile
            }
        }

        Log.d("PhotoActivity", "Fragment transaction committed")
        return
    }
}