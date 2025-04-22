package com.dualand.app.activities

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dualand.app.R
import com.example.animationdemo.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DuaScreen(
    innerPadding: PaddingValues,
    index: Int,
    navController: NavController
) {
    val systemUiController = rememberSystemUiController()
    val context = LocalContext.current
    val duas = duaList

    var currentIndex by remember { mutableStateOf(index.coerceIn(0, duas.lastIndex)) }

    val twoDuaIndices = setOf(0, 1, 2, 3, 6, 7, 15, 16, 18, 21, 22, 27, 28, 30, 31)
    val threeDuaIndices = setOf(18, 19, 20, 35, 36, 37)

    val showCount = when {
        currentIndex in threeDuaIndices -> 3
        currentIndex in twoDuaIndices -> 2
        else -> 1
    }

    val NavigationBarColor = colorResource(id = R.color.top_nav_new)
    val statusBarColor = colorResource(id = duas[currentIndex].statusBarColorResId)

    val MyArabicFont = FontFamily(Font(R.font.al_quran))
    val translationtext = FontFamily(Font(R.font.poppins_regular))
    val reference = FontFamily(Font(R.font.poppins_semibold))
    val title = FontFamily(Font(R.font.mochypop_regular))

    SideEffect {
        systemUiController.setStatusBarColor(color = statusBarColor)
        systemUiController.setNavigationBarColor(color = NavigationBarColor)
    }

    var currentPlayingIndex by remember { mutableStateOf(-1) }
    var globalWordIndex by remember { mutableStateOf(-1) }
    var isPlaying by remember { mutableStateOf(false) }
    var showListening by remember { mutableStateOf(false) }
    var globalMediaPlayer: MediaPlayer? by remember { mutableStateOf(null) }

    Box(modifier = Modifier.fillMaxHeight()) {
        Image(
            painter = painterResource(id = duas[currentIndex].backgroundResId),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.padding(start = 4.dp, top = 5.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.home_btn),
                        contentDescription = "Back",
                        modifier = Modifier.size(29.dp, 30.dp)
                    )
                }

                Text(
                    text = duas[currentIndex].textheading,
                    fontSize = 14.sp,
                    color = colorResource(R.color.heading_color),
                    fontFamily = title,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 10.dp)
                )

                IconButton(
                    onClick = { navController.navigate("SettingsScreen") },
                    modifier = Modifier.padding(end = 4.dp, top = 4.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.setting_btn),
                        contentDescription = "Settings",
                        modifier = Modifier.size(29.dp, 30.dp)
                    )
                }
            }

            Image(
                painter = painterResource(id = duas[currentIndex].image),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .padding(top = 5.dp)
            )

            DuaTabs()

            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 50.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(innerPadding)
                            .verticalScroll(rememberScrollState())
                    ) {
                        for (i in currentIndex until (currentIndex + showCount).coerceAtMost(duas.size)) {
                            val dua = duas[i]

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp),
                                horizontalArrangement = Arrangement.spacedBy(
                                    -10.dp,
                                    Alignment.CenterHorizontally
                                )
                            ) {
                                IconButton(onClick = {  }) {
                                    Image(
                                        painter = painterResource(id = R.drawable.favourite_icon),
                                        contentDescription = "Favourite",
                                        modifier = Modifier.size(33.dp)
                                    )
                                }

                                fun playWord(index: Int) {
                                    if (index >= dua.wordAudioPairs.size) {
                                        isPlaying = false
                                        showListening = false
                                        globalWordIndex = -1
                                        return
                                    }

                                    val (_, audioResId) = dua.wordAudioPairs[index]

                                    globalMediaPlayer?.release()
                                    globalMediaPlayer = MediaPlayer.create(context, audioResId)

                                    globalWordIndex = index
                                    showListening = false

                                    globalMediaPlayer?.apply {
                                        val rawDuration = duration
                                        val silencePadding = 500
                                        val effectiveDuration = (rawDuration - silencePadding).coerceAtLeast(100)

                                        setOnCompletionListener {
                                            showListening = true
                                            Handler(Looper.getMainLooper()).postDelayed({
                                                playWord(index + 1)
                                            }, effectiveDuration.toLong())
                                        }

                                        start()
                                    }
                                }

                                PlayWordByWordButton(
                                    isPlaying = isPlaying && currentPlayingIndex == i,
                                    showListening = showListening && currentPlayingIndex == i,
                                    onClick = {
                                        if (isPlaying && currentPlayingIndex == i) {
                                            globalMediaPlayer?.pause()
                                            isPlaying = false
                                        } else {
                                            globalMediaPlayer?.release()
                                            currentPlayingIndex = i
                                            globalWordIndex = -1
                                            isPlaying = true
                                            playWord(0)
                                        }
                                    }
                                )

                                IconButton(onClick = {
                                    globalMediaPlayer?.release()
                                    globalMediaPlayer = null
                                    isPlaying = false
                                    globalWordIndex = -1
                                }) {
                                    Image(
                                        painter = painterResource(id = R.drawable.repeat_icon),
                                        contentDescription = "Stop",
                                        modifier = Modifier.size(33.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(9.dp))

                            dua.steps?.let {
                                Text(
                                    text = it,
                                    fontSize = 12.sp,
                                    fontFamily = translationtext,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center,
                                    color = colorResource(R.color.heading_color),
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            }

                            val annotatedText = buildAnnotatedString {
                                dua.wordAudioPairs.forEachIndexed { index, pair ->
                                    pushStringAnnotation("WORD", index.toString())
                                    withStyle(
                                        style = SpanStyle(
                                            color = if (globalWordIndex == index && currentPlayingIndex == i)
                                                colorResource(R.color.highlited_color)
                                            else colorResource(R.color.arabic_color),
                                            fontWeight = if (globalWordIndex == index && currentPlayingIndex == i)
                                                FontWeight.Bold
                                            else FontWeight.Normal
                                        )
                                    ) {
                                        append(pair.first + " ")
                                    }
                                    pop()
                                }
                            }

                            Spacer(modifier = Modifier.height(5.dp))

                            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                                ClickableText(
                                    text = annotatedText,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 25.dp, end = 25.dp),
                                    style = TextStyle(
                                        fontSize = 24.sp,
                                        fontFamily = MyArabicFont,
                                        textDirection = TextDirection.Rtl,
                                        textAlign = TextAlign.Center
                                    ),
                                    onClick = { offset ->
                                        annotatedText.getStringAnnotations("WORD", offset, offset)
                                            .firstOrNull()?.let { annotation ->
                                                val clickedIndex = annotation.item.toInt()
                                                globalMediaPlayer?.release()
                                                globalMediaPlayer = MediaPlayer.create(
                                                    context,
                                                    dua.wordAudioPairs[clickedIndex].second
                                                )
                                                currentPlayingIndex = i
                                                globalWordIndex = clickedIndex
                                                globalMediaPlayer?.setOnCompletionListener {
                                                    globalWordIndex = -1
                                                }
                                                globalMediaPlayer?.start()
                                            }
                                    }
                                )
                            }

                            Spacer(modifier = Modifier.height(7.dp))

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = dua.translation,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center,
                                    fontFamily = translationtext,
                                    lineHeight = 18.sp,
                                    color = colorResource(R.color.translation_color),
                                    modifier = Modifier.padding(start = 20.dp, end = 20.dp)
                                )

                                Spacer(modifier = Modifier.height(7.dp))

                                Text(
                                    text = dua.reference,
                                    fontSize = 10.sp,
                                    fontFamily = reference,
                                    color = colorResource(R.color.reference_color),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .height(50.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.dua_bottom_bg),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.matchParentSize()
                    )

                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            val step = when {
                                (currentIndex - 1) in threeDuaIndices -> 3
                                (currentIndex - 1) in twoDuaIndices -> 2
                                else -> 1
                            }
                            currentIndex = (currentIndex - step).coerceAtLeast(0)
                        }) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_backarrow),
                                contentDescription = "Previous",
                                modifier = Modifier.size(29.dp, 30.dp)
                            )
                        }

                        IconButton(onClick = {
                            val step = when {
                                currentIndex in threeDuaIndices -> 3
                                currentIndex in twoDuaIndices -> 2
                                else -> 1
                            }
                            val newIndex = currentIndex + step
                            if (newIndex < duas.size) {
                                currentIndex = newIndex
                            }
                        }) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_nextarrow),
                                contentDescription = "Next",
                                modifier = Modifier.size(29.dp, 30.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlayWordByWordButton(
    isPlaying: Boolean,
    showListening: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier,
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(width = 60.dp, height = 50.dp)
        ) {
            val iconSize = 47.dp
            val iconRes = when {
                showListening -> R.drawable.icon_listening
                isPlaying -> R.drawable.pause_icon
                else -> R.drawable.icon_playy
            }

            Image(
                painter = painterResource(id = iconRes),
                contentDescription = "Play/Pause/Listening",
                modifier = Modifier.size(iconSize)
            )
        }
    }
}

