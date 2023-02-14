package com.jjcr11.memorygame.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Score(
    @PrimaryKey(autoGenerate = true) val uid: Long = 0,
    val score: Int,
    var medal: Int,
    val date: String
)
