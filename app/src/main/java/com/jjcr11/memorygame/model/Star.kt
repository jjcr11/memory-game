package com.jjcr11.memorygame.model

import android.animation.ObjectAnimator
import android.widget.ImageView

class Star(
    var view: ImageView,
    private val animationDuration: Long
) {
    private val alphaIn: ObjectAnimator = ObjectAnimator.ofFloat(
        view,
        "alpha",
        1f
    ).apply {
        duration = animationDuration
        repeatCount = ObjectAnimator.INFINITE
    }

    private val alphaOut: ObjectAnimator = ObjectAnimator.ofFloat(
        view,
        "alpha",
        0f
    ).apply {
        duration = 0
        startDelay = animationDuration
        repeatCount = ObjectAnimator.INFINITE
    }

    private val translation: ObjectAnimator = ObjectAnimator.ofFloat(
        view,
        "translationY",
        (200..500).random().toFloat()
    ).apply {
        duration = animationDuration
        repeatCount = ObjectAnimator.INFINITE
    }

    private val rotation: ObjectAnimator = ObjectAnimator.ofFloat(
        view,
        "rotation",
        getAngle()
    ).apply {
        duration = animationDuration
        repeatCount = ObjectAnimator.INFINITE
    }

    private fun getAngle(): Float {
        val angles = mutableListOf<Int>()
        for (i in (-360..360 step 10)) {
            angles.add(i)
        }
        return angles.random().toFloat()
    }

    fun startAnimations() {
        alphaIn.start()
        translation.start()
        rotation.start()
        alphaOut.start()
    }
}