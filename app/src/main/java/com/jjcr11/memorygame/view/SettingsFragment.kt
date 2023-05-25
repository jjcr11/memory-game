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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jjcr11.memorygame.R
import com.jjcr11.memorygame.databinding.DialogCustomBinding
import com.jjcr11.memorygame.databinding.FragmentSettingsBinding
import com.jjcr11.memorygame.model.Button
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.flag.BubbleFlag
import com.skydoves.colorpickerview.flag.FlagMode
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var colors: SharedPreferences
    private var backdropOpen = false
    private var buttons = listOf<Button>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        sharedPreferences = activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)!!
        changeThemeTitle()
        changeButtonTypeLayout()

        buttons = listOf(
            Button(
                binding.clButton1, binding.mcvButton1, binding.tvColorButton1,
                "colorButton1", "#DB070A", "#F94144"
            ),
            Button(
                binding.clButton2, binding.mcvButton2, binding.tvColorButton2,
                "colorButton2", "#C14B0B", "#F3722C"
            ),
            Button(
                binding.clButton3, binding.mcvButton3, binding.tvColorButton3,
                "colorButton3", "#C206B9", "#F820ED"
            ),
            Button(
                binding.clButton4, binding.mcvButton4, binding.tvColorButton4,
                "colorButton4", "#E1A008", "#F9C74F"
            ),
            Button(
                binding.clButton5, binding.mcvButton5, binding.tvColorButton5,
                "colorButton5", "#669443", "#90BE6D"
            ),
            Button(
                binding.clButton6, binding.mcvButton6, binding.tvColorButton6,
                "colorButton6", "#4A00F8", "#8D5BFF"
            ),
            Button(
                binding.clButton7, binding.mcvButton7, binding.tvColorButton7,
                "colorButton7", "#66433D", "#8F5E56"
            ),
            Button(
                binding.clButton8, binding.mcvButton8, binding.tvColorButton8,
                "colorButton8", "#1C5872", "#277DA1"
            ),
        )

        setColorButtons()

        binding.clTheme.setOnClickListener {
            openBackdrop(BackdropThemeFragment())
        }

        val underScore = sharedPreferences.getFloat("underScore", 1f)
        setSliderTextView(underScore)
        binding.s.value = underScore

        binding.s.addOnChangeListener { _, value, _ ->
            sharedPreferences.edit()?.putFloat("underScore", value)?.apply()
            setSliderTextView(value)
        }

        binding.sSounds.isChecked = sharedPreferences.getBoolean("sounds", true)
        binding.sSounds.setOnCheckedChangeListener { _, b ->
            sharedPreferences.edit()?.putBoolean("sounds", b)?.apply()
        }

        binding.sVibration.isChecked = sharedPreferences.getBoolean("vibration", true)
        binding.sVibration.setOnCheckedChangeListener { _, b ->
            sharedPreferences.edit()?.putBoolean("vibration", b)?.apply()
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            removeBackdrop()
        }

        binding.clType.setOnClickListener {
            openBackdrop(BackdropTypeFragment())
        }

        for (button in buttons) {
            button.constraintLayout.setOnClickListener {
                changeColorButton(
                    button.materialCardView,
                    button.textView,
                    button.tag
                )
            }
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
                    sharedPreferences.edit().putFloat("underScore", 1f).apply()
                    binding.s.value = 1f
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
        for (button in buttons) {
            val colorButton: String
            if (isDarkTheme()) {
                colors = activity?.getSharedPreferences("darkColors", Context.MODE_PRIVATE)!!
                colorButton = colors.getString(button.tag, button.defaultDarkColor)!!
            } else {
                colors = activity?.getSharedPreferences("lightColors", Context.MODE_PRIVATE)!!
                colorButton = colors.getString(button.tag, button.defaultLightColor)!!
            }
            button.materialCardView.setCardBackgroundColor(Color.parseColor(colorButton))
            button.textView.text = colorButton
        }
    }

    private fun isDarkTheme(): Boolean =
        when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> false
            else -> false
        }

    private fun setDefaultColorButtons() {
        for (button in buttons) {
            if (isDarkTheme()) {
                colors.edit().putString(button.tag, button.defaultDarkColor).apply()
                button.materialCardView.setCardBackgroundColor(Color.parseColor(button.defaultDarkColor))
            } else {
                colors.edit().putString(button.tag, button.defaultLightColor).apply()
                button.materialCardView.setCardBackgroundColor(Color.parseColor(button.defaultLightColor))
            }
        }
    }
}