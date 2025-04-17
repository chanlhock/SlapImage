package com.example.slapimage.gridiconactivity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.slapimage.R

class CalculatorActivity : AppCompatActivity() {

    private lateinit var currentDisplay: TextView
    private lateinit var previousCalculation: TextView
    private var currentNumber = ""
    private var firstOperand = ""
    private var secondOperand = ""
    private var operation = ""
    private var isNewOperation = true

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)
        // Lock screen orientation to portrait
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        currentDisplay = findViewById(R.id.current_display)
        previousCalculation = findViewById(R.id.previous_calculation)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.black)

        setupToolbar()
        setupNumberButtons()
        setupOperationButtons()
        setupClearButton()
        setupEqualsButton()
        setupDecimalButton()
        setupDeleteButton()
    }

    private fun updateCurrentDisplay() {
        currentDisplay.text = if (currentNumber.isEmpty()) getString(R.string.zero) else currentNumber
    }

    private fun updateCalculationPreview() {
        previousCalculation.text = when {
            firstOperand.isNotEmpty() && operation.isNotEmpty() ->
                getString(R.string.calculation_template, firstOperand, operation)
            else -> ""
        }
    }

    private fun showFinalCalculation() {
        previousCalculation.text = getString(
            R.string.full_calculation_template,
            firstOperand,
            operation,
            secondOperand
        )
    }

    private fun setupNumberButtons() {
        listOf(
            R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
            R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9).forEach { id ->
            findViewById<Button>(id).setOnClickListener { v ->
                if (isNewOperation) {
                    currentNumber = ""
                    isNewOperation = false
                }
                currentNumber += (v as Button).text
                updateCurrentDisplay()
                updateCalculationPreview()
            }
        }
    }
    private fun setupOperationButtons() {
        listOf(R.id.btn_add, R.id.btn_subtract, R.id.btn_multiply, R.id.btn_divide).forEach { id ->
            findViewById<Button>(id).setOnClickListener { v ->
                if (currentNumber.isNotEmpty()) {
                    firstOperand = currentNumber
                    operation = (v as Button).text.toString()
                    currentNumber = ""
                    updateCurrentDisplay()
                    updateCalculationPreview()
                }
            }
        }
    }

    private fun setupEqualsButton() {
        findViewById<Button>(R.id.btn_equals).setOnClickListener {
            if (firstOperand.isNotEmpty() && currentNumber.isNotEmpty() && operation.isNotEmpty()) {
                secondOperand = currentNumber
                val result = performOperation(
                    firstOperand.toDouble(),
                    secondOperand.toDouble(),
                    operation
                )
                showFinalCalculation()
                currentNumber = result.toString()
                updateCurrentDisplay()
                isNewOperation = true
            }
        }
    }

    private fun setupClearButton() {
        findViewById<Button>(R.id.btn_clear).setOnClickListener {
            currentNumber = ""
            firstOperand = ""
            secondOperand = ""
            operation = ""
            updateCurrentDisplay()
            previousCalculation.text = ""
            isNewOperation = true
        }
    }

    private fun setupDecimalButton() {
        findViewById<Button>(R.id.btn_decimal).setOnClickListener {
            if (isNewOperation) {
                currentNumber = "0"
                isNewOperation = false
            }
            if (!currentNumber.contains(".")) {
                currentNumber += "."
                updateCurrentDisplay()
            }
        }
    }

    private fun setupDeleteButton() {
        findViewById<Button>(R.id.btn_delete).setOnClickListener {
            if (currentNumber.isNotEmpty()) {
                currentNumber = currentNumber.dropLast(1)
                updateCurrentDisplay()
                isNewOperation = currentNumber.isEmpty()
                if (currentNumber.isEmpty()) {
                    previousCalculation.text = ""
                }
            }
        }
    }

    private fun performOperation(first: Double, second: Double, op: String): Double {
        return when (op) {
            "+" -> first + second
            "-" -> first - second
            "×" -> first * second
            "÷" -> if (second != 0.0) first / second else Double.NaN
            else -> second
        }
    }


    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.calculator)
        }
        toolbar.setNavigationOnClickListener { finish() }
    }

}