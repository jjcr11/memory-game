package com.jjcr11.memorygame.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jjcr11.memorygame.R
import com.jjcr11.memorygame.databinding.FragmentScoreBinding
import com.jjcr11.memorygame.viewmodel.ScoreViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScoreFragment : Fragment() {

    private lateinit var binding: FragmentScoreBinding
    private lateinit var adapter: ScoreAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var swipeHelper: ItemTouchHelper
    private lateinit var scoreViewModel: ScoreViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScoreBinding.inflate(inflater, container, false)

        adapter = ScoreAdapter(mutableListOf())
        layoutManager = LinearLayoutManager(requireContext())

        binding.rv.let {
            it.adapter = adapter
            it.layoutManager = layoutManager
        }

        scoreViewModel = ViewModelProvider(activity as MainActivity)[ScoreViewModel::class.java]
        scoreViewModel.model.observe(activity as MainActivity) {
            if (it.size >= 3) {
                it[0].medal = R.color.gold
                it[1].medal = R.color.silver
                it[2].medal = R.color.copper
            } else if (it.size >= 2) {
                it[0].medal = R.color.gold
                it[1].medal = R.color.silver
            } else if (it.size >= 1) {
                it[0].medal = R.color.gold
            }
            adapter.scores = it
        }

        lifecycleScope.launch(Dispatchers.IO) {
            scoreViewModel.getAllScores()
        }

        swipeHelper = ItemTouchHelper(
            ScoreItemTouchHelper(
                adapter,
                requireContext(),
                binding.root,
                viewLifecycleOwner
            )
        )
        swipeHelper.attachToRecyclerView(binding.rv)

        return binding.root
    }
}