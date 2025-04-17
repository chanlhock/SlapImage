package com.example.slapimage

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.min
import kotlin.random.Random

class GameOfLifeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    internal var cellSize = 20f
        private set
    private var liveColor = Color.parseColor("#FF03DAC5")
    private var deadColor = Color.BLACK
    private var gridColor = Color.DKGRAY
    private var showGrid = true
    private var wrapAround = false // Add this property

    var gridWidth = 30
        private set
    var gridHeight = 30
        private set
    private var currentGrid = Array(gridWidth) { BooleanArray(gridHeight) }
    private var nextGrid = Array(gridWidth) { BooleanArray(gridHeight) }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = gridColor
        style = Paint.Style.STROKE
        strokeWidth = 1f
    }

    fun setGridSize(width: Int, height: Int) {
        require(width > 0 && height > 0) { "Grid dimensions must be positive" }
        gridWidth = width.coerceAtMost(1000)  // Safety limit
        gridHeight = height.coerceAtMost(1000)
        currentGrid = Array(gridWidth) { BooleanArray(gridHeight) }
        nextGrid = Array(gridWidth) { BooleanArray(gridHeight) }
        requestLayout()
        invalidate()
    }

    fun setColors(live: Int, dead: Int, grid: Int) {
        liveColor = live
        deadColor = dead
        gridColor = grid
        gridPaint.color = gridColor
        invalidate()
    }

    fun randomizeGrid(density: Double = 0.2) {
        currentGrid = Array(gridWidth) {
            BooleanArray(gridHeight) { Random.Default.nextDouble() < density }
        }
        invalidate()
    }

    fun clearGrid() {
        currentGrid = Array(gridWidth) { BooleanArray(gridHeight) }
        invalidate()
    }

    fun setCellAlive(x: Int, y: Int, alive: Boolean = true) {
        if (x in 0 until gridWidth && y in 0 until gridHeight) {
            currentGrid[x][y] = alive
            invalidate()
        } else {
            Log.w("GameOfLifeView", "Attempted to set cell at ($x, $y) which is out of bounds")
        }
    }

    /**
     * @param birthRules    Set of neighbor counts that cause a dead cell to become alive.
     *                     Default: [3] (Conway's Game of Life standard).
     * @param survivalRules Set of neighbor counts that allow a live cell to survive.
     *                     Default: [2, 3] (Conway's Game of Life standard).
     */
    fun nextGeneration(birthRules: Set<Int> = setOf(3), survivalRules: Set<Int> = setOf(2, 3)) {
        require(birthRules.all { it in 0..8 }) { "Birth rules must be 0-8" }
        require(survivalRules.all { it in 0..8 }) { "Survival rules must be 0-8" }
        nextGrid = Array(gridWidth) { x ->
            BooleanArray(gridHeight) { y ->
                val neighbors = countLiveNeighbors(x, y)
                when {
                    !currentGrid[x][y] && neighbors in birthRules -> true  // Birth
                    currentGrid[x][y] && neighbors in survivalRules -> true  // Survival
                    else -> false  // Death
                }
            }
        }
        currentGrid = nextGrid
        invalidate()
    }

    /**
     * Sets whether the grid should wrap around at edges (toroidal mode)
     * @param wrapAround true to enable wrap-around, false for finite grid
     */
    fun setWrapAround(wrapAround: Boolean) {
        this.wrapAround = wrapAround
        invalidate() // Redraw if needed
    }

    private val neighborOffsets = (-1..1).flatMap { i ->
        (-1..1).map { j -> i to j }
    }.filterNot { (i, j) -> i == 0 && j == 0 }

    // Modify your countLiveNeighbors function to use the wrapAround setting
    private fun countLiveNeighbors(x: Int, y: Int): Int =
        neighborOffsets.count { (i, j) ->
            val neighborX = if (wrapAround) (x + i + gridWidth) % gridWidth else x + i
            val neighborY = if (wrapAround) (y + j + gridHeight) % gridHeight else y + j

            neighborX in 0 until gridWidth &&
                    neighborY in 0 until gridHeight &&
                    currentGrid[neighborX][neighborY]
        }

    fun  addGlider(centerX: Int, centerY: Int) {
        clearGrid()
        setCellAlive(centerX + 1, centerY)
        setCellAlive(centerX + 2, centerY + 1)
        setCellAlive(centerX, centerY + 2)
        setCellAlive(centerX + 1, centerY + 2)
        setCellAlive(centerX + 2, centerY + 2)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        cellSize = min(width.toFloat() / gridWidth, height.toFloat() / gridHeight)
    }

    override fun onDraw(canvas: Canvas) {
        // Draw cells
        currentGrid.forEachIndexed { x, row ->
            row.forEachIndexed { y, alive ->
                paint.color = if (alive) liveColor else deadColor
                canvas.drawRect(
                    x * cellSize, y * cellSize,
                    (x + 1) * cellSize, (y + 1) * cellSize,
                    paint
                )
            }
        }

        // Draw grid
        if (showGrid) {
            (0..gridWidth).forEach { i ->
                canvas.drawLine(i * cellSize, 0f, i * cellSize, height.toFloat(), gridPaint)
            }
            (0..gridHeight).forEach { j ->
                canvas.drawLine(0f, j * cellSize, width.toFloat(), j * cellSize, gridPaint)
            }
        }
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}