@Composable
fun SettingsScreen(navController: NavController) {
    var selectedLanguage by remember { mutableStateOf("English") }
    var fontSize by remember { mutableStateOf(32f) }
    var isAutoPlayEnabled by remember { mutableStateOf(true) }

    val languages = listOf("English", "Urdu")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Header
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF4CAF50))
            }
            Text(
                text = if (selectedLanguage == "Urdu") "ترتیبات" else "Settings",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { /* Info logic */ }) {
                Icon(Icons.Default.Info, contentDescription = "Info", tint = Color(0xFF4CAF50))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Language selection
        Text(
            text = if (selectedLanguage == "Urdu") "پہلی زبان" else "Default Language",
            color = Color(0xFF00BCD4),
            fontWeight = FontWeight.Medium
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFF88)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Column {
                languages.forEach { lang ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = selectedLanguage == lang,
                                onClick = { selectedLanguage = lang }
                            )
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedLanguage == lang,
                            onClick = { selectedLanguage = lang }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (lang == "Urdu") "اردو" else "English"
                        )
                    }
                }
            }
        }


        Text(
            text = if (selectedLanguage == "Urdu") "فونٹ سیٹنگز" else "Font Settings",
            color = Color(0xFF00BCD4),
            fontWeight = FontWeight.Medium
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFF88)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Column(Modifier.padding(12.dp)) {
                Text(text = if (selectedLanguage == "Urdu") "فونٹ سائز" else "Font Size")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "اللّهُـمَّ باعِـدْ بَيْني وَبَيْنَ خَطاياي",
                    fontSize = fontSize.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Slider(
                    value = fontSize,
                    onValueChange = { fontSize = it },
                    valueRange = 16f..48f
                )
                Text(
                    text = fontSize.toInt().toString(),
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }

        // Playback Settings
        Text(
            text = if (selectedLanguage == "Urdu") "پلے بیک سیٹنگز" else "Playback Settings",
            color = Color(0xFF00BCD4),
            fontWeight = FontWeight.Medium
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFF88)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (selectedLanguage == "Urdu") "آٹو پلے" else "AutoPlay",
                    modifier = Modifier.weight(1f)
                )
                RadioButton(
                    selected = isAutoPlayEnabled,
                    onClick = { isAutoPlayEnabled = !isAutoPlayEnabled }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Save button
        Button(
            onClick = { /* Save logic here */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0097A7))
        ) {
            Text(
                text = if (selectedLanguage == "Urdu") "تبدیلیاں محفوظ کریں" else "Save changes",
                color = Color.White
            )
        }
    }
}

@Composable
fun DuaTabs(
    onWordByWordClick: () -> Unit = {},
    onCompleteDuaClick: () -> Unit = {}
) {
    val MyArabicFont = FontFamily(Font(R.font.doodlestrickers))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.complete_dua_word_by_word_dua_button),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onWordByWordClick() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "WORD BY WORD",
                    color = Color.White,
                    fontFamily = MyArabicFont,
                    fontSize = 18.sp
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onCompleteDuaClick() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "COMPLETE DUA",
                    color = Color.White,
                    fontFamily = MyArabicFont,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(navController = rememberNavController())
}

@Preview(showBackground = true)
@Composable
fun DuaScreenPreview() {
    val fakeNavController = rememberNavController()
    DuaScreen(index = 0, navController = fakeNavController, innerPadding = PaddingValues())
}



