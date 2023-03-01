package com.jjcr11.memorygame.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView
import com.jjcr11.memorygame.R
import com.jjcr11.memorygame.databinding.FragmentSettingsBinding
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.flag.BubbleFlag
import com.skydoves.colorpickerview.flag.FlagMode
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var backdropOpen = false

    private var trueColor1 = 0
    private var trueColor2 = 0
    private var trueColor3 = 0
    private var trueColor4 = 0
    private var trueColor5 = 0
    private var trueColor6 = 0
    private var trueColor7 = 0
    private var trueColor8 = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        sharedPreferences = activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)!!
        changeThemeTitle()
        changeButtonTypeLayout()

        setColorButtons()

        binding.clTheme.setOnClickListener {
            openBackdrop(BackdropThemeFragment())
        }

        val underScore = sharedPreferences.getFloat("underScore", 0f)
        setSliderTextView(underScore)
        binding.s.value = underScore

        binding.s.addOnChangeListener { _, value, _ ->
            sharedPreferences.edit()?.putFloat("underScore", value)?.apply()
            setSliderTextView(value)
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            removeBackdrop()
        }

        binding.clType.setOnClickListener {
            openBackdrop(BackdropTypeFragment())
        }

        binding.clButton1.setOnClickListener { changeColorButton(binding.mcvButton1, "color1", binding.tvColorButton1) }
        binding.clButton2.setOnClickListener { changeColorButton(binding.mcvButton2, "color2", binding.tvColorButton2) }
        binding.clButton3.setOnClickListener { changeColorButton(binding.mcvButton3, "color3", binding.tvColorButton3) }
        binding.clButton4.setOnClickListener { changeColorButton(binding.mcvButton4, "color4", binding.tvColorButton4) }
        binding.clButton5.setOnClickListener { changeColorButton(binding.mcvButton5, "color5", binding.tvColorButton5) }
        binding.clButton6.setOnClickListener { changeColorButton(binding.mcvButton6, "color6", binding.tvColorButton6) }
        binding.clButton7.setOnClickListener { changeColorButton(binding.mcvButton7, "color7", binding.tvColorButton7) }
        binding.clButton8.setOnClickListener { changeColorButton(binding.mcvButton8, "color8", binding.tvColorButton8) }

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
        when (sharedPreferences.getInt("theme", 2)) {
            0 -> binding.tvTheme.text = "Light"
            1 -> binding.tvTheme.text = "Dark"
            2 -> binding.tvTheme.text = "System"
        }
    }

    private fun changeButtonTypeLayout() {
        binding.llButtons.visibility = View.VISIBLE
        when (sharedPreferences.getInt("type", 0)) {
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

    private fun changeColorButton(materialCardView: MaterialCardView, tag: String, textView: TextView) {
        val builder = ColorPickerDialog.Builder(requireContext())
            .setTitle("Select a color")
            .setPositiveButton("Select", ColorEnvelopeListener { envelope, _ ->
                val colorCode = "#" + envelope.hexCode.lowercase().substring(2..7)
                textView.text = colorCode
                materialCardView.setCardBackgroundColor(envelope.color)
                sharedPreferences.edit()?.putInt(tag, envelope.color)?.apply()
            })
            .setNegativeButton("Cancel") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .attachAlphaSlideBar(false)

        val colorPickerView = builder.colorPickerView
        val bubbleFlag = BubbleFlag(requireContext())
        val color = materialCardView.cardBackgroundColor.defaultColor

        bubbleFlag.flagMode = FlagMode.ALWAYS
        colorPickerView.flagView = bubbleFlag
        colorPickerView.setInitialColor(color)

        builder.show()
    }

    private fun setColorButtons() {
        var color = sharedPreferences.getInt("color1", R.color.imperial_red)
        trueColor1 = compareColors(color, R.color.imperial_red, binding.tvColorButton1)
        binding.mcvButton1.setCardBackgroundColor(trueColor1)

        color = sharedPreferences.getInt("color2", R.color.orange_crayola)
        trueColor2 = compareColors(color, R.color.orange_crayola, binding.tvColorButton2)
        binding.mcvButton2.setCardBackgroundColor(trueColor2)

        color = sharedPreferences.getInt("color3", R.color.fuchsia)
        trueColor3 = compareColors(color, R.color.fuchsia, binding.tvColorButton3)
        binding.mcvButton3.setCardBackgroundColor(trueColor3)

        color = sharedPreferences.getInt("color4", R.color.saffron)
        trueColor4 = compareColors(color, R.color.saffron, binding.tvColorButton4)
        binding.mcvButton4.setCardBackgroundColor(trueColor4)

        color = sharedPreferences.getInt("color5", R.color.pistachio)
        trueColor5 = compareColors(color, R.color.pistachio, binding.tvColorButton5)
        binding.mcvButton5.setCardBackgroundColor(trueColor5)

        color = sharedPreferences.getInt("color6", R.color.medium_slate_blue)
        trueColor6 = compareColors(color, R.color.medium_slate_blue, binding.tvColorButton6)
        binding.mcvButton6.setCardBackgroundColor(trueColor6)

        color = sharedPreferences.getInt("color7", R.color.rose_taupe)
        trueColor7 = compareColors(color, R.color.rose_taupe, binding.tvColorButton7)
        binding.mcvButton7.setCardBackgroundColor(trueColor7)

        color = sharedPreferences.getInt("color8", R.color.cerulean)
        trueColor8 = compareColors(color, R.color.cerulean, binding.tvColorButton8)
        binding.mcvButton8.setCardBackgroundColor(trueColor8)
    }

    private fun compareColors(color1: Int, color2: Int, textView: TextView): Int {
        val colorCode: String
        val trueColor = if (color1 == color2) {
            colorCode = "#" + resources.getString(color2).substring(3..8)
            ContextCompat.getColor(requireContext(), color2)
        } else {
            colorCode = "#" + Integer.toHexString(color1).substring(2..7)
            color1
        }

        textView.text = colorCode

        return trueColor
    }
}