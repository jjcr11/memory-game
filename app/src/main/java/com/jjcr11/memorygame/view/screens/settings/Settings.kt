package com.jjcr11.memorygame.view.screens.settings

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jjcr11.memorygame.R
import com.jjcr11.memorygame.model.BackdropItem

@Composable
@Preview(showBackground = true)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
fun Settings() {
    val (visible, setVisible) = remember { mutableStateOf(false) }
    val (index, setIndex) = remember { mutableStateOf(0) }
    val (items, setItems) = remember { mutableStateOf(listOf<BackdropItem>()) }
    val density = LocalDensity.current
    Box {
        Scaffold(
            topBar = { SettingsTopBar() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                SettingsOptions("App theme", "Light") {
                    setVisible(true)
                    setIndex(0)
                }
                SettingsSlider()
                SettingsSwitch("Sounds effects")
                SettingsSwitch("Vibration")
                SettingsOptions("Button type", "Colors") {
                    setVisible(true)
                    setIndex(1)
                }
                SettingColor("FFF94144")
                SettingColor("FFF3722C")
                SettingColor("FFF820ED")
                SettingColor("FFF9C74F")
                SettingColor("FF90BE6D")
                SettingColor("FF8D5BFF")
                SettingColor("FF8F5E56")
                SettingColor("FF277DA1")
                SettingsOptions("Export / import scores", "") {
                    setVisible(true)
                    setIndex(2)
                }
                SettingsSourceCode()
            }
        }
        if (visible) {
            Card(
                backgroundColor = colorResource(R.color.black_transparent),
                shape = RoundedCornerShape(0.dp),
                modifier = Modifier
                    .fillMaxSize()
            ) {}
            BackHandler {
                setVisible(false)
            }
        }
        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            visible = visible,
            enter = slideInVertically {
                with(density) { 200.dp.roundToPx() }
            },
            exit = slideOutVertically {
                with(density) { 200.dp.roundToPx() }
            }
        ) {
            val aux = when (index) {
                0 -> listOf(
                    BackdropItem(R.drawable.ic_wb_sunny, "Light", false),
                    BackdropItem(R.drawable.ic_dark_mode, "Dark", false),
                    BackdropItem(R.drawable.ic_phone_android, "System", true)
                )

                1 -> listOf(
                    BackdropItem(R.drawable.ic_color_lens, "Colors", false),
                    BackdropItem(R.drawable.ic_looks_one, "Numbers", false),
                    BackdropItem(R.drawable.ic_phone_android, "Both", true)
                )

                2 -> listOf(
                    BackdropItem(R.drawable.ic_send_to_mobile, "Export", false),
                    BackdropItem(R.drawable.ic_add_to_home_screen, "Import", false)
                )


                else -> listOf()
            }
            setItems(aux)
            SettingsBackdrop(items)
        }
    }
}