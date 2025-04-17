package com.example.slapimage

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.VideoView

class AccessibleVideoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : VideoView(context, attrs, defStyleAttr) {

    override fun performClick(): Boolean {
        super.performClick()
        // Handle the click action
        return true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                performClick()
                return true
            }
        }
        return super.onTouchEvent(event)
    }
}