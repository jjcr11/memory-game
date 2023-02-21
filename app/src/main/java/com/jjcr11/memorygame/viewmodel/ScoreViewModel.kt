package com.jjcr11.memorygame.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.jjcr11.memorygame.model.AppDatabase
import com.jjcr11.memorygame.model.Score

class ScoreViewModel(application: Application): AndroidViewModel(application) {
    val model = MutableLiveData<MutableList<Score>>()

    suspend fun getAllScoresByScore() {
        val scores = AppDatabase.getDatabase(getApplication()).dao().getAllScoresByScore()
        model.postValue(scores)
    }

    suspend fun getAllScoresByDate() {
        val scores = AppDatabase.getDatabase(getApplication()).dao().getAllScoresByDate()
        model.postValue(scores)
    }
}