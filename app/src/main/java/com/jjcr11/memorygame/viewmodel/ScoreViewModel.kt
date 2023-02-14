package com.jjcr11.memorygame.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.jjcr11.memorygame.model.AppDatabase
import com.jjcr11.memorygame.model.Score

class ScoreViewModel(application: Application): AndroidViewModel(application) {
    val model = MutableLiveData<MutableList<Score>>()

    suspend fun getAllScores() {
        val scores = AppDatabase.getDatabase(getApplication()).dao().getAllScores()
        model.postValue(scores)
    }
}