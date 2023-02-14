package com.jjcr11.memorygame

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.jjcr11.memorygame.databinding.ItemScoreBinding

class ScoreAdapter(
    private val scores: MutableList<Score>,
) : RecyclerView.Adapter<ScoreAdapter.ViewHolder>() {

    private lateinit var context: Context

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemScoreBinding.bind(view)
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
            val wrappedDrawable = DrawableCompat.wrap(it.binding.ivMedal.drawable)
            val mutableDrawable = wrappedDrawable.mutate()
            DrawableCompat.setTint(
                mutableDrawable,
                ContextCompat.getColor(context, score.medal)
            )
        }
    }

    override fun getItemCount(): Int = scores.size

    fun restoreScore(position: Int, score: Score) {
        scores.add(position, score)
        notifyItemInserted(position)

        if(position in 0..2) {
            if (scores.size >= 4) {
                scores[0].medal = R.color.gold
                scores[1].medal = R.color.silver
                scores[2].medal = R.color.copper
                scores[3].medal = R.color.transparent
            } else if (scores.size >= 3) {
                scores[0].medal = R.color.gold
                scores[1].medal = R.color.silver
                scores[2].medal = R.color.copper
            } else if (scores.size >= 2) {
                scores[0].medal = R.color.gold
                scores[1].medal = R.color.silver
            } else if (scores.size >= 1) {
                scores[0].medal = R.color.gold
            }
            notifyItemRangeChanged(0, 4)
        }
    }

    fun deleteScore(position: Int): Score {
        val score = scores.removeAt(position)
        notifyItemRemoved(position)

        if (position in 0..2) {
            if (scores.size >= 3) {
                scores[0].medal = R.color.gold
                scores[1].medal = R.color.silver
                scores[2].medal = R.color.copper
            } else if (scores.size >= 2) {
                scores[0].medal = R.color.gold
                scores[1].medal = R.color.silver
            } else if (scores.size >= 1) {
                scores[0].medal = R.color.gold
            }
            notifyItemRangeChanged(0, 3)
        }

        return score
    }
}