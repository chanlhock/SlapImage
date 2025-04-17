package com.example.slapimage.gridiconactivity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.slapimage.R
import com.example.slapimage.gridiconactivity.SimulationActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.slider.Slider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText

// Add this enum class at the top of your file
enum class InitialPattern { RANDOM, EMPTY, GLIDER }

class GameOfLifeActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var widthInput: TextInputEditText
    private lateinit var heightInput: TextInputEditText
    private lateinit var liveColorPreview: View
    private lateinit var deadColorPreview: View
    private lateinit var chooseLiveColorButton: Button
    private lateinit var chooseDeadColorButton: Button
    private lateinit var speedSlider: Slider
    private val wrapAroundSwitch: SwitchMaterial by lazy {
        findViewById(R.id.wrapAroundSwitch)
    }
    private lateinit var patternToggleGroup: MaterialButtonToggleGroup
    private lateinit var startButton: MaterialButton
    private var selectedPattern: InitialPattern = InitialPattern.RANDOM

    private var liveCellColor = Color.parseColor("#FF03DAC5")
    private var deadCellColor = Color.WHITE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Hide the action bar before setting content view
        supportActionBar?.hide()
        setContentView(R.layout.activity_game_of_life)

        wrapAroundSwitch.isChecked = false  // Wrap-around off by default
        // Initialize views
        toolbar = findViewById(R.id.toolbar)
        widthInput = findViewById(R.id.widthInput)
        heightInput = findViewById(R.id.heightInput)
        liveColorPreview = findViewById(R.id.liveColorPreview)
        deadColorPreview = findViewById(R.id.deadColorPreview)
        chooseLiveColorButton = findViewById(R.id.chooseLiveColorButton)
        chooseDeadColorButton = findViewById(R.id.chooseDeadColorButton)
        speedSlider = findViewById(R.id.speedSlider)
        patternToggleGroup = findViewById(R.id.patternToggleGroup)
        startButton = findViewById(R.id.startButton)

        toolbar.setNavigationOnClickListener { finish() }

        // Set initial color previews
        liveColorPreview.setBackgroundColor(liveCellColor)
        deadColorPreview.setBackgroundColor(deadCellColor)

        // Color pickers
        chooseLiveColorButton.setOnClickListener {
            liveCellColor = listOf(
                ContextCompat.getColor(this, R.color.teal_200),
                ContextCompat.getColor(this, R.color.purple_500),
                ContextCompat.getColor(this, R.color.red_500),
                ContextCompat.getColor(this, R.color.green_500)
            ).random()
            liveColorPreview.setBackgroundColor(liveCellColor)
        }

        chooseDeadColorButton.setOnClickListener {
            deadCellColor = listOf(Color.WHITE, Color.BLACK, Color.GRAY, Color.LTGRAY).random()
            deadColorPreview.setBackgroundColor(deadCellColor)
        }

        // Start simulation
        startButton.setOnClickListener {
            val width = widthInput.text?.toString()?.toIntOrNull()?.coerceIn(1, 100) ?: 0
            val height = heightInput.text?.toString()?.toIntOrNull()?.coerceIn(1, 100) ?: 0

            if (width <= 0 || height <= 0) {
                Toast.makeText(this, R.string.invalid_dimensions, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            startActivity(Intent(this, SimulationActivity::class.java).apply {
                putExtra("width", width)
                putExtra("height", height)
                putExtra("liveColor", liveCellColor)
                putExtra("deadColor", deadCellColor)
                putExtra("gridColor", Color.DKGRAY)
                putExtra("speed", (1100 - speedSlider.value * 100).toLong())
                putExtra("pattern", selectedPattern.name) // Add pattern selection
                putExtra("wrapAround", wrapAroundSwitch.isChecked)  // Critical addition
            })
        }

        // Pattern selection - MODIFY THIS LISTENER
        patternToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                selectedPattern = when (checkedId) {
                    R.id.randomButton -> {
                        Toast.makeText(this, R.string.random_pattern_selected, Toast.LENGTH_SHORT).show()
                        InitialPattern.RANDOM
                    }
                    R.id.emptyButton -> {
                        Toast.makeText(this, R.string.empty_pattern_selected, Toast.LENGTH_SHORT).show()
                        InitialPattern.EMPTY
                    }
                    R.id.gliderButton -> {
                        Toast.makeText(this, R.string.glider_pattern_selected, Toast.LENGTH_SHORT).show()
                        InitialPattern.GLIDER
                    }
                    else -> InitialPattern.RANDOM
                }
            }
        }
    }
}