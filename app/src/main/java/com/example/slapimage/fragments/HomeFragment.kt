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
import android.app.ActivityOptions
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.scale
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.slapimage.BuildConfig
import com.example.slapimage.GeminiAIChatActivity
import com.example.slapimage.Icon
import com.example.slapimage.R
import com.example.slapimage.WallpaperSetterActivity
import com.example.slapimage.gridiconactivity.CalculatorActivity
import com.example.slapimage.gridiconactivity.CalendarActivity
import com.example.slapimage.gridiconactivity.ComingSoonActivity
import com.example.slapimage.gridiconactivity.GalleryActivity
import com.example.slapimage.gridiconactivity.GameOfLifeActivity
import com.example.slapimage.gridiconactivity.MusicPlayerActivity
import com.example.slapimage.gridiconactivity.StockActivity
import com.example.slapimage.mbcompass.MBCompassMainActivity
import com.example.slapimage.mp3tagger.MP3TaggerMainActivity
import com.example.slapimage.musicplayer.MainMusicActivity
import com.example.slapimage.newcalculator.CalcMainActivity
import com.example.slapimage.solitaire_cg.SolitaireCG
import com.example.slapimage.tetris.TetrisActivity
import com.example.slapimage.textpad.activities.EditorActivity
import com.example.slapimage.tictactoe.content.TicTacToeMainActivity
import com.example.slapimage.xededitor.xededitor.MainActivity.XEDMainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.view.GestureDetector
import android.view.MotionEvent
import com.example.slapimage.ibook.foobnix.ui2.MainTabs2

class HomeFragment : Fragment() {

    private lateinit var iconRecyclerView: RecyclerView

