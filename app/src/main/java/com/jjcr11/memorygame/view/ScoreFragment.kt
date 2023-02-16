package com.jjcr11.memorygame.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jjcr11.memorygame.R
import com.jjcr11.memorygame.databinding.FragmentScoreBinding
import com.jjcr11.memorygame.model.AppDatabase
import com.jjcr11.memorygame.viewmodel.ScoreViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScoreFragment : Fragment() {

    private lateinit var binding: FragmentScoreBinding
    private lateinit var adapter: ScoreAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var swipeHelper: ItemTouchHelper
    private lateinit var scoreViewModel: ScoreViewModel
    private var backdropOpen = false

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

        binding.mtb.menu.getItem(0).setOnMenuItemClickListener {
            binding.mcv.visibility = View.VISIBLE
            binding.rv.setOnTouchListener { view, motionEvent -> true }
            childFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.anim_enter_backdrop,
                    0,
                    0,
                    R.anim.anim_exit_backdrop
                )
                .add(R.id.f, BackdropFragment())
                .addToBackStack("null")
                .commit()
            backdropOpen = true
            true
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (backdropOpen) {
                binding.mcv.visibility = View.GONE
                childFragmentManager.popBackStack()
                backdropOpen = false
                binding.rv.setOnTouchListener { view, motionEvent -> false }
            } else {
                remove()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        lifecycleScope.launch(Dispatchers.IO) {
            val dao = AppDatabase.getDatabase(requireContext()).dao()
            val scores = dao.getAllScores()
            for (i in scores.indices) {
                when (i) {
                    0 -> dao.updateMedal(scores[0].uid, R.color.gold)
                    1 -> dao.updateMedal(scores[1].uid, R.color.silver)
                    2 -> dao.updateMedal(scores[2].uid, R.color.copper)
                    else -> dao.updateMedal(scores[i].uid, R.color.transparent)
                }
            }
        }
    }
}