package com.example.slapimage.gridiconactivity

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

enum class SwipeDirection { LEFT, RIGHT }

fun View.addSwipeListener(
    context: Context,
    onSwipe: (SwipeDirection) -> Unit
) {
    val gestureDetector =
        GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            private val swipeThreshold = 100
            private val swipeVelocityThreshold = 100

            override fun onDown(e: MotionEvent) = true

            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                val direction = when {
                    e1 == null -> null
                    abs(e2.x - e1.x) > abs(e2.y - e1.y) &&
                            abs(e2.x - e1.x) > swipeThreshold &&
                            abs(velocityX) > swipeVelocityThreshold ->
                        if (e2.x > e1.x) SwipeDirection.RIGHT else SwipeDirection.LEFT

                    else -> null
                }
                direction?.let(onSwipe)
                return true
            }
        })

    setOnTouchListener { _, event -> gestureDetector.onTouchEvent(event) }
}