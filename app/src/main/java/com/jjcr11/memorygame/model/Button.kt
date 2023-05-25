package com.jjcr11.memorygame.model

import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.card.MaterialCardView

data class Button(
    val constraintLayout: ConstraintLayout,
    val materialCardView: MaterialCardView,
    val textView: TextView,
    val tag: String,
    val defaultDarkColor: String,
    val defaultLightColor: String
)
