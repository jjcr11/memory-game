package com.jjcr11.memorygame.view.screens.settings

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jjcr11.memorygame.R

@Composable
fun SettingsSourceCode() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(dimensionResource(R.dimen.setting_option_height))
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.settings_margin), 0.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.github_mark),
            contentDescription = null,
            modifier = Modifier
                .width(25.dp)
        )
        Text(
            text = "Source code",
            color = colorResource(R.color.black),
            fontSize = dimensionResource(R.dimen.settings_text_size).value.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(start = 20.dp)
        )
    }
}