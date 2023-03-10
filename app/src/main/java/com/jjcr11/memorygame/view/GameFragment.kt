package com.jjcr11.memorygame.view

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
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
    private lateinit var sharedPreferences: SharedPreferences
    private var round = 1
    private var size = 3
    private val gameColors = mutableListOf<String>()
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

    private fun startGame(delay: Long = 0) {
        lifecycleScope.launch(Dispatchers.Main) {
            disabledAll()
            delay(delay)
            binding.mcvMain.visibility = View.INVISIBLE
            binding.mcvPlay.visibility = View.INVISIBLE
            binding.tvScore.visibility = View.VISIBLE

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

            for (i in 1..size) {
                val color = colors.random()

                if (sharedPreferences.getInt("type", 0) != 1) {
                    binding.mcvMain.setBackgroundColor(Color.parseColor(color))
                }

                binding.tvMain.text = getNumber(color)
                startAnimation()

                binding.mcvMain.visibility = View.VISIBLE
                gameColors.add(color)
                colors.remove(color)
                delay(900)
                binding.mcvMain.visibility = View.INVISIBLE
            }
            enabledAll()
        }
    }

    private fun changeColor(color: String) {
        if (sharedPreferences.getInt("type", 0) != 1) {
            binding.mcvMain.setBackgroundColor(Color.parseColor(color))
        }

        binding.tvMain.text = getNumber(color)
        startAnimation()

        binding.mcvMain.visibility = View.VISIBLE

        if (color == gameColors[0]) {
            gameColors.remove(color)
            if (gameColors.isEmpty()) {
                winRound()
            }
        } else {
            failRound()
        }

    }

    private fun startAnimation() {
        val coords = Pair(binding.mcvMain.width / 2, binding.mcvMain.height / 2)
        val radius = hypot(coords.first.toDouble(), coords.second.toDouble()).toFloat()
        val animation = ViewAnimationUtils.createCircularReveal(
            binding.mcvMain,
            coords.first,
            coords.second,
            0f,
            radius
        )
        animation.duration = 500
        animation.start()
    }

    private fun getNumber(color: String): String {
        return when (color) {
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
    }


    private fun winRound() {
        score++
        round++
        gameColors.clear()
        binding.tvScore.text = "$score"
        startGame(delay = 1000)
    }

    private fun failRound() {
        lifecycleScope.launch(Dispatchers.Main) {
            val dao = AppDatabase.getDatabase(requireContext()).dao()
            withContext(Dispatchers.IO) {
                val underScore = sharedPreferences.getFloat("underScore", 0f)
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
            Toast.makeText(requireContext(), "Score: $score", Toast.LENGTH_LONG).show()
            binding.mcvPlay.visibility = View.VISIBLE
            binding.tvScore.visibility = View.INVISIBLE
            score = 0
            round = 1
            size = 3
            gameColors.clear()
            binding.tvScore.text = "$score"
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
        binding.tvMain.visibility = View.INVISIBLE
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
        if (isDarkTheme()) {
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