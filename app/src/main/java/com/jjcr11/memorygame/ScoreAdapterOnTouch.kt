package com.jjcr11.memorygame

import android.view.MotionEvent
import android.view.View

interface ScoreAdapterOnTouch {
    fun onTouch(view: View, motionEvent: MotionEvent, position: Int): Boolean
}