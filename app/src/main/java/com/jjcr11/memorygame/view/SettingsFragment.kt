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
        changeButtonTypeLayout()

        binding.clTheme.setOnClickListener {
            openBackdrop(BackdropThemeFragment())
        }

        val underScore = sharedPreferences?.getFloat("underScore", 0f)!!
        setSliderTextView(underScore)
        binding.s.value = underScore

        binding.s.addOnChangeListener { _, value, _ ->
            sharedPreferences?.edit()?.putFloat("underScore", value)?.apply()
            setSliderTextView(value)
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            removeBackdrop()
        }

        binding.clType.setOnClickListener {
            openBackdrop(BackdropTypeFragment())
        }

        binding.clButton1.setOnClickListener {}
        binding.clButton2.setOnClickListener {}
        binding.clButton3.setOnClickListener {}
        binding.clButton4.setOnClickListener {}
        binding.clButton5.setOnClickListener {}
        binding.clButton6.setOnClickListener {}
        binding.clButton7.setOnClickListener {}
        binding.clButton8.setOnClickListener {}

        binding.tvExportData.setOnClickListener {}

        return binding.root
    }

    private fun openBackdrop(backdrop: Fragment) {
        if (!backdropOpen) {
            binding.mcv.visibility = View.VISIBLE
            childFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.anim_enter_backdrop,
                    0,
                    0,
                    R.anim.anim_exit_backdrop,
                )
                .add(R.id.f, backdrop)
                .addToBackStack(null)
                .commit()
            backdropOpen = true
        }
    }

    fun removeBackdrop() {
        if (backdropOpen) {
            binding.mcv.visibility = View.GONE
            childFragmentManager.popBackStack()
            changeThemeTitle()
            changeButtonTypeLayout()
            backdropOpen = false
        } else {
            (activity as MainActivity).updateBottomNavigation()
        }
    }

    private fun changeThemeTitle() {
        when (sharedPreferences?.getInt("theme", 2)) {
            0 -> binding.tvTheme.text = "Light"
            1 -> binding.tvTheme.text = "Dark"
            2 -> binding.tvTheme.text = "System"
        }
    }

    private fun changeButtonTypeLayout() {
        binding.llButtons.visibility = View.VISIBLE
        when (sharedPreferences?.getInt("type", 0)) {
            0 -> binding.tvType.text = "Colors"
            1 -> {
                binding.tvType.text = "Numbers"
                binding.llButtons.visibility = View.GONE
            }
            2 -> binding.tvType.text = "Both"
        }
    }

    private fun setSliderTextView(value: Float) {
        if (value == 0f)
            binding.tvUnderScore.text = "Never"
        else
            binding.tvUnderScore.text = "${value.toInt()}"
    }
}