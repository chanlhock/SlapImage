package com.example.slapimage.gridiconactivity

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.example.slapimage.R
import com.example.slapimage.databinding.ActivitySimulationBinding

class SimulationActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySimulationBinding
    private var isRunning = false
    private var speed = 500L
    private var isEditMode = false
    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            if (isRunning) {
                binding.gameView.nextGeneration()
                handler.postDelayed(this, speed)
            }
        }
    }

    enum class InitialPattern { RANDOM, EMPTY, GLIDER }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivitySimulationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val wrapAround = intent.getBooleanExtra("wrapAround", false)  // Default false

        with(binding) {
            toolbar.setNavigationOnClickListener { finish() }

            // Initialize game parameters
            val width = intent.getIntExtra("width", 30)
            val height = intent.getIntExtra("height", 30)
            gameView.setGridSize(width, height)
            gameView.setColors(
                intent.getIntExtra("liveColor", Color.parseColor("#FF03DAC5")),
                intent.getIntExtra("deadColor", Color.BLACK),
                intent.getIntExtra("gridColor", Color.DKGRAY)
            )
            speed = intent.getLongExtra("speed", 500L)
            gameView.setWrapAround(wrapAround)  // Add this method to GameOfLifeView

            // Enable touch drawing when pattern is EMPTY
            gameView.setOnTouchListener { v, event ->
                if (isEditMode) {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            val cellSize = gameView.cellSize
                            val x = (event.x / cellSize).toInt()
                            val y = (event.y / cellSize).toInt()
                            gameView.setCellAlive(x, y, true)
                            v.performClick()  // Required for accessibility
                            true
                        }
                        else -> false
                    }
                } else {
                    false
                }
            }

            // Button controls
            pauseButton.setOnClickListener {
                isRunning = !isRunning
                pauseButton.text = getString(if (isRunning) R.string.pause else R.string.resume)
                if (isRunning) handler.post(updateRunnable)
            }

            stepButton.setOnClickListener {
                isRunning = false
                pauseButton.text = getString(R.string.resume)
                gameView.nextGeneration()
            }

            resetButton.setOnClickListener {
                isRunning = false
                pauseButton.text = getString(R.string.resume)
                handler.removeCallbacks(updateRunnable)
                applyInitialPattern(
                    try {
                        InitialPattern.valueOf(
                            intent.getStringExtra("pattern") ?: InitialPattern.RANDOM.name
                        )
                    } catch (e: IllegalArgumentException) {
                        InitialPattern.RANDOM
                    }
                )
            }

            // Apply initial pattern
            val initialPattern = try {
                InitialPattern.valueOf(
                    intent.getStringExtra("pattern") ?: InitialPattern.RANDOM.name
                )
            } catch (e: IllegalArgumentException) {
                InitialPattern.RANDOM
            }
            applyInitialPattern(initialPattern)

            // Enable edit mode if pattern is empty
            isEditMode = initialPattern == InitialPattern.EMPTY
        }
    }

    private fun applyInitialPattern(pattern: InitialPattern) {
        with(binding.gameView) {
            when (pattern) {
                InitialPattern.RANDOM -> randomizeGrid()
                InitialPattern.EMPTY -> {
                    clearGrid()
                    isEditMode = true
                }
                InitialPattern.GLIDER -> {
                    // Use the addGlider function instead of manual placement
                    addGlider(gridWidth / 2, gridHeight / 2)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isEditMode) {  // Only auto-start if not in edit mode
            isRunning = true
            binding.pauseButton.text = getString(R.string.pause)
            handler.post(updateRunnable)
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(updateRunnable)
        isRunning = false
    }
}