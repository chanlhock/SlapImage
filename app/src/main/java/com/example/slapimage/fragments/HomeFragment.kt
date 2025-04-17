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
import com.example.slapimage.GalleryActivity
import com.example.slapimage.Icon
import com.example.slapimage.gridiconactivity.MusicPlayerActivity
import com.example.slapimage.PlaylistActivity
import com.example.slapimage.R
import com.example.slapimage.gridiconactivity.GameOfLifeActivity
import com.example.slapimage.gridiconactivity.StockActivity
import com.example.slapimage.gridiconactivity.TextViewerActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {

    private lateinit var iconRecyclerView: RecyclerView
    private val bannerImages: List<Int> = listOf(
        R.drawable.banner1, R.drawable.banner2,
        R.drawable.banner8, R.drawable.banner4, R.drawable.banner5, R.drawable.banner6,
        R.drawable.banner7, R.drawable.banner3, R.drawable.banner9, R.drawable.banner10,
        R.drawable.banner11, R.drawable.banner12, R.drawable.banner13, R.drawable.banner14,
        R.drawable.banner15, R.drawable.banner16, R.drawable.banner17, R.drawable.banner18
    )
    private var currentBannerIndex = 0
    private val handler = Handler(Looper.getMainLooper())
    private val bannerChangeInterval = 3000L
    private lateinit var bannerContainer: ViewGroup
    private lateinit var currentBanner: ImageView
    private lateinit var nextBanner: ImageView

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
        Glide.with(this).load(bannerImages[currentBannerIndex]).into(currentBanner)
        rotateBannerWithAnimation()

        // Initialize icon pager
        iconRecyclerView = view.findViewById(R.id.icon_pager_recycler_view)
        setupIconPager()

        // Initialize buttons
        val myButton: Button = view.findViewById(R.id.appGalleryButton)
        val myMusicButton: Button = view.findViewById(R.id.musicButton)
        val newWidth = 100
        val newHeight = 100
        updateButtonIcon(myButton, R.drawable.icon12, newWidth, newHeight)
        updateButtonIcon(myMusicButton, R.drawable.icon13, newWidth, newHeight)

        // Set button click listeners
        myMusicButton.setOnClickListener {
            val intent = Intent(requireActivity(), PlaylistActivity::class.java)
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

        return view
    }

    private fun setupIconPager() {
        // Create pages of icons
        val iconPages = listOf(
            // First page (original icons)
            listOf(
                Icon(R.drawable.icon1, "Open Photo"),
                Icon(R.drawable.icon2, "Play Video"),
                Icon(R.drawable.icon3, "DeepSeek Bot"),
                Icon(R.drawable.icon4, "Open TextFile"),
                Icon(R.drawable.icon5, "Calendar"),
                Icon(R.drawable.icon6, "Calculator"),
                Icon(R.drawable.icon7, "Game of Life"),
                Icon(R.drawable.icon8, "Stock"),
                Icon(R.drawable.icon13, "Play Music"),
                Icon(R.drawable.icon14, "Coming Soon"),
                Icon(R.drawable.icon14, "Coming Soon"),
                Icon(R.drawable.icon10, "About")
            ),
            // Second page (example additional icons)
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

        iconRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = IconPagerAdapter(iconPages) { icon ->
                handleIconClick(icon)
            }
            PagerSnapHelper().attachToRecyclerView(this)
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
                val intent = Intent(activity, ComingSoonActivity::class.java)
                startActivity(intent)
            }
            "Open TextFile" -> openTextFile()
            "Calendar" -> {
                val intent = Intent(activity, CalendarActivity::class.java)
                startActivity(intent)
            }
            "Calculator" -> {
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
            "About" -> {
                val transaction = parentFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, ProfileFragment())
                transaction.addToBackStack(null)
                transaction.commit()
                requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.nav_profile
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
            Glide.with(this).load(bannerImages[nextIndex]).into(nextBanner)

            val slideOut = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_left).apply {
                duration = 500
            }
            val slideIn = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right).apply {
                duration = 500
            }

            nextBanner.visibility = View.VISIBLE
            nextBanner.bringToFront()

            currentBanner.startAnimation(slideOut)
            nextBanner.startAnimation(slideIn)

            slideOut.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    val temp = currentBanner
                    currentBanner = nextBanner
                    nextBanner = temp
                    currentBannerIndex = nextIndex
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

            fun bind(icon: Icon) {
                iconImage.setImageResource(icon.image)
                iconText.text = icon.text
                itemView.setOnClickListener { onIconClick(icon) }
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