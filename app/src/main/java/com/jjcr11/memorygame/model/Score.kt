package com.jjcr11.memorygame.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Score(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val score: Int,
    var medal: Int,
    val date: Date
)
