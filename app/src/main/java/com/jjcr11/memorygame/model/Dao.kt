package com.jjcr11.memorygame.model

import androidx.room.Insert
import androidx.room.Query
import androidx.room.Dao
import androidx.room.Delete

@Dao
interface Dao {
    @Insert
    suspend fun addScore(score: Score)

    @Delete
    suspend fun removeScore(score: Score)

    @Query("SELECT * FROM Score ORDER BY score DESC")
    suspend fun getAllScores(): MutableList<Score>
}