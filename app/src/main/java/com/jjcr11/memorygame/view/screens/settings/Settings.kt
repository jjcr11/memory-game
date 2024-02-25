package com.jjcr11.memorygame.view.screens.settings

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.activity.compose.BackHandler
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.google.gson.Gson
import com.jjcr11.memorygame.R
import com.jjcr11.memorygame.model.AppDatabase
import com.jjcr11.memorygame.model.BackdropItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@Composable
@Preview(showBackground = true)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
fun Settings() {
    val (visible, setVisible) = remember { mutableStateOf(false) }
    val (index, setIndex) = remember { mutableStateOf(0) }
    val (items, setItems) = remember { mutableStateOf(listOf<BackdropItem>()) }
    val (theme, setTheme) = remember { mutableStateOf("") }
    val (type, setType) = remember { mutableStateOf("") }
    val density = LocalDensity.current
    val activity = LocalContext.current as Activity
    val coroutineScope = rememberCoroutineScope()
    val sharedPreferences = activity.getSharedPreferences("settings", Context.MODE_PRIVATE)
    Box {
        Scaffold(
            topBar = { SettingsTopBar() }
        ) {
            Column(
                modifier = Modifier
                    .background(color = colorResource(R.color.white))
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
            ) {
                when (sharedPreferences.getInt("theme", 2)) {
                    0 -> setTheme("Light")
                    1 -> setTheme("Dark")
                    2 -> setTheme("System")
                }
                when (sharedPreferences.getInt("type", 0)) {
                    0 -> setType("Colors")
                    1 -> setType("Numbers")
                }
                SettingsOptions("App theme", theme) {
                    setVisible(true)
                    setIndex(0)
                }
                SettingsSlider(sharedPreferences)
                SettingsSwitch("Sounds effects", "sounds", sharedPreferences)
                SettingsSwitch("Vibration", "vibration", sharedPreferences)
                SettingsOptions("Button type", type) {
                    setVisible(true)
                    setIndex(1)
                }
                if (sharedPreferences.getInt("type", 0) == 0) {
                    SettingColor("colorButton1", "#DB070A", "#F94144", activity)
                    SettingColor("colorButton2", "#C14B0B", "#F3722C", activity)
                    SettingColor("colorButton3", "#C206B9", "#F820ED", activity)
                    SettingColor("colorButton4", "#E1A008", "#F9C74F", activity)
                    SettingColor("colorButton5", "#669443", "#90BE6D", activity)
                    SettingColor("colorButton6", "#4A00F8", "#8D5BFF", activity)
                    SettingColor("colorButton7", "#66433D", "#8F5E56", activity)
                    SettingColor("colorButton8", "#1C5872", "#277DA1", activity)
                }
                SettingsOptions("Export / import scores", "") {
                    setVisible(true)
                    setIndex(2)
                }
                SettingsSourceCode(activity)
            }
        }
        if (visible) {
            Card(
                backgroundColor = colorResource(R.color.black_transparent),
                shape = RoundedCornerShape(0.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { setVisible(false) }
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
            /*val aux = when (index) {
                0 -> listOf(
                    BackdropItem(R.drawable.ic_wb_sunny, "Light", false) {
                        Log.d("Siuuuuuuu", "Light")
                    },
                    BackdropItem(R.drawable.ic_dark_mode, "Dark", false) {
                        Log.d("Siuuuuuuu", "Dark")
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    },
                    BackdropItem(R.drawable.ic_phone_android, "System", true) {
                        Log.d("Siuuuuuuu", "System")
                    }
                )

                /*1 -> listOf(
                    BackdropItem(R.drawable.ic_color_lens, "Colors", false),
                    BackdropItem(R.drawable.ic_looks_one, "Numbers", false),
                    BackdropItem(R.drawable.ic_phone_android, "Both", true)
                )

                2 -> listOf(
                    BackdropItem(R.drawable.ic_send_to_mobile, "Export", false) {},
                    BackdropItem(R.drawable.ic_add_to_home_screen, "Import", false) {}
                )*/


                else -> listOf()
            }
            setItems(aux)
            SettingsBackdrop(items)*/
            var aux = listOf<BackdropItem>()
            when (index) {
                0 -> {
                    aux = setBackdropTheme(sharedPreferences, setVisible)
                    when (sharedPreferences.getInt("theme", 2)) {
                        0 -> aux[0].selected = true
                        1 -> aux[1].selected = true
                        2 -> aux[2].selected = true
                    }
                }

                1 -> {
                    aux = setBackdropType(sharedPreferences, setVisible)
                    when (sharedPreferences.getInt("type", 0)) {
                        0 -> aux[0].selected = true
                        1 -> aux[1].selected = true
                    }
                }

                2 -> aux = setBackdropExport(sharedPreferences, activity, coroutineScope, setVisible)
            }
            setItems(aux)
            SettingsBackdrop(items)
        }
    }
}

fun setBackdropTheme(
    sharedPreferences: SharedPreferences,
    setVisible: (Boolean) -> Unit
): List<BackdropItem> = listOf(
    BackdropItem(R.drawable.ic_wb_sunny, "Light", false) {
        sharedPreferences.edit()?.putInt("theme", 0)?.apply()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setVisible(false)
    },
    BackdropItem(R.drawable.ic_dark_mode, "Dark", false) {
        sharedPreferences.edit()?.putInt("theme", 1)?.apply()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        setVisible(false)
    },
    BackdropItem(R.drawable.ic_phone_android, "System", false) {
        sharedPreferences.edit()?.putInt("theme", 2)?.apply()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        setVisible(false)
    }
)

fun setBackdropType(
    sharedPreferences: SharedPreferences,
    setVisible: (Boolean) -> Unit
): List<BackdropItem> = listOf(
    BackdropItem(R.drawable.ic_color_lens, "Colors", false) {
        sharedPreferences.edit()?.putInt("type", 0)?.apply()
        setVisible(false)
    },
    BackdropItem(R.drawable.ic_looks_one, "Numbers", false) {
        sharedPreferences.edit()?.putInt("type", 1)?.apply()
        setVisible(false)
    }
)

fun setBackdropExport(
    sharedPreferences: SharedPreferences,
    activity: Activity,
    coroutineScope: CoroutineScope,
    setVisible: (Boolean) -> Unit
): List<BackdropItem> = listOf(
    BackdropItem(R.drawable.ic_send_to_mobile, "Export", false) {

        coroutineScope.launch(Dispatchers.IO) {
            val scores =
                AppDatabase.getDatabase(activity).dao().getAllScoresByScore()
            val content = Gson().toJson(scores)
            val file = File(activity.filesDir, "memory-game-backup.json")
            file.writeText(content)
            val intent: Intent = Intent().let {
                it.action = Intent.ACTION_SEND
                it.putExtra(
                    Intent.EXTRA_STREAM, FileProvider.getUriForFile(
                        activity,
                        activity.applicationContext.packageName + ".provider", file
                    )
                )
                it.type = "text/json"
                it
            }
            startActivity(activity, Intent.createChooser(intent, null), null)
            setVisible(false)
        }


    },
    BackdropItem(R.drawable.ic_add_to_home_screen, "Import", false) {
        setVisible(false)
    }
)