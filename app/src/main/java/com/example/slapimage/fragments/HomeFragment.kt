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
package com.example.slapimage.fragments

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import androidx.core.graphics.scale
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.slapimage.gridiconactivity.CalculatorActivity
import com.example.slapimage.gridiconactivity.CalendarActivity
import com.example.slapimage.gridiconactivity.ComingSoonActivity
import com.example.slapimage.gridiconactivity.GalleryActivity
import com.example.slapimage.Icon
import com.example.slapimage.gridiconactivity.MusicPlayerActivity
import com.example.slapimage.R
import com.example.slapimage.gridiconactivity.GameOfLifeActivity
import com.example.slapimage.gridiconactivity.StockActivity
import com.example.slapimage.gridiconactivity.TextViewerActivity
import com.example.slapimage.GeminiAIChatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import android.app.ActivityOptions
import androidx.activity.OnBackPressedCallback
import com.example.slapimage.BuildConfig
import com.example.slapimage.mp3tagger.MP3TaggerMainActivity
import com.example.slapimage.musicplayer.MainMusicActivity
import com.example.slapimage.tetris.TetrisActivity
import com.example.slapimage.newcalculator.CalcMainActivity
import com.example.slapimage.tictactoe.content.TicTacToeMainActivity
//import com.github.chrisbanes.photoview.BuildConfig

class HomeFragment : Fragment() {

    private lateinit var iconRecyclerView: RecyclerView

 private val bannerImages: List<Int> = when {
     BuildConfig.MINIMAL_BUILDSIZE == true -> listOf(
         R.drawable.banner1, R.drawable.banner2,
         R.drawable.banner3, R.drawable.banner4, R.drawable.banner5, R.drawable.banner6,
         R.drawable.banner7, R.drawable.banner8, R.drawable.banner9, R.drawable.banner10,
         R.drawable.banner12, R.drawable.banner13, R.drawable.banner14
     )
     else -> listOf(
         R.drawable.banner1, R.drawable.banner2,
         R.drawable.banner3, R.drawable.banner4, R.drawable.banner5, R.drawable.banner6,
         R.drawable.banner7, R.drawable.banner8, R.drawable.banner9, R.drawable.banner10,
         R.drawable.banner12, R.drawable.banner13, R.drawable.banner14,
         R.drawable.banner15, R.drawable.banner16, R.drawable.banner17, R.drawable.banner18,
         R.drawable.banner19, R.drawable.banner20, R.drawable.banner21, R.drawable.banner22,
         R.drawable.banner23, R.drawable.banner24, R.drawable.banner25, R.drawable.banner26,
         R.drawable.banner27, R.drawable.banner28, R.drawable.banner29, R.drawable.banner30,
         R.drawable.banner32, R.drawable.banner33, R.drawable.banner34,
         R.drawable.banner35, R.drawable.banner36, R.drawable.banner37, R.drawable.banner38,
         R.drawable.banner39, R.drawable.banner40, R.drawable.banner41, R.drawable.banner42,
         R.drawable.banner43, R.drawable.banner44, R.drawable.banner45, R.drawable.banner46,
         R.drawable.banner47, R.drawable.banner48, R.drawable.banner49, R.drawable.banner50,
         R.drawable.banner51, R.drawable.banner52, R.drawable.banner53, R.drawable.banner54,
         R.drawable.banner55, R.drawable.banner56, R.drawable.banner57, R.drawable.banner58,
         R.drawable.banner59, R.drawable.banner60, R.drawable.banner61, R.drawable.banner62,
         R.drawable.banner63, R.drawable.banner64, R.drawable.banner65, R.drawable.banner66,
         R.drawable.banner67, R.drawable.banner68, R.drawable.banner69, R.drawable.banner70,
         R.drawable.banner71, R.drawable.banner72, R.drawable.banner73, R.drawable.banner74,
         R.drawable.banner75, R.drawable.banner76, R.drawable.banner77, R.drawable.banner78,
         R.drawable.banner79, R.drawable.banner80, R.drawable.banner84, R.drawable.banner85
     )
 }
    private var currentBannerIndex = 3
    private val handler = Handler(Looper.getMainLooper())
    private val bannerChangeInterval = 1500L
    private lateinit var bannerContainer: ViewGroup
    private lateinit var currentBanner: ImageView
    private lateinit var nextBanner: ImageView
    private lateinit var pageIndicator: LinearLayout

