package com.jjcr11.memorygame.view.screens.settings

import android.content.SharedPreferences
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.jjcr11.memorygame.R

@Composable
fun SettingsSwitch(text: String, preferenceName:String ,sharedPreferences: SharedPreferences) {
    val (checked, setChecked) = remember { mutableStateOf(true) }
    setChecked(sharedPreferences.getBoolean(preferenceName, true))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .height(dimensionResource(R.dimen.setting_option_height))
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.settings_margin), 0.dp)
    ) {
        SettingsText(text = text)
        Switch(
            checked = checked,
            onCheckedChange = {
                setChecked(it)
                sharedPreferences.edit()?.putBoolean(preferenceName, it)?.apply()
            }
        )
    }
}