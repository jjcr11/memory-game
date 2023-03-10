package com.jjcr11.memorygame.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
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
    private val gameColors = mutableListOf<String>()
    private val userColors = mutableListOf<String>()
    private var score = 0

    private lateinit var colorButton1: String
    private lateinit var colorButton2: String
    private lateinit var colorButton3: String
    private lateinit var colorButton4: String
    private lateinit var colorButton5: String
    private lateinit var colorButton6: String
    private lateinit var colorButton7: String
    private lateinit var colorButton8: String

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

        if (sharedPreferences.getInt("type", 0) == 0) {
            setAllNumbersAsInvisible()
        } else if (sharedPreferences.getInt("type", 0) == 1) {
            setAllColorsAsTransparent()
        }

        setColorButtons()
        disabledAll()

        binding.mcvPlay.setOnClickListener { startGame() }
        binding.mcvButton1.setOnClickListener { changeColor(colorButton1) }
        binding.mcvButton2.setOnClickListener { changeColor(colorButton2) }
        binding.mcvButton3.setOnClickListener { changeColor(colorButton3) }
        binding.mcvButton4.setOnClickListener { changeColor(colorButton4) }
        binding.mcvButton5.setOnClickListener { changeColor(colorButton5) }
        binding.mcvButton6.setOnClickListener { changeColor(colorButton6) }
        binding.mcvButton7.setOnClickListener { changeColor(colorButton7) }
        binding.mcvButton8.setOnClickListener { changeColor(colorButton8) }

        return binding.root
    }

    private fun startGame() {
        val colors = mutableListOf(
            colorButton1,
            colorButton2,
            colorButton3,
            colorButton4,
            colorButton5,
            colorButton6,
            colorButton7,
            colorButton8
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
                val color = colors.random()
                val animation = setupAnimation(color)
                animation.start()
                mcvMain2.visibility = View.VISIBLE
                gameColors.add(color)
                colors.remove(color)
                delay(700)
            }

            mcvMain.visibility = View.INVISIBLE
            mcvMain2.visibility = View.INVISIBLE
        }
    }

    private fun changeColor(color: String) {
        disabledAll()
        val animation = setupAnimation(color)
        animation.start()
        mcvMain2.visibility = View.VISIBLE
        userColors.add(color)
    }

    private fun setupAnimation(color: String): Animator {
        mcvMain.elevation = 1f
        mcvMain2.elevation = 200f

        if (sharedPreferences.getInt("type", 0) != 1) {
            mcvMain2.setBackgroundColor(Color.parseColor(color))
        }

        tvMain2.text = when(color) {
            colorButton1 -> "1"
            colorButton2 -> "2"
            colorButton3 -> "3"
            colorButton4 -> "4"
            colorButton5 -> "5"
            colorButton6 -> "6"
            colorButton7 -> "7"
            colorButton8 -> "8"
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
            size = 3
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
        val colors: SharedPreferences
        if(isDarkTheme()) {
            colors = activity?.getSharedPreferences("darkColors", Context.MODE_PRIVATE)!!
            colorButton1 = colors.getString("colorButton1", "#DB070A")!!
            colorButton2 = colors.getString("colorButton2", "#C14B0B")!!
            colorButton3 = colors.getString("colorButton3", "#C206B9")!!
            colorButton4 = colors.getString("colorButton4", "#E1A008")!!
            colorButton5 = colors.getString("colorButton5", "#669443")!!
            colorButton6 = colors.getString("colorButton6", "#4A00F8")!!
            colorButton7 = colors.getString("colorButton7", "#66433D")!!
            colorButton8 = colors.getString("colorButton8", "#1C5872")!!
        } else {
            colors = activity?.getSharedPreferences("lightColors", Context.MODE_PRIVATE)!!
            colorButton1 = colors.getString("colorButton1", "#F94144")!!
            colorButton2 = colors.getString("colorButton2", "#F3722C")!!
            colorButton3 = colors.getString("colorButton3", "#F820ED")!!
            colorButton4 = colors.getString("colorButton4", "#F9C74F")!!
            colorButton5 = colors.getString("colorButton5", "#90BE6D")!!
            colorButton6 = colors.getString("colorButton6", "#8D5BFF")!!
            colorButton7 = colors.getString("colorButton7", "#8F5E56")!!
            colorButton8 = colors.getString("colorButton8", "#277DA1")!!
        }
        binding.mcvButton1.setCardBackgroundColor(Color.parseColor(colorButton1))
        binding.mcvButton2.setCardBackgroundColor(Color.parseColor(colorButton2))
        binding.mcvButton3.setCardBackgroundColor(Color.parseColor(colorButton3))
        binding.mcvButton4.setCardBackgroundColor(Color.parseColor(colorButton4))
        binding.mcvButton5.setCardBackgroundColor(Color.parseColor(colorButton5))
        binding.mcvButton6.setCardBackgroundColor(Color.parseColor(colorButton6))
        binding.mcvButton7.setCardBackgroundColor(Color.parseColor(colorButton7))
        binding.mcvButton8.setCardBackgroundColor(Color.parseColor(colorButton8))
    }

    private fun isDarkTheme(): Boolean =
        when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> false
            else -> false
        }
}