package com.jjcr11.memorygame.view.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jjcr11.memorygame.R
import com.jjcr11.memorygame.model.BackdropItem

@Composable
fun SettingsBackdropItem(item: BackdropItem) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .clickable { }
            .fillMaxWidth()
            .height(50.dp)
            .padding(
                start = 30.dp,
                top = 10.dp,
                end = 30.dp,
                bottom = 10.dp
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(item.icon),
                contentDescription = null
            )
            Text(
                modifier = Modifier.padding(start = 40.dp),
                text = item.text,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        if (item.selected) {
            Icon(
                painter = painterResource(R.drawable.ic_check_circle_outline),
                contentDescription = null
            )
        }
    }
}