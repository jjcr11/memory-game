package com.jjcr11.memorygame.view.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.jjcr11.memorygame.R

@Composable
fun SettingsSlider() {
    val (sliderValue, setSliderValue) = remember { mutableStateOf(0f) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.settings_margin), 0.dp)
    ) {
        SettingsText(text = "Don't save scores under:")
        SettingsText(text = "${sliderValue.toInt()}")
    }
    Slider(
        value = sliderValue,
        valueRange = 0f..30f,
        steps = 30,
        onValueChange = {
            setSliderValue(it)
        },
        colors = SliderDefaults.colors(
            activeTrackColor = colorResource(R.color.orange_crayola),
            thumbColor = colorResource(R.color.orange_crayola),
            inactiveTickColor = colorResource(R.color.transparent),
            activeTickColor = colorResource(R.color.transparent)
        ),
        modifier = Modifier
            .padding(dimensionResource(R.dimen.settings_margin), 0.dp)
    )
}