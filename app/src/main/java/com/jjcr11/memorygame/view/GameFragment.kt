package com.jjcr11.memorygame.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.card.MaterialCardView
import com.jjcr11.memorygame.R
import com.jjcr11.memorygame.databinding.FragmentGameBinding
import com.jjcr11.memorygame.model.AppDatabase
import com.jjcr11.memorygame.model.Score
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.math.hypot

class GameFragment : Fragment() {

    private lateinit var binding: FragmentGameBinding
    private lateinit var mcvMain: MaterialCardView
    private lateinit var mcvMain2: MaterialCardView
    private lateinit var tvMain: TextView
    private lateinit var tvMain2: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private var round = 1
    private var size = 3
    private val gameColors = mutableListOf<Int>()
    private val userColors = mutableListOf<Int>()
    private var score = 0

    private var trueColor1 = 0
    private var trueColor2 = 0
    private var trueColor3 = 0
    private var trueColor4 = 0
    private var trueColor5 = 0
    private var trueColor6 = 0
    private var trueColor7 = 0
    private var trueColor8 = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameBinding.inflate(inflater, container, false)

        mcvMain = binding.mcvMain
        mcvMain2 = binding.mcvMain2

        tvMain = binding.tvMain
        tvMain2 = binding.tvMain2

        sharedPreferences = activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)!!

        setColorButtons()

        if (sharedPreferences.getInt("type", 0) == 0) {
            setAllNumbersAsInvisible()
        } else if (sharedPreferences.getInt("type", 0) == 1) {
            setAllColorsAsTransparent()
        }

        disabledAll()

        binding.mcvPlay.setOnClickListener { startGame() }
        binding.mcvButton1.setOnClickListener { changeColor(trueColor1) }
        binding.mcvButton2.setOnClickListener { changeColor(trueColor2) }
        binding.mcvButton3.setOnClickListener { changeColor(trueColor3) }
        binding.mcvButton4.setOnClickListener { changeColor(trueColor4) }
        binding.mcvButton5.setOnClickListener { changeColor(trueColor5) }
        binding.mcvButton6.setOnClickListener { changeColor(trueColor6) }
        binding.mcvButton7.setOnClickListener { changeColor(trueColor7) }
        binding.mcvButton8.setOnClickListener { changeColor(trueColor8) }

        return binding.root
    }

    private fun startGame() {
        val colors = mutableListOf(
            trueColor1,
            trueColor2,
            trueColor3,
            trueColor4,
            trueColor5,
            trueColor6,
            trueColor7,
            trueColor8,
        )

        if (round % 5 == 0) {
            size++
        }

        lifecycleScope.launch(Dispatchers.Main) {
            binding.mcvPlay.visibility = View.INVISIBLE
            binding.tvScore.visibility = View.VISIBLE

            mcvMain.visibility = View.INVISIBLE
            mcvMain2.visibility = View.INVISIBLE
            delay(1000)

            for (i in 1..size) {
                val colorId = colors.random()
                val animation = setupAnimation(colorId)
                animation.start()
                mcvMain2.visibility = View.VISIBLE

                gameColors.add(colorId)
                colors.remove(colorId)
                delay(700)
            }

            mcvMain.visibility = View.INVISIBLE
            mcvMain2.visibility = View.INVISIBLE
        }
    }

    private fun changeColor(colorId: Int) {
        disabledAll()
        val animation = setupAnimation(colorId)
        animation.start()
        mcvMain2.visibility = View.VISIBLE
        userColors.add(colorId)
    }

    private fun setupAnimation(colorId: Int): Animator {
        mcvMain.elevation = 1f
        mcvMain2.elevation = 200f

        if (sharedPreferences.getInt("type", 0) != 1) {
            mcvMain2.setBackgroundColor(colorId)
        }

        tvMain2.text = when (colorId) {
            trueColor1 -> "1"
            trueColor2 -> "2"
            trueColor3 -> "3"
            trueColor4 -> "4"
            trueColor5 -> "5"
            trueColor6 -> "6"
            trueColor7 -> "7"
            trueColor8 -> "8"
            else -> ""
        }

        val coords = Pair(mcvMain2.width / 2, mcvMain2.height / 2)
        val radius = hypot(coords.first.toDouble(), coords.second.toDouble()).toFloat()
        val animation = ViewAnimationUtils.createCircularReveal(
            mcvMain2,
            coords.first,
            coords.second,
            0f,
            radius
        ).let {
            it.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    val mcvAux = mcvMain
                    mcvMain = mcvMain2
                    mcvMain2 = mcvAux
                    mcvMain2.visibility = View.INVISIBLE

                    val tvAux = tvMain
                    tvMain = tvMain2
                    tvMain2 = tvAux

                    if (userColors.size == size) {
                        if (userColors == gameColors) {
                            winRound()
                        } else {
                            failGame()
                        }
                        gameColors.clear()
                        userColors.clear()
                    }
                    if (gameColors.size == size) {
                        enabledAll()
                    }
                }
            })
            it.duration = 500
            it
        }
        return animation
    }

    private fun winRound() {
        score++
        binding.tvScore.text = score.toString()
        round++
        startGame()
    }

    private fun failGame() {
        lifecycleScope.launch(Dispatchers.Main) {
            val dao = AppDatabase.getDatabase(requireContext()).dao()
            withContext(Dispatchers.IO) {
                val sharedPreferences =
                    activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)
                val underScore = sharedPreferences?.getFloat("underScore", 0f)!!
                if (score >= underScore) {
                    val date = Date()
                    val score = Score(
                        score = score,
                        medal = R.color.transparent,
                        date = date
                    )
                    dao.addScore(score)
                    val scores = dao.getAllScoresByScore()
                    for (i in scores.indices) {
                        when (i) {
                            0 -> dao.updateMedal(scores[0].id, R.color.gold)
                            1 -> dao.updateMedal(scores[1].id, R.color.silver)
                            2 -> dao.updateMedal(scores[2].id, R.color.copper)
                            3 -> dao.updateMedal(scores[3].id, R.color.transparent_2)
                            else -> dao.updateMedal(scores[i].id, R.color.transparent)
                        }
                    }
                }
            }
            Toast.makeText(requireContext(), "Game Over", Toast.LENGTH_SHORT).show()
            score = 0
            round = 1
            binding.tvScore.text = score.toString()
            binding.tvScore.visibility = View.INVISIBLE
            binding.mcvPlay.visibility = View.VISIBLE
            disabledAll()
        }
    }

    private fun disabledAll() {
        binding.mcvButton1.isEnabled = false
        binding.mcvButton2.isEnabled = false
        binding.mcvButton3.isEnabled = false
        binding.mcvButton4.isEnabled = false
        binding.mcvButton5.isEnabled = false
        binding.mcvButton6.isEnabled = false
        binding.mcvButton7.isEnabled = false
        binding.mcvButton8.isEnabled = false
    }

    private fun enabledAll() {
        binding.mcvButton1.isEnabled = true
        binding.mcvButton2.isEnabled = true
        binding.mcvButton3.isEnabled = true
        binding.mcvButton4.isEnabled = true
        binding.mcvButton5.isEnabled = true
        binding.mcvButton6.isEnabled = true
        binding.mcvButton7.isEnabled = true
        binding.mcvButton8.isEnabled = true
    }

    private fun setAllNumbersAsInvisible() {
        tvMain.visibility = View.INVISIBLE
        tvMain2.visibility = View.INVISIBLE
        binding.tvButton1.visibility = View.INVISIBLE
        binding.tvButton2.visibility = View.INVISIBLE
        binding.tvButton3.visibility = View.INVISIBLE
        binding.tvButton4.visibility = View.INVISIBLE
        binding.tvButton5.visibility = View.INVISIBLE
        binding.tvButton6.visibility = View.INVISIBLE
        binding.tvButton7.visibility = View.INVISIBLE
        binding.tvButton8.visibility = View.INVISIBLE
    }

    private fun setAllColorsAsTransparent() {
        val transparent = ContextCompat.getColor(requireContext(), R.color.transparent)
        binding.mcvButton1.setBackgroundColor(transparent)
        binding.mcvButton2.setBackgroundColor(transparent)
        binding.mcvButton3.setBackgroundColor(transparent)
        binding.mcvButton4.setBackgroundColor(transparent)
        binding.mcvButton5.setBackgroundColor(transparent)
        binding.mcvButton6.setBackgroundColor(transparent)
        binding.mcvButton7.setBackgroundColor(transparent)
        binding.mcvButton8.setBackgroundColor(transparent)
    }

    private fun setColorButtons() {
        var color = sharedPreferences.getInt("color1", R.color.imperial_red)
        trueColor1 = compareColors(color, R.color.imperial_red)
        binding.mcvButton1.setCardBackgroundColor(trueColor1)

        color = sharedPreferences.getInt("color2", R.color.orange_crayola)
        trueColor2 = compareColors(color, R.color.orange_crayola)
        binding.mcvButton2.setCardBackgroundColor(trueColor2)

        color = sharedPreferences.getInt("color3", R.color.fuchsia)
        trueColor3 = compareColors(color, R.color.fuchsia)
        binding.mcvButton3.setCardBackgroundColor(trueColor3)

        color = sharedPreferences.getInt("color4", R.color.saffron)
        trueColor4 = compareColors(color, R.color.saffron)
        binding.mcvButton4.setCardBackgroundColor(trueColor4)

        color = sharedPreferences.getInt("color5", R.color.pistachio)
        trueColor5 = compareColors(color, R.color.pistachio)
        binding.mcvButton5.setCardBackgroundColor(trueColor5)

        color = sharedPreferences.getInt("color6", R.color.medium_slate_blue)
        trueColor6 = compareColors(color, R.color.medium_slate_blue)
        binding.mcvButton6.setCardBackgroundColor(trueColor6)

        color = sharedPreferences.getInt("color7", R.color.rose_taupe)
        trueColor7 = compareColors(color, R.color.rose_taupe)
        binding.mcvButton7.setCardBackgroundColor(trueColor7)

        color = sharedPreferences.getInt("color8", R.color.cerulean)
        trueColor8 = compareColors(color, R.color.cerulean)
        binding.mcvButton8.setCardBackgroundColor(trueColor8)
    }

    private fun compareColors(color1: Int, color2: Int): Int {
        return if (color1 == color2) {
            ContextCompat.getColor(requireContext(), color2)
        } else {
            color1
        }
    }
}