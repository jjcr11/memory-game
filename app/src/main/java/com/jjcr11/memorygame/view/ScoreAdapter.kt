package com.jjcr11.memorygame.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.jjcr11.memorygame.R
import com.jjcr11.memorygame.databinding.ItemScoreBinding
import com.jjcr11.memorygame.model.Score
import java.text.SimpleDateFormat
import java.util.*

class ScoreAdapter(
    scores: MutableList<Score>,
    private val listener: ScoreAdapterOnClick
) : RecyclerView.Adapter<ScoreAdapter.ViewHolder>() {

    var scores = scores
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private lateinit var context: Context

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemScoreBinding.bind(view)
        fun setListener(score: Score, position: Int) {
            binding.ibDelete?.setOnClickListener {
                listener.onClick(score, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_score, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val score = scores[position]
        holder.let {
            it.binding.tvScore.text = "${score.score} Score"

            val formatter = SimpleDateFormat("MMM-dd-yyyy", Locale.US)
            val stringDate = formatter.format(score.date)
            it.binding.tvDate.text = stringDate

            val wrappedDrawable = DrawableCompat.wrap(it.binding.ivMedal.drawable)
            val mutableDrawable = wrappedDrawable.mutate()
            DrawableCompat.setTint(
                mutableDrawable,
                ContextCompat.getColor(context, score.medal)
            )

            it.setListener(score, position)
        }
    }

    override fun getItemCount(): Int = scores.size

    fun restoreScore(position: Int, score: Score) {
        val gold = scores.find { it.medal == R.color.gold }
        val silver = scores.find { it.medal == R.color.silver }
        val copper = scores.find { it.medal == R.color.copper }
        when (score.medal) {
            R.color.gold -> {
                gold?.medal = R.color.silver
                silver?.medal = R.color.copper
                copper?.medal = R.color.transparent_2
            }
            R.color.silver -> {
                silver?.medal = R.color.copper
                copper?.medal = R.color.transparent_2
            }
            R.color.copper -> {
                copper?.medal = R.color.transparent_2
            }
        }
        notifyItemRangeChanged(0, scores.size)

        scores.add(position, score)
        notifyItemInserted(position)
    }

    fun deleteScore(position: Int): Score {
        val score = scores.removeAt(position)
        notifyItemRemoved(position)

        val silver = scores.find { it.medal == R.color.silver }
        val copper = scores.find { it.medal == R.color.copper }
        val transparent = scores.find { it.medal == R.color.transparent_2 }
        when (score.medal) {
            R.color.gold -> {
                silver?.medal = R.color.gold
                copper?.medal = R.color.silver
                transparent?.medal = R.color.copper
            }
            R.color.silver -> {
                copper?.medal = R.color.silver
                transparent?.medal = R.color.copper
            }
            R.color.copper -> {
                transparent?.medal = R.color.copper
            }
        }
        notifyItemRangeChanged(0, scores.size)

        return score
    }
}