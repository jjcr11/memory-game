package com.jjcr11.memorygame.view.screens.settings

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.jjcr11.memorygame.R
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.flag.BubbleFlag
import com.skydoves.colorpickerview.flag.FlagMode
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener

@Composable
fun SettingColor(
    tag: String,
    defaultLightColor: String,
    defaultDarkColor: String,
    activity: Activity
) {
    var colors = activity.getSharedPreferences("lightColors", Context.MODE_PRIVATE)
    val (color, setColor) = remember { mutableStateOf("") }
    when (activity.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
        Configuration.UI_MODE_NIGHT_YES -> {
            colors = activity.getSharedPreferences("darkColors", Context.MODE_PRIVATE)
            setColor(colors.getString(tag, defaultDarkColor)!!)
        }

        Configuration.UI_MODE_NIGHT_NO -> {
            colors = activity.getSharedPreferences("lightColors", Context.MODE_PRIVATE)
            setColor(colors.getString(tag, defaultLightColor)!!)
        }

        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
            colors = activity.getSharedPreferences("lightColors", Context.MODE_PRIVATE)
            setColor(colors.getString(tag, defaultLightColor)!!)
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .height(dimensionResource(R.dimen.setting_option_height))
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.settings_margin), 0.dp)
            .clickable(onClick = {
                val builder = ColorPickerDialog
                    .Builder(activity)
                    .setTitle("Select a color")
                    .setPositiveButton("Select", ColorEnvelopeListener { envelope, _ ->
                        val newColor = "#${envelope.hexCode.takeLast(6)}"
                        setColor(newColor)
                        colors.edit()?.putString(tag, newColor)?.apply()
                        //materialCardView.setCardBackgroundColor(envelope.color)
                    })
                    .setNegativeButton("Cancel") { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
                    .attachAlphaSlideBar(false)
                val colorPickerView = builder.colorPickerView
                val bubbleFlag = BubbleFlag(activity)
                //val color = materialCardView.cardBackgroundColor.defaultColor
                bubbleFlag.flagMode = FlagMode.ALWAYS
                colorPickerView.flagView = bubbleFlag
                //colorPickerView.setInitialColor(color)
                builder.show()
            })
    ) {
        Card(
            backgroundColor = Color("FF${color.takeLast(6)}".toLong(radix = 16)),
            modifier = Modifier
                .width(dimensionResource(R.dimen.small_button_side))
                .height(dimensionResource(R.dimen.small_button_side))
        ) {}
        SettingsText(text = "#${color.takeLast(6)}")
    }
}