    private val openTextFileLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let { fileUri ->
            try {
                requireContext().contentResolver.takePersistableUriPermission(
                    fileUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                val intent = Intent(requireActivity(), TextViewerActivity::class.java).apply {
                    data = fileUri
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                startActivity(intent)
            } catch (e: SecurityException) {
                Log.e("HomeFragment", "Error taking permission", e)
                Toast.makeText(requireContext(), "Couldn't access file", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Initialize banner views
        bannerContainer = view.findViewById(R.id.banner_container)
        currentBanner = view.findViewById(R.id.banner)
        nextBanner = ImageView(requireContext()).apply {
            layoutParams = currentBanner.layoutParams
            scaleType = currentBanner.scaleType
            visibility = View.INVISIBLE
        }
        bannerContainer.addView(nextBanner)

        // Clear Glide memory cache when the fragment resumes
        Glide.get(requireContext()).clearMemory()
        Thread {
            Glide.get(requireContext()).clearDiskCache()
        }.start()
        Glide.with(this).load(bannerImages[currentBannerIndex]).into(currentBanner)
        rotateBannerWithAnimation()

        pageIndicator = view.findViewById(R.id.pageIndicator)
        // Initialize icon pager
        iconRecyclerView = view.findViewById(R.id.icon_pager_recycler_view)
        setupIconPager()

        // Initialize buttons
        val myButton: Button = view.findViewById(R.id.appGalleryButton)
        val myMusicButton: Button = view.findViewById(R.id.musicButton)

        val newWidth = 150
        val newHeight = 150
        updateButtonIcon(myButton, R.drawable.icon12, newWidth, newHeight)
        updateButtonIcon(myMusicButton, R.drawable.icon13, newWidth, newHeight)

        // Set button click listeners
        myMusicButton.setOnClickListener {
            val intent = Intent(requireActivity(), MainMusicActivity::class.java)
            ActivityOptionsCompat.makeCustomAnimation(
                requireContext(),
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            startActivity(intent)
        }

        myButton.setOnClickListener {
            val intent = Intent(requireContext(), GalleryActivity::class.java)
            ActivityOptionsCompat.makeCustomAnimation(
                requireContext(),
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            startActivity(intent)
        }

        // Create the callback
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Navigate back to HomeFragment
                //parentFragmentManager.popBackStack()
            }
        }

        // Register the callback with the correct dispatcher
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )

        return view
    }

    private fun setupPageIndicator(pageCount: Int) {
        pageIndicator.removeAllViews() // This clears any existing indicators

        for (i in 0 until pageCount) {
            val circle = ImageView(requireContext()).apply {
                setImageResource(R.drawable.indicator_circle)
                layoutParams = LinearLayout.LayoutParams(
                    resources.getDimensionPixelSize(R.dimen.indicator_size),
                    resources.getDimensionPixelSize(R.dimen.indicator_size)
                ).apply {
                    setMargins(
                        resources.getDimensionPixelSize(R.dimen.indicator_margin),
                        0,
                        resources.getDimensionPixelSize(R.dimen.indicator_margin),
                        0
                    )
                }
            }
            pageIndicator.addView(circle)
        }
        updateIndicator(0)
    }

    private fun updateIndicator(position: Int) {
        for (i in 0 until pageIndicator.childCount) {
            val circle = pageIndicator.getChildAt(i) as ImageView
            circle.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    if (i == position) R.color.indicator_active else R.color.indicator_inactive
                )
            )
        }
    }

    private fun setupIconPager() {
        // Create pages of icons
        val iconPages = listOf(
            // First page (original icons)
            listOf(
                Icon(R.drawable.icon1, "Open Photo"),
                Icon(R.drawable.icon2, "Play Video"),
                Icon(R.drawable.icon13, "Play Music"),
                Icon(R.drawable.icon4, "Open TextFile"),
                Icon(R.drawable.icon5, "Calendar"),
                Icon(R.drawable.icon6, "Calculator"),
                Icon(R.drawable.icon7, "Game of Life"),
                Icon(R.drawable.icon8, "Stock"),
                Icon(R.drawable.icon3, "DeepSeek Bot"),
                Icon(R.drawable.icon9, "Gemini AI"),
                Icon(R.drawable.icon17,"MP3 TagEdit"),
                Icon(R.drawable.icon10, "About")
            ),
            // Second page (example additional icons)
            listOf(
                Icon(R.drawable.icon11, "Tetris"),
                Icon(R.drawable.icon16, "TicTacToe"),
                Icon(R.drawable.icon14, "Coming Soon"),
                Icon(R.drawable.icon15, "AI Calc"),
                Icon(R.drawable.icon14, "Coming Soon"),
                Icon(R.drawable.icon14, "Coming Soon"),
                Icon(R.drawable.icon14, "Coming Soon"),
                Icon(R.drawable.icon14, "Coming Soon"),
                Icon(R.drawable.icon14, "Coming Soon"),
                Icon(R.drawable.icon14, "Coming Soon"),
                Icon(R.drawable.icon14, "Coming Soon"),
                Icon(R.drawable.icon10, "About")
            )
        )

        setupPageIndicator(iconPages.size)

        iconRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = IconPagerAdapter(iconPages) { icon ->
                handleIconClick(icon)
            }
            PagerSnapHelper().attachToRecyclerView(this)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
                    if (firstVisibleItem != RecyclerView.NO_POSITION) {
                        updateIndicator(firstVisibleItem)
                    }
                }
            })
        }
    }

    private fun handleIconClick(icon: Icon) {
        when (icon.text) {
            "Open Photo" -> {
                val transaction = parentFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, PhotoFragment())
                transaction.addToBackStack(null)
                transaction.commit()
                requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.nav_search
            }
            "Play Video" -> {
                val transaction = parentFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, PlayFragment())
                transaction.addToBackStack(null)
                transaction.commit()
                requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.nav_play
            }
            "DeepSeek Bot" -> {
                val transaction = parentFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, ChatBotFragment())
                transaction.addToBackStack(null)
                transaction.commit()
                requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.nav_chatbot
            }
            "Open TextFile" -> openTextFile()
            "Calendar" -> {
                val intent = Intent(activity, CalendarActivity::class.java)
                startActivity(intent)
            }
            "AI Calc" -> {
                val intent = Intent(activity, CalculatorActivity::class.java)
                startActivity(intent)
            }
            "Game of Life" -> {
                val intent = Intent(activity, GameOfLifeActivity::class.java)
                startActivity(intent)
            }
            "Stock" -> {
                val intent = Intent(activity, StockActivity::class.java)
                startActivity(intent)
            }
            "Play Music" -> {
                val intent = Intent(activity, MusicPlayerActivity::class.java)
                startActivity(intent)
            }
            "Coming Soon" -> {
                val intent = Intent(activity, ComingSoonActivity::class.java)
                startActivity(intent)
            }
            "Gemini AI" -> {
                val intent = Intent(activity, GeminiAIChatActivity::class.java)
                val options = ActivityOptions.makeCustomAnimation(
                    requireContext(),
                    R.anim.slide_up,
                    R.anim.no_animation
                )
                startActivity(intent, options.toBundle())
            }
            "About" -> {
                val transaction = parentFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, ProfileFragment())
                transaction.addToBackStack(null)
                transaction.commit()
                requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.nav_profile
            }
            "Tetris" -> {
                val intent = Intent(activity, TetrisActivity::class.java)
                val options = ActivityOptions.makeCustomAnimation(
                    requireContext(),
                    R.anim.slide_up,
                    R.anim.no_animation
                )
                startActivity(intent, options.toBundle())
            }
            "Calculator" -> {
                val intent = Intent(activity, CalcMainActivity::class.java)
                val options = ActivityOptions.makeCustomAnimation(
                    requireContext(),
                    R.anim.slide_up,
                    R.anim.no_animation
                )
                startActivity(intent, options.toBundle())
            }
            "TicTacToe" -> {
                val intent = Intent(activity, TicTacToeMainActivity::class.java)
                val options = ActivityOptions.makeCustomAnimation(
                    requireContext(),
                    R.anim.slide_up,
                    R.anim.no_animation
                )
                startActivity(intent, options.toBundle())
            }
            "MP3 TagEdit" -> {
                val intent = Intent(activity, MP3TaggerMainActivity::class.java)
                val options = ActivityOptions.makeCustomAnimation(
                    requireContext(),
                    R.anim.slide_up,
                    R.anim.no_animation
                )
                startActivity(intent, options.toBundle())
            }
        }
    }

    private fun openTextFile() {
        try {
            openTextFileLauncher.launch(arrayOf("text/plain"))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                requireContext(),
                "No app can handle this request: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun rotateBannerWithAnimation() {
        handler.postDelayed({
            val nextIndex = (currentBannerIndex + 1) % bannerImages.size
            // Load the next banner image
            Glide.with(this).load(bannerImages[nextIndex]).into(nextBanner)

            // Define animations
            val slideOut = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_left).apply {
                duration = 300
            }
            val slideIn = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right).apply {
                duration = 300
            }

            // Set next banner visibility and animation
            nextBanner.visibility = View.VISIBLE
            nextBanner.bringToFront()

            // Start animations
            currentBanner.startAnimation(slideOut)
            nextBanner.startAnimation(slideIn)

            slideOut.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    currentBannerIndex = nextIndex
                    // Swap banners
                    val temp = currentBanner
                    currentBanner = nextBanner
                    nextBanner = temp

                    // Continue rotating banners
                    rotateBannerWithAnimation()
                }
            })
        }, bannerChangeInterval)
    }

    private fun updateButtonIcon(button: Button, iconResourceId: Int, newWidth: Int, newHeight: Int) {
        val originalBitmap = BitmapFactory.decodeResource(requireContext().resources, iconResourceId)
        val resizedBitmap = originalBitmap.scale(newWidth, newHeight, true)
        val resizedDrawable = BitmapDrawable(requireContext().resources, resizedBitmap)
        button.setCompoundDrawablesWithIntrinsicBounds(resizedDrawable, null, null, null)
        originalBitmap.recycle()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onResume() {
        super.onResume()

        // Clear Glide memory cache when the fragment resumes
        //Glide.get(requireContext()).clearMemory()
        //Thread {
        //    Glide.get(requireContext()).clearDiskCache()
        //}.start()
        rotateBannerWithAnimation()
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
    }

    private inner class IconPagerAdapter(
        private val pages: List<List<Icon>>,
        private val onIconClick: (Icon) -> Unit
    ) : RecyclerView.Adapter<IconPagerAdapter.IconPageViewHolder>() {

        inner class IconPageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val gridRecyclerView: RecyclerView = itemView.findViewById(R.id.icon_grid_recycler_view)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconPageViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_icon_page, parent, false)
            return IconPageViewHolder(view)
        }

        override fun onBindViewHolder(holder: IconPageViewHolder, position: Int) {
            val icons = pages[position]
            holder.gridRecyclerView.apply {
                layoutManager = GridLayoutManager(context, 4)
                adapter = IconGridAdapter(icons, onIconClick)
            }
        }

        override fun getItemCount(): Int = pages.size
    }

    private inner class IconGridAdapter(
        private val icons: List<Icon>,
        private val onIconClick: (Icon) -> Unit
    ) : RecyclerView.Adapter<IconGridAdapter.IconViewHolder>() {

        inner class IconViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val iconImage: ImageView = itemView.findViewById(R.id.icon_image)
            private val iconText: TextView = itemView.findViewById(R.id.icon_text)
            //private val container: View = itemView.findViewById(R.id.icon_container)
            fun bind(icon: Icon) {
                iconImage.setImageResource(icon.image)
                iconText.text = icon.text
                itemView.setOnClickListener { onIconClick(icon) }
                //container.setBackgroundResource(R.drawable.icon_item_background)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_icon, parent, false)
            return IconViewHolder(view)
        }

        override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
            holder.bind(icons[position])
        }

        override fun getItemCount(): Int = icons.size
    }
}