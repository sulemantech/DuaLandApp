package com.dualand.app.activities

import android.content.Context
import com.dualand.app.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dualand.app.DuaViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.runtime.collectAsState
@Composable
fun SettingsScreen(navController: NavController, innerPadding: PaddingValues,duaViewModel: DuaViewModel = viewModel()) {

    val readTitleEnabled by duaViewModel.readTitleEnabled.collectAsState()
    val rewardsEnabled by duaViewModel.rewardsEnabled.collectAsState()
    val autoNextDuasEnabled by duaViewModel.autoNextEnabled.collectAsState()
    val wordByWordPauseEnabled by duaViewModel.wordByWordPauseEnabled.collectAsState()
    val pauseSeconds by duaViewModel.pauseSeconds.collectAsState()
    val selectedVoice by duaViewModel.selectedVoice.collectAsState()
    val fontSize by duaViewModel.fontSize.collectAsState()

    val context = LocalContext.current
    val systemUiController = rememberSystemUiController()
    var wordByWordPause by remember { mutableStateOf(2) }

    val MyArabicFont = FontFamily(Font(R.font.lateef_regular))
    val text_font = FontFamily(Font(R.font.montserrat_regular))
    val text_font1 = FontFamily(Font(R.font.doodlestrickers))
    val settings = FontFamily(Font(R.font.mochypop_regular))

    val sharedPref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val savedVoice = sharedPref.getString("selected_voice", "Female")
    //val selectedVoice = remember { mutableStateOf(savedVoice ?: "Female") }

    val NavigationBarColor = colorResource(id = R.color.background_screen)
    val statusBarColor = colorResource(id = R.color.top_nav_new)

    val someSetting by duaViewModel.someSettingState

    //val fontSize = remember { mutableStateOf(sharedPref.getFloat("font_size", 24f)) }

    val scrollState = rememberScrollState()
    val toggleOptions =
        listOf("Reading Out Dua Title", "Rewards", "Auto Next Dua's", "Word-by-Word Pause")

    val savedLanguages =
        remember { mutableStateOf(LanguagePreferences.getSelectedLanguages(context)) }
    val selectedLanguages = remember { mutableStateListOf(*savedLanguages.value.toTypedArray()) }

//    var readTitleEnabled by remember {
//        mutableStateOf(
//            context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
//                .getBoolean("read_title_enabled", false)
//        )
//    }

//    var rewardsEnabled by remember {
//        mutableStateOf(
//            context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
//                .getBoolean("rewards_enabled", false)
//        )
//    }

//    var autoNextDuasEnabled by remember {
//        mutableStateOf(
//            context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
//                .getBoolean("auto_next_duas_enabled", false)
//        )
//    }
    var WordbyWordPauseEnabled by remember {
        mutableStateOf(
            context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                .getBoolean("Word_by_Word_Pause_Enabled", false)
        )
    }
//    var pauseSeconds by remember {
//        mutableStateOf(sharedPref.getInt("word_by_word_pause_seconds", 2))
//    }

    SideEffect {
        systemUiController.setStatusBarColor(color = statusBarColor)
        systemUiController.setNavigationBarColor(color = NavigationBarColor)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(colorResource(R.color.background_screen))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(R.color.top_nav_new)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(start = 4.dp, top = 5.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_backarrow),
                    contentDescription = "Back",
                    modifier = Modifier.size(29.dp, 30.dp)
                )
            }
            Text(
                text = "Settings",
                fontSize = 16.sp,
                fontFamily = settings,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.heading_color),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 6.dp)
            )
            Spacer(modifier = Modifier.width(48.dp))
        }

        Spacer(modifier = Modifier.height(17.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Dua Translation",
                        fontFamily = text_font,
                        fontWeight = FontWeight.W600,
                        color = colorResource(R.color.heading_color)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    listOf("English").forEach { lang ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Image(
                                painter = painterResource(
                                    id = R.drawable.eng_flag // Only using English flag for now
                                    // id = when (lang) {
                                    //     "English" -> R.drawable.eng_flag
                                    //     "Urdu" -> R.drawable.urdu_flag
                                    //     else -> R.drawable.hindi_flag
                                    // }
                                ),
                                contentDescription = lang,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                lang,
                                fontSize = 16.sp,
                                fontFamily = text_font,
                                fontWeight = FontWeight.W500
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Checkbox(
                                checked = selectedLanguages.contains(lang),
                                onCheckedChange = {
                                    if (it) selectedLanguages.add(lang) else selectedLanguages.remove(
                                        lang
                                    )
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = colorResource(R.color.check_box),
                                    uncheckedColor = Color.Gray,
                                    checkmarkColor = Color.White
                                )
                            )
                        }
                    }

                    Divider(Modifier.padding(vertical = 4.dp))

                    val context = LocalContext.current
                    val sharedPref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Font Size",
                            fontFamily = text_font,
                            fontWeight = FontWeight.W600,
                            color = colorResource(R.color.heading_color),
                            fontSize = 14.sp
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = {
                                if (fontSize > 10) {
                                    duaViewModel.setFontSize(fontSize - 2f)

                                    // Auto-save font size
                                    sharedPref.edit()
                                        .putFloat("font_size", fontSize)
                                        .apply()
                                }
                            }) {
                                Image(
                                    painter = painterResource(id = R.drawable.minus_icon),
                                    contentDescription = "Minus"
                                )
                            }

                            Text(
                                text = "$fontSize",
                                fontSize = 20.sp,
                                fontFamily = text_font,
                                color = colorResource(R.color.heading_color),
                                fontWeight = FontWeight.W700
                            )

                            IconButton(onClick = {
                                //duaViewModel.fontSize  += 2f
                                duaViewModel.setFontSize(fontSize + 2f)
                                // Auto-save font size
                                sharedPref.edit()
                                    .putFloat("font_size", fontSize)
                                    .apply()
                            }) {
                                Image(
                                    painter = painterResource(id = R.drawable.plus_icon),
                                    contentDescription = "Plus"
                                )
                            }
                        }
                    }


                    Text(
                        text = "سُبْحَانَ اللّٰہِ",
                        fontSize = fontSize.sp,
                        fontFamily = MyArabicFont,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Divider(Modifier.padding(vertical = 4.dp))

                    toggleOptions.forEach { title ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp)
                            ) {
                                Text(
                                    text = title,
                                    fontFamily = text_font,
                                    fontWeight = FontWeight.W600,
                                    color = colorResource(R.color.heading_color),
                                    fontSize = 15.sp,
                                    lineHeight = 15.sp
                                )
                                if (title != "Word-by-Word Pause") {
                                    Text(
                                        text = when (title) {
                                            "Reading Out Dua Title" -> "Reads out the dua title automatically"
                                            "Rewards" -> "Gives a reward when a dua is completed"
                                            "Auto Next Dua’s" -> "Automatically move to the next dua"
                                            else -> ""
                                        },
                                        fontSize = 10.sp,
                                        lineHeight = 12.sp,
                                        fontFamily = text_font,
                                        color = colorResource(R.color.heading_color)
                                    )
                                }
                            }

                            Switch(
                                checked = when (title) {
                                    "Reading Out Dua Title" -> readTitleEnabled
                                    "Rewards" -> rewardsEnabled
                                    "Auto Next Dua's" -> autoNextDuasEnabled
                                    "Word-by-Word Pause" -> WordbyWordPauseEnabled
                                    else -> false
                                },
                                onCheckedChange = { isChecked ->
                                    when (title) {
                                        "Reading Out Dua Title" -> {
                                            duaViewModel.setReadTitleEnabled(isChecked)
                                            sharedPref.edit().putBoolean("read_title_enabled", isChecked).apply()
                                        }
                                        "Rewards" -> {
                                            duaViewModel.setRewardsEnabled(isChecked)
                                            sharedPref.edit().putBoolean("rewards_enabled", isChecked).apply()
                                        }
                                        "Auto Next Dua's" -> {
                                            duaViewModel.setAutoNextEnabled(isChecked)
                                            sharedPref.edit().putBoolean("auto_next_duas_enabled", isChecked).apply()
                                        }
                                        "Word-by-Word Pause" -> {
                                            duaViewModel.setWordByWordPauseEnabled(isChecked)
                                            sharedPref.edit().putBoolean("word_by_word_pause_enabled", isChecked).apply()
                                        }
                                    }
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = colorResource(R.color.check_box),
                                    uncheckedThumbColor = colorResource(R.color.uncheckedThumbColor),
                                    uncheckedTrackColor = colorResource(R.color.white)
                                )
                            )
                        }

                        if (title == "Word-by-Word Pause") {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                IconButton(
                                    onClick = {
                                        if (pauseSeconds > 1) {
                                            duaViewModel.setPauseSeconds(pauseSeconds-1)
                                            sharedPref.edit().putInt("word_by_word_pause_seconds", pauseSeconds).apply()
                                        }
                                    },
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.minus_icon),
                                        contentDescription = "Minus"
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    "$pauseSeconds sec",
                                    fontSize = 15.sp,
                                    fontFamily = text_font,
                                    color = colorResource(R.color.heading_color),
                                    fontWeight = FontWeight.W700
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                IconButton(
                                    onClick = {
                                        duaViewModel.setPauseSeconds(pauseSeconds+1)
                                        sharedPref.edit().putInt("word_by_word_pause_seconds", pauseSeconds).apply()
                                    },
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.plus_icon),
                                        contentDescription = "Plus"
                                    )
                                }
                            }
                        }

                        Divider(Modifier.padding(vertical = 4.dp))
                    }

                    Spacer(modifier = Modifier.height(17.dp))

                    Text(
                        "Choose Voice",
                        fontFamily = text_font,
                        fontWeight = FontWeight.W600,
                        color = colorResource(R.color.heading_color)
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf("Male", "Female").forEach { gender ->
                            Box(
                                modifier = Modifier
                                    .border(
                                        1.dp,
                                        if (selectedVoice == gender) colorResource(R.color.check_box) else Color.Transparent,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .padding(
                                        start = 25.dp,
                                        end = 25.dp,
                                        top = 15.dp,
                                        bottom = 10.dp
                                    )
                                    .clickable { duaViewModel.setSelectedVoice(gender) }
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Image(
                                        painter = painterResource(
                                            id = if (gender == "Male") R.drawable.male_icon else R.drawable.female_icon
                                        ),
                                        contentDescription = gender,
                                        modifier = Modifier.size(60.dp)
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(gender, fontFamily = text_font)
                                }
                                if (selectedVoice == gender) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_check_circle),
                                        contentDescription = "Selected",
                                        modifier = Modifier
                                            .size(16.dp)
                                            .align(Alignment.TopEnd)
                                            .offset(x = 17.dp, y = (-6).dp)
                                    )
                                }
                            }
                        }
                    }

                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(0.9f)
                    .height(46.dp)
                    .clickable {
                        CoroutineScope(Dispatchers.IO).launch {
                            LanguagePreferences.saveLanguages(context, selectedLanguages.toSet())

                            val sharedPref =
                                context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                            sharedPref
                                .edit()
                                .putBoolean("read_title_enabled", readTitleEnabled)
                                .putBoolean("rewards_enabled", rewardsEnabled)
                                .putBoolean("auto_next_duas_enabled", autoNextDuasEnabled)
                                .putBoolean("Word_by_Word_Pause_Enabled", WordbyWordPauseEnabled)
                                .putInt("word_by_word_pause_seconds", pauseSeconds)
                                .putStringSet("selected_languages", selectedLanguages.toSet())
                                .putString("selected_voice", selectedVoice)
                                .apply()

                            withContext(Dispatchers.Main) {
                                navController.popBackStack()
                            }
                        }
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.save_changes_btn),
                    contentDescription = "Save",
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "SAVE CHANGES",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontFamily = text_font1,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(navController = rememberNavController(), innerPadding = PaddingValues())
}