 private val bannerImages: List<Int> = when {
     BuildConfig.MINIMAL_BUILDSIZE == true -> listOf(
         R.drawable.banner1, R.drawable.banner2,
         R.drawable.banner3, R.drawable.banner4, R.drawable.banner5, R.drawable.banner6,
         R.drawable.banner7, R.drawable.banner8, R.drawable.banner37, R.drawable.banner10,
         R.drawable.banner12, R.drawable.banner13, R.drawable.banner14
     )
     else -> listOf(
         R.drawable.banner1, R.drawable.banner2,
         R.drawable.banner3, R.drawable.banner4, R.drawable.banner5, R.drawable.banner6,
         R.drawable.banner7, R.drawable.banner8, R.drawable.banner37, R.drawable.banner10,
         R.drawable.banner12, R.drawable.banner13, R.drawable.banner14,
         R.drawable.banner15, R.drawable.banner18,
         R.drawable.banner19, R.drawable.banner23, R.drawable.banner24, R.drawable.banner25,
         R.drawable.banner26, R.drawable.banner30, R.drawable.banner32,
         R.drawable.banner35, R.drawable.banner36, R.drawable.banner9, R.drawable.banner38,
         R.drawable.banner39, R.drawable.banner40,
         R.drawable.banner43, R.drawable.banner44, R.drawable.banner45, R.drawable.banner46,
         R.drawable.banner47, R.drawable.banner48, R.drawable.banner49,
         R.drawable.banner51, R.drawable.banner53, R.drawable.banner54,
         R.drawable.banner55, R.drawable.banner56, R.drawable.banner57, R.drawable.banner58,
         R.drawable.banner60, R.drawable.banner61, R.drawable.banner62,
         R.drawable.banner63, R.drawable.banner65, R.drawable.banner66,
         R.drawable.banner68, R.drawable.banner70, R.drawable.banner71, R.drawable.banner72,
         R.drawable.banner74, R.drawable.banner78
     )
 }
    private var currentBannerIndex = 3
    private val handler = Handler(Looper.getMainLooper())
    private val bannerChangeInterval = 1500L
    private lateinit var bannerContainer: ViewGroup
    private lateinit var currentBanner: ImageView
    private lateinit var nextBanner: ImageView
    private lateinit var pageIndicator: LinearLayout
    private lateinit var gestureDetector: GestureDetector
    private var initialY1: Float = 0f
    private var initialY2: Float = 0f
    private var isTwoFingersDown = false
    private var isFragmentResumed = false
    private var isBannerRotationPaused = false

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        gestureDetector = GestureDetector(requireContext(), object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent): Boolean = true
            override fun onDoubleTap(e: MotionEvent): Boolean {
                toggleBannerRotation()
                return true
            }
        })

        view.setOnTouchListener { v, event ->
            gestureDetector.onTouchEvent(event)

            when (event.actionMasked) {
                MotionEvent.ACTION_POINTER_DOWN -> {
                    if (event.pointerCount == 2) {
                        initialY1 = event.getY(0)
                        initialY2 = event.getY(1)
                        isTwoFingersDown = true
                    }
                }

                MotionEvent.ACTION_MOVE -> {
                    if (isTwoFingersDown && event.pointerCount == 2) {
                        val currentY1 = event.getY(0)
                        val currentY2 = event.getY(1)

                        // Calculate average movement of both fingers
                        val avgDiffY = ((currentY1 - initialY1) + (currentY2 - initialY2)) / 2

                        if (avgDiffY > 100) { // Minimum swipe distance threshold
                            launchTargetActivity()
                            isTwoFingersDown = false
                            return@setOnTouchListener true
                        }
                    }
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                    isTwoFingersDown = false
                    v.performClick() // For accessibility
                }
            }

            true
        }

        // Important for accessibility
        view.isClickable = true
        view.isFocusable = true
        view.isLongClickable = true

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

    private fun launchTargetActivity() {
        val intent = Intent(requireContext(), WallpaperSetterActivity::class.java)
        try {
            ActivityOptionsCompat.makeCustomAnimation(
                requireContext(),
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
                Icon(R.drawable.icon20, "XED-Editor"),
                Icon(R.drawable.icon4, "TextPad"),
                Icon(R.drawable.icon5, "Calendar"),
                Icon(R.drawable.icon6, "Calculator"),
                Icon(R.drawable.icon17, "MP3 TagEdit"),
                Icon(R.drawable.icon8, "Stock"),
                Icon(R.drawable.icon3, "DeepSeek Bot"),
                Icon(R.drawable.icon9, "Gemini AI"),
                Icon(R.drawable.icon22, "Librera"),
                Icon(R.drawable.icon19, "Compass")
            ),
            // Second page (example additional icons)
            listOf(
                Icon(R.drawable.icon11, "Tetris"),
                Icon(R.drawable.icon16, "TicTacToe"),
                Icon(R.drawable.icon18, "SolitaireCG"),
                Icon(R.drawable.icon7, "Game of Life"),
                Icon(R.drawable.icon15, "AI Calc"),
                Icon(R.drawable.icon21, "Wallpaper"),
                Icon(R.drawable.icon13, "Play Music"),
                Icon(R.drawable.icon14, "Coming Soon"),
                Icon(R.drawable.icon14, "Coming Soon"),
                Icon(R.drawable.icon14, "Coming Soon"),
                Icon(R.drawable.icon14, "Coming Soon"),
                Icon(R.drawable.icon10, "About")
            ),
            // Third page (example additional icons)
            listOf(
                Icon(R.drawable.icon14, "Coming Soon"),
                Icon(R.drawable.icon14, "Coming Soon"),
                Icon(R.drawable.icon14, "Coming Soon"),
                Icon(R.drawable.icon14, "Coming Soon"),
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

            "TextPad" -> {
                val intent = Intent(activity, EditorActivity::class.java)
                val options = ActivityOptions.makeCustomAnimation(
                    requireContext(),
                    R.anim.slide_up,
                    R.anim.no_animation
                )
                startActivity(intent, options.toBundle())
            }
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
            "Compass" -> {
                val intent = Intent(activity, MBCompassMainActivity::class.java)
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
            "SolitaireCG" -> {
                val intent = Intent(activity, SolitaireCG::class.java)
                val options = ActivityOptions.makeCustomAnimation(
                    requireContext(),
                    R.anim.slide_up,
                    R.anim.no_animation
                )
                startActivity(intent, options.toBundle())
            }
            "XED-Editor" -> {
                val intent = Intent(activity, XEDMainActivity::class.java)
                val options = ActivityOptions.makeCustomAnimation(
                    requireContext(),
                    R.anim.slide_up,
                    R.anim.no_animation
                )
                startActivity(intent, options.toBundle())
            }
            "Wallpaper" -> {
                val intent = Intent(activity, WallpaperSetterActivity::class.java)
                val options = ActivityOptions.makeCustomAnimation(
                    requireContext(),
                    R.anim.slide_up,
                    R.anim.no_animation
                )
                startActivity(intent, options.toBundle())
            }
            "Librera" -> {
                val intent = Intent(activity, MainTabs2::class.java)
                val options = ActivityOptions.makeCustomAnimation(
                    requireContext(),
                    R.anim.slide_up,
                    R.anim.no_animation
                )
                startActivity(intent, options.toBundle())
            }
        }
    }

    private fun toggleBannerRotation() {
        isBannerRotationPaused = !isBannerRotationPaused
        if (isBannerRotationPaused) {
            handler.removeCallbacksAndMessages(null)    // Stop banner rotation
        }else{
            rotateBannerWithAnimation() // Start banner rotation
        }
    }

    private fun rotateBannerWithAnimation() {
        if (!isFragmentResumed || isBannerRotationPaused) return  // Prevent rotation of banner if paused or fragment is not resumed

        handler.postDelayed({
            if (!isFragmentResumed || isBannerRotationPaused)  return@postDelayed // Check if the fragment is still active before proceeding

            val nextIndex = (currentBannerIndex + 1) % bannerImages.size
            // Load the next banner image
            Glide.with(this).load(bannerImages[nextIndex]).into(nextBanner)

            // Define animations
            val slideOut = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_left).apply {
                duration = 500
            }
            val slideIn = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right).apply {
                duration = 500
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
        isBannerRotationPaused = false
        isFragmentResumed = true
        rotateBannerWithAnimation()
    }

    override fun onPause() {
        super.onPause()
        isFragmentResumed = false
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