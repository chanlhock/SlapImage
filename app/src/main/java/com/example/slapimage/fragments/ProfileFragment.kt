package com.example.slapimage.fragments

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.slapimage.R
import com.example.slapimage.droidzebra.DroidZebra.DialogDonate
import com.example.slapimage.xededitor.xededitor.MainActivity.tabs.editor.getCurrentFragment

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
            //val newFragment: DialogFragment = DialogDonate.newInstance()
            //showDialog(newFragment, "dialog_donate")
           // newFragment.show(requireActivity().getSupportFragmentManager(), "dialog_donate")
            showExitDialog()
            //requireActivity().finishAffinity() // Close all activities in the app's task
        }

        return view
    }


    private fun showExitDialog() {
        val icon = ContextCompat.getDrawable(requireContext(), R.drawable.icon_reversi)
        val scaledIcon = icon?.apply {
            // Set size in pixels (convert dp to px)
            val sizeInPx = (24 * resources.displayMetrics.density).toInt()
            setBounds(0, 0, sizeInPx, sizeInPx)
        }
        val builder = AlertDialog.Builder(requireContext(),R.style.CustomActionBarThemeSlapImage)
        builder.setTitle("Exit SlapImage")
        builder.setMessage(R.string.dialog_donate_message)
        //builder.setMessage("Are you sure you want to exit?")
        builder.setIcon(scaledIcon)
        builder.setNeutralButton("Quit") { dialog, which ->
            // User clicked Quit button
            requireActivity().finishAffinity() // Close all activities in the app's task
        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            // User cancelled the dialog
            dialog.dismiss() // Dismiss the dialog
        }
        builder.setPositiveButton("Buy Me a Coffee") { dialog, which ->
            // Open up Buy Me a Coffee website
            val url = "https://buymeacoffee.com/chanlhock/"
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())

            requireContext().packageManager?.let { pm ->
                if (intent.resolveActivity(pm) != null) {
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "No browser found",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            dialog.dismiss() // Donate
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
        // Access and set text color for the buttons
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(
            ContextCompat.getColor(requireContext(), R.color.white)
        )
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(
            ContextCompat.getColor(requireContext(), R.color.white)
        )
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL)?.setTextColor(
            ContextCompat.getColor(requireContext(), R.color.white)
        )
    }

    //override fun onDestroy() {
    //    super.onDestroy()
    //}

}