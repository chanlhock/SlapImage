package com.example.slapimage.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.slapimage.R

class ProfileFragment : Fragment() {
    private lateinit var btnQuitProgram: Button
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var ivLogo: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialize constraintLayout
        constraintLayout = view.findViewById(R.id.constraintLayout) // Replace with your actual ID

        // Initialize the ImageView
        ivLogo = view.findViewById(R.id.ivLogo)
        ivLogo.visibility = View.VISIBLE

        // Initialize views
        btnQuitProgram = view.findViewById(R.id.btnQuitProgram) // Initialize the Quit Program button

        // Load GIF from a local resource (e.g., raw or drawable folder)
        val gifResource = R.drawable.animated_logo
        Glide.with(requireContext())
            .asGif()
            .load(gifResource)
            .into(ivLogo)

        // Create the callback
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Explicitly replace ProfileFragment with HomeFragment
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment()) // Replace with your container ID
                    .commit()
            }
        }

        // Register the callback with the correct dispatcher
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )

        // Button to quit the program
        btnQuitProgram.setOnClickListener {
            showExitDialog()
            //requireActivity().finishAffinity() // Close all activities in the app's task
        }

        return view
    }


    private fun showExitDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Exit App")
        builder.setMessage("Are you sure you want to exit?")

        builder.setPositiveButton("Quit") { dialog, which ->
            // User clicked Quit button
            requireActivity().finishAffinity() // Close all activities in the app's task
        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            // User cancelled the dialog
            dialog.dismiss() // Dismiss the dialog
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
        // Access and set text color for the buttons
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(
            ContextCompat.getColor(requireContext(), R.color.dark_blue)
        )
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(
            ContextCompat.getColor(requireContext(), R.color.dark_blue)
        )
    }

    //override fun onDestroy() {
    //    super.onDestroy()
    //}

}