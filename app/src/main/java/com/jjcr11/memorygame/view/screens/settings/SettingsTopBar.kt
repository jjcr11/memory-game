package com.jjcr11.memorygame.view.screens.settings

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import com.jjcr11.memorygame.R

@Composable
fun SettingsTopBar() {
    TopAppBar(
        backgroundColor = colorResource(R.color.cerulean),
        title = {
            Text(
                text = "Settings",
                color = colorResource(R.color.white)
            )
        },
        actions = {
            IconButton(onClick = { }) {
                Icon(
                    painter = painterResource(R.drawable.ic_refresh),
                    contentDescription = null,
                    tint = colorResource(R.color.white)
                )
            }
        },
    )
}