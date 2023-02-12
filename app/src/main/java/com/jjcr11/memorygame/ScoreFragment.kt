package com.jjcr11.memorygame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jjcr11.memorygame.databinding.FragmentScoreBinding


class ScoreFragment : Fragment(), ScoreAdapterOnTouch {

    private lateinit var binding: FragmentScoreBinding
    private lateinit var adapter: ScoreAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScoreBinding.inflate(inflater, container, false)

        val list = mutableListOf<Score>()

        list.add(
            Score(20, R.color.gold)
        )

        list.add(
            Score(19, R.color.silver)
        )

        list.add(
            Score(18, R.color.copper)
        )

        for (i in 17 downTo 1) {
            list.add(
                Score(i, R.color.transparent)
            )
        }

        adapter = ScoreAdapter(list, this)
        layoutManager = LinearLayoutManager(requireContext())

        binding.rv.let {
            it.adapter = adapter
            it.layoutManager = layoutManager
        }

        return binding.root
    }

    override fun onTouch(view: View, motionEvent: MotionEvent, position: Int): Boolean {
        return false
    }
}