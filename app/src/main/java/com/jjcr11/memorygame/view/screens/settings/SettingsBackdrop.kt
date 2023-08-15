package com.jjcr11.memorygame.view.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.jjcr11.memorygame.R
import com.jjcr11.memorygame.model.BackdropItem

@Composable
fun SettingsBackdrop(items: List<BackdropItem>) {
    Card(
        backgroundColor = colorResource(R.color.white),
        shape = RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Column {
            items.forEach { SettingsBackdropItem(it) }
        }
    }
}