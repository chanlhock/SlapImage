package com.example.slapimage.utils

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.text.style.ReplacementSpan

class CodeSpan : ReplacementSpan() {
    override fun getSize(
        paint: Paint,
        text: CharSequence,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int = paint.measureText(text, start, end).toInt()

    override fun draw(
        canvas: Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        // Only draw if we have valid text
        if (start < end) {
            val textWidth = paint.measureText(text, start, end)
            val rect = RectF(x, top.toFloat(), x + textWidth, bottom.toFloat())

            paint.color = 0xFFF5F5F5.toInt()
            canvas.drawRoundRect(rect, 8f, 8f, paint)

            paint.color = 0xFF333333.toInt()
            canvas.drawText(text, start, end, x, y.toFloat(), paint)
        }
    }
}