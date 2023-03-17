package com.jjcr11.memorygame.view

import android.content.Context
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
import androidx.window.layout.WindowMetricsCalculator
import com.google.android.material.snackbar.Snackbar
import com.jjcr11.memorygame.R
import com.jjcr11.memorygame.databinding.FragmentScoreBinding
import com.jjcr11.memorygame.model.AppDatabase
import com.jjcr11.memorygame.model.Score
import com.jjcr11.memorygame.viewmodel.ScoreViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScoreFragment : Fragment(), ScoreAdapterOnClick {

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

        adapter = ScoreAdapter(mutableListOf(), this)
        layoutManager = LinearLayoutManager(requireContext())

        binding.rv.let {
            it.adapter = adapter
            it.layoutManager = layoutManager
        }

        scoreViewModel = ViewModelProvider(activity as MainActivity)[ScoreViewModel::class.java]
        scoreViewModel.model.observe(activity as MainActivity) {
            adapter.scores = it
        }

        getScores()

        swipeHelper = ItemTouchHelper(
            ScoreItemTouchHelper(
                adapter,
                requireContext(),
                binding.root,
                viewLifecycleOwner
            )
        )

        binding.mtb.menu.getItem(0).setOnMenuItemClickListener {
            if (!backdropOpen) {
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
                    .addToBackStack(null)
                    .commit()
                backdropOpen = true
            }
            true
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            removeBackdrop()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val metrics = WindowMetricsCalculator.getOrCreate()
            .computeCurrentWindowMetrics(requireActivity())

        val dp = metrics.bounds.height() /
                resources.displayMetrics.density

        if (dp >= 480) {
            swipeHelper.attachToRecyclerView(binding.rv)
        }

    }

    fun removeBackdrop() {
        if (backdropOpen) {
            binding.mcv.visibility = View.GONE
            childFragmentManager.popBackStack()
            backdropOpen = false
            binding.rv.setOnTouchListener { view, motionEvent -> false }
            getScores()
        } else {
            (activity as MainActivity).updateBottomNavigation()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        lifecycleScope.launch(Dispatchers.IO) {
            val dao = AppDatabase.getDatabase(requireContext()).dao()
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

    private fun getScores() {
        lifecycleScope.launch(Dispatchers.IO) {
            val sharedPreferences = activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)
            val scoreSelected = sharedPreferences?.getBoolean("scoreSelected", true)!!
            if (scoreSelected) {
                scoreViewModel.getAllScoresByScore()
            } else {
                scoreViewModel.getAllScoresByDate()
            }
        }
    }

    override fun onClick(score: Score, position: Int) {
        adapter.deleteScore(position)
        lifecycleScope.launch(Dispatchers.IO) {
            AppDatabase.getDatabase(requireContext()).dao().removeScore(score)
        }
        Snackbar.make(binding.root, "Deleted", Snackbar.LENGTH_SHORT)
            .setAction("Undo") {
                adapter.restoreScore(position, score)
                lifecycleScope.launch(Dispatchers.IO) {
                    AppDatabase.getDatabase(requireContext()).dao().addScore(score)
                }
            }
            .show()
    }
}