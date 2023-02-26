package com.jjcr11.memorygame.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import com.jjcr11.memorygame.R
import com.jjcr11.memorygame.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private var sharedPreferences: SharedPreferences? = null
    private var backdropOpen = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        sharedPreferences = activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)
        changeThemeTitle()

        binding.clTheme.setOnClickListener {
            if (!backdropOpen) {
                binding.mcv.visibility = View.VISIBLE
                childFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        R.anim.anim_enter_backdrop,
                        0,
                        0,
                        R.anim.anim_exit_backdrop,
                    )
                    .add(R.id.f, BackdropThemeFragment())
                    .addToBackStack(null)
                    .commit()
                backdropOpen = true
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            removeBackdrop()

        }

        binding.tvExportData.setOnClickListener {}
        binding.clType.setOnClickListener {}

        binding.clButton1.setOnClickListener {}
        binding.clButton2.setOnClickListener {}
        binding.clButton3.setOnClickListener {}
        binding.clButton4.setOnClickListener {}
        binding.clButton5.setOnClickListener {}
        binding.clButton6.setOnClickListener {}
        binding.clButton7.setOnClickListener {}
        binding.clButton8.setOnClickListener {}

        return binding.root
    }

    fun removeBackdrop() {
        if (backdropOpen) {
            binding.mcv.visibility = View.GONE
            childFragmentManager.popBackStack()
            changeThemeTitle()
            backdropOpen = false
        } else {
            (activity as MainActivity).updateBottomNavigation()
        }
    }

    private fun changeThemeTitle() {
        when(sharedPreferences?.getInt("theme", 2)) {
            0 -> binding.tvTheme.text = "Light"
            1 -> binding.tvTheme.text = "Dark"
            2 -> binding.tvTheme.text = "System"
        }
    }
}