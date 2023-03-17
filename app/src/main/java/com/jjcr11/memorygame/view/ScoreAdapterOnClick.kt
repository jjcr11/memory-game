package com.jjcr11.memorygame.view

import com.jjcr11.memorygame.model.Score

interface ScoreAdapterOnClick {
    fun onClick(score: Score, position: Int)
}