package com.jjcr11.memorygame.view.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview(showBackground = true)
fun Settings() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        SettingsOptions("App theme", "Light")
        SettingsSlider()
        SettingsSwitch("Sounds effects")
        SettingsSwitch("Vibration")
        SettingsOptions("Button type", "Colors")
        SettingColor("FFF94144")
        SettingColor("FFF3722C")
        SettingColor("FFF820ED")
        SettingColor("FFF9C74F")
        SettingColor("FF90BE6D")
        SettingColor("FF8D5BFF")
        SettingColor("FF8F5E56")
        SettingColor("FF277DA1")
        SettingsOptions("Export / import scores", "")
        SettingsSourceCode()
    }
}



