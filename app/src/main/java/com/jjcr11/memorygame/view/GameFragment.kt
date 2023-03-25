package com.jjcr11.memorygame.view

import android.app.ActionBar
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jjcr11.memorygame.R
import com.jjcr11.memorygame.databinding.FragmentGameBinding
import com.jjcr11.memorygame.model.AppDatabase
import com.jjcr11.memorygame.model.Score
import com.jjcr11.memorygame.model.Star
import com.jjcr11.memorygame.viewmodel.GameViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.math.hypot

class GameFragment : Fragment() {

    private lateinit var binding: FragmentGameBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var gameViewModel: GameViewModel
    private var round = 1
    private var size = 3
    private var gameColors = mutableListOf<String>()
    private val stars = mutableListOf<Star>()

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

        if (sharedPreferences.getBoolean("onBoarding", true)) {
            val navController = findNavController()
            navController.navigate(R.id.onBoardingFragment)
        }

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

        binding.tvClose.setOnClickListener { hideNewRecord() }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameViewModel = GameViewModel()
        lifecycleScope.launch {
            gameViewModel.score.collect { binding.tvScore.text = "$it" }

        }
        lifecycleScope.launch {
            gameViewModel.mcvPlayVisible.collect {
                binding.mcvPlay.visibility = if (it) View.VISIBLE else View.INVISIBLE
            }
        }
        lifecycleScope.launch {
            gameViewModel.tvScoreVisible.collect {
                binding.tvScore.visibility = if (it) View.VISIBLE else View.INVISIBLE
            }
        }

        savedInstanceState?.getInt("score")?.let { gameViewModel.score.value = it }
        savedInstanceState?.getBoolean("mcvPlayVisible")?.let {
            gameViewModel.mcvPlayVisible.value = it
        }
        savedInstanceState?.getBoolean("tvScoreVisible")?.let {
            gameViewModel.mcvPlayVisible.value = it
        }
        savedInstanceState?.getInt("round")?.let { this.round = it }
        savedInstanceState?.getInt("size")?.let { this.size = it }
        savedInstanceState?.getStringArrayList("gameColors")?.let {
            if (it.isNotEmpty()) {
                this.gameColors = it
                enabledAll()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (this::binding.isInitialized) {
            val numberString = binding.tvScore.text.toString()
            outState.putInt("score", numberString.toInt())
            outState.putBoolean("mcvPlayVisible", binding.mcvPlay.isVisible)
            outState.putBoolean("tvScoreVisible", binding.mcvPlay.isVisible)
            outState.putInt("round", round)
            outState.putInt("size", size)
            val arrayList = ArrayList(gameColors)
            outState.putStringArrayList("gameColors", arrayList)
        }
    }

    private fun hideNewRecord() {
        binding.vRecord.visibility = View.GONE
        binding.mcvRecord.visibility = View.GONE
        for (star in stars) {
            binding.root.removeView(star.view)
        }
        stars.clear()
    }

    private fun startGame(delay: Long = 0) {
        lifecycleScope.launch(Dispatchers.Main) {
            disabledAll()
            delay(delay)
            binding.mcvMain.visibility = View.INVISIBLE
            gameViewModel.setMcvPlayInvisible()
            gameViewModel.setTvScoreVisible()

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
        gameViewModel.addScore()
        round++
        gameColors.clear()
        startGame(delay = 1000)
    }

    private fun failRound() {
        lifecycleScope.launch(Dispatchers.Main) {
            var scores = listOf<Score>()
            withContext(Dispatchers.IO) {
                val dao = AppDatabase.getDatabase(requireContext()).dao()
                val underScore = sharedPreferences.getFloat("underScore", 1f)
                if (gameViewModel.score.value >= underScore) {
                    val date = Date()
                    val score = Score(
                        score = gameViewModel.score.value,
                        medal = R.color.transparent,
                        date = date
                    )
                    dao.addScore(score)
                    scores = dao.getAllScoresByScore()
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

            val bestScores = scores.filter { it.score == gameViewModel.score.value }
            if (bestScores.size == 1) {
                if (scores[0].score == gameViewModel.score.value) {
                    binding.vRecord.visibility = View.VISIBLE
                    binding.mcvRecord.visibility = View.VISIBLE
                    binding.tvRecord.text = "Score: ${gameViewModel.score.value}"
                    val scale = resources.displayMetrics.density
                    var margin = (10 * scale).toInt()
                    val starCount = resources.displayMetrics.heightPixels / 10
                    for (i in 1..starCount) {
                        val imageView = makeImageView(scale)

                        binding.root.addView(imageView)

                        val constrainSet = makeConstrainSet(imageView, margin, i)

                        if (i % 2 == 0) {
                            margin += (10 * scale).toInt()
                        }

                        constrainSet.applyTo(binding.root)

                        val animationDuration = (2000..5000).random().toLong()

                        val star = Star(imageView, animationDuration)
                        star.startAnimations()
                        stars.add(star)
                    }
                }
            }

            binding.mcvPlay.visibility = View.VISIBLE
            gameViewModel.setMcvPlayVisible()
            gameViewModel.setTvScoreInvisible()
            gameViewModel.resetScore()
            round = 1
            size = 3
            gameColors.clear()
            disabledAll()
        }
    }

    private fun makeImageView(scale: Float): ImageView {
        return ImageView(requireContext()).apply {
            id = View.generateViewId()
            setImageResource(R.drawable.ic_star)
            layoutParams = ActionBar.LayoutParams((20 * scale).toInt(), (20 * scale).toInt())
            elevation = 50f
            alpha = 0f
            setColorFilter(ContextCompat.getColor(requireContext(), R.color.gold))
        }
    }

    private fun makeConstrainSet(
        imageView: ImageView,
        marginTop: Int,
        index: Int,
    ): ConstraintSet {
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.root)
        constraintSet.connect(
            imageView.id,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP,
            marginTop
        )

        val width = resources.displayMetrics.widthPixels / 2
        val margin = (0..width).random()

        if (index % 2 == 0) {
            constraintSet.connect(
                imageView.id,
                ConstraintSet.START,
                ConstraintSet.PARENT_ID,
                ConstraintSet.START,
                margin
            )
        } else {
            constraintSet.connect(
                imageView.id,
                ConstraintSet.END,
                ConstraintSet.PARENT_ID,
                ConstraintSet.END,
                margin
            )
        }

        return constraintSet
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