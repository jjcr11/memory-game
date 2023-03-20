package com.jjcr11.memorygame.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jjcr11.memorygame.R
import com.jjcr11.memorygame.databinding.DialogCustomBinding
import com.jjcr11.memorygame.databinding.FragmentSettingsBinding
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.flag.BubbleFlag
import com.skydoves.colorpickerview.flag.FlagMode
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var backdropOpen = false
    private lateinit var colors: SharedPreferences

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

        binding.clButton1.setOnClickListener {
            changeColorButton(
                binding.mcvButton1,
                binding.tvColorButton1,
                "colorButton1"
            )
        }
        binding.clButton2.setOnClickListener {
            changeColorButton(
                binding.mcvButton2,
                binding.tvColorButton2,
                "colorButton2"
            )
        }
        binding.clButton3.setOnClickListener {
            changeColorButton(
                binding.mcvButton3,
                binding.tvColorButton3,
                "colorButton3"
            )
        }
        binding.clButton4.setOnClickListener {
            changeColorButton(
                binding.mcvButton4,
                binding.tvColorButton4,
                "colorButton4"
            )
        }
        binding.clButton5.setOnClickListener {
            changeColorButton(
                binding.mcvButton5,
                binding.tvColorButton5,
                "colorButton5"
            )
        }
        binding.clButton6.setOnClickListener {
            changeColorButton(
                binding.mcvButton6,
                binding.tvColorButton6,
                "colorButton6"
            )
        }
        binding.clButton7.setOnClickListener {
            changeColorButton(
                binding.mcvButton7,
                binding.tvColorButton7,
                "colorButton7"
            )
        }
        binding.clButton8.setOnClickListener {
            changeColorButton(
                binding.mcvButton8,
                binding.tvColorButton8,
                "colorButton8"
            )
        }


        binding.tvExportData.setOnClickListener {
            openBackdrop(BackdropExportFragment())
        }

        binding.llSourceCode.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/jjcr11/memory-game"))
            startActivity(intent)
        }

        binding.mtb.menu.getItem(0).setOnMenuItemClickListener {
            val bindingDialog: DialogCustomBinding =
                DialogCustomBinding.inflate(layoutInflater)

            bindingDialog.tvLine1.text = "Reset all settings"
            bindingDialog.tvLine2.visibility = View.GONE

            MaterialAlertDialogBuilder(requireContext())
                .setCancelable(false)
                .setView(bindingDialog.root)
                .setPositiveButton("Accept") { _, _ ->
                    sharedPreferences.edit().putFloat("underScore", 0f).apply()
                    binding.s.value = 0f
                    sharedPreferences.edit().putInt("type", 0).apply()
                    binding.llButtons.visibility = View.VISIBLE
                    binding.tvType.text = "Colors"
                    setDefaultColorButtons()
                }
                .setNegativeButton("Cancel") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
                .show()

            true
        }

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

    private fun changeColorButton(
        materialCardView: MaterialCardView,
        textView: TextView,
        tag: String
    ) {
        val builder = ColorPickerDialog.Builder(requireContext())
            .setTitle("Select a color")
            .setPositiveButton("Select", ColorEnvelopeListener { envelope, _ ->
                val color = "#${envelope.hexCode.substring(2..7)}"
                textView.text = color
                materialCardView.setCardBackgroundColor(envelope.color)
                colors.edit()?.putString(tag, color)?.apply()
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

        binding.tvColorButton1.text = colorButton1
        binding.tvColorButton2.text = colorButton2
        binding.tvColorButton3.text = colorButton3
        binding.tvColorButton4.text = colorButton4
        binding.tvColorButton5.text = colorButton5
        binding.tvColorButton6.text = colorButton6
        binding.tvColorButton7.text = colorButton7
        binding.tvColorButton8.text = colorButton8
    }

    private fun isDarkTheme(): Boolean =
        when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> false
            else -> false
        }

    private fun setDefaultColorButtons() {
        if (isDarkTheme()) {
            colors.edit().putString("colorButton1", "#DB070A").apply()
            colors.edit().putString("colorButton2", "#C14B0B").apply()
            colors.edit().putString("colorButton3", "#C206B9").apply()
            colors.edit().putString("colorButton4", "#E1A008").apply()
            colors.edit().putString("colorButton5", "#669443").apply()
            colors.edit().putString("colorButton6", "#4A00F8").apply()
            colors.edit().putString("colorButton7", "#66433D").apply()
            colors.edit().putString("colorButton8", "#1C5872").apply()
            binding.mcvButton1.setCardBackgroundColor(Color.parseColor("#DB070A"))
            binding.mcvButton2.setCardBackgroundColor(Color.parseColor("#C14B0B"))
            binding.mcvButton3.setCardBackgroundColor(Color.parseColor("#C206B9"))
            binding.mcvButton4.setCardBackgroundColor(Color.parseColor("#E1A008"))
            binding.mcvButton5.setCardBackgroundColor(Color.parseColor("#669443"))
            binding.mcvButton6.setCardBackgroundColor(Color.parseColor("#4A00F8"))
            binding.mcvButton7.setCardBackgroundColor(Color.parseColor("#66433D"))
            binding.mcvButton8.setCardBackgroundColor(Color.parseColor("#1C5872"))
        } else {
            colors.edit().putString("colorButton1", "#F94144").apply()
            colors.edit().putString("colorButton2", "#F3722C").apply()
            colors.edit().putString("colorButton3", "#F820ED").apply()
            colors.edit().putString("colorButton4", "#F9C74F").apply()
            colors.edit().putString("colorButton5", "#90BE6D").apply()
            colors.edit().putString("colorButton6", "#8D5BFF").apply()
            colors.edit().putString("colorButton7", "#8F5E56").apply()
            colors.edit().putString("colorButton8", "#277DA1").apply()
            binding.mcvButton1.setCardBackgroundColor(Color.parseColor("#F94144"))
            binding.mcvButton2.setCardBackgroundColor(Color.parseColor("#F3722C"))
            binding.mcvButton3.setCardBackgroundColor(Color.parseColor("#F820ED"))
            binding.mcvButton4.setCardBackgroundColor(Color.parseColor("#F9C74F"))
            binding.mcvButton5.setCardBackgroundColor(Color.parseColor("#90BE6D"))
            binding.mcvButton6.setCardBackgroundColor(Color.parseColor("#8D5BFF"))
            binding.mcvButton7.setCardBackgroundColor(Color.parseColor("#8F5E56"))
            binding.mcvButton8.setCardBackgroundColor(Color.parseColor("#277DA1"))
        }
    }
}