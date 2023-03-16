package com.jjcr11.memorygame.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class GameViewModel : ViewModel() {
    val score = MutableStateFlow(0)
    val mcvPlayVisible = MutableStateFlow(true)
    val tvScoreVisible = MutableStateFlow(true)

    fun resetScore() {
        score.value = 0
    }

    fun addScore() {
        score.value++
    }

    fun setMcvPlayVisible() {
        mcvPlayVisible.value = true
    }

    fun setMcvPlayInvisible() {
        mcvPlayVisible.value = false
    }

    fun setTvScoreVisible() {
        tvScoreVisible.value = true
    }

    fun setTvScoreInvisible() {
        tvScoreVisible.value = false
    }
}