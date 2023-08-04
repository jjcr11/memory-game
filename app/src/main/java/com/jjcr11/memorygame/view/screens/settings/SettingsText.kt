package com.jjcr11.memorygame.view.screens.settings

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.jjcr11.memorygame.R

@Composable
fun SettingsText(text: String) {
    Text(
        text = text,
        color = colorResource(R.color.black),
        fontSize = dimensionResource(R.dimen.settings_text_size).value.sp,
        fontWeight = FontWeight.Bold
    )
}