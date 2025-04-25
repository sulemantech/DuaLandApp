package com.dualand.app.activities

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
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
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.text.font.FontWeight.Companion.W700
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
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DuaScreen(
    innerPadding: PaddingValues,
    index: Int,
    navController: NavController
) {
    val systemUiController = rememberSystemUiController()
    val duas = duaList
    val context = LocalContext.current
    var mediaPlayer: MediaPlayer? by remember { mutableStateOf(null) }


    var currentIndex by remember { mutableStateOf(index.coerceIn(0, duas.lastIndex)) }

    val selectedLanguages = remember { mutableStateOf(LanguagePreferences.getLanguages(context)) }

    LaunchedEffect(selectedLanguages.value) {
        selectedLanguages.value = LanguagePreferences.getLanguages(context)
    }

    val twoDuaIndices = setOf(0, 1, 2, 3, 6, 7, 15, 16, 19, 21, 22, 23, 28, 29, 31, 32)
    val threeDuaIndices = setOf(19, 20, 21, 36, 37, 38)

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
    var repeatCount by remember { mutableStateOf(0) }
    var currentRepeat by remember { mutableStateOf(0) }
    var isRepeatingNow by remember { mutableStateOf(false) }
    var isRepeatMode by remember { mutableStateOf(false) }
    var currentlyRepeatingDuaIndex by remember { mutableStateOf(-1) }

    val stopAudioPlayback = {
        globalMediaPlayer?.apply {
            if (isPlaying) {
                stop()
                release()
            }
        }
        globalMediaPlayer = null
        isPlaying = false
        showListening = false
        globalWordIndex = -1
    }

    LaunchedEffect(currentIndex) {
        stopAudioPlayback()
        repeatCount = 0
        currentRepeat = 0
        isRepeatMode = false
    }
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

                Row(
                    modifier = Modifier.padding(horizontal = 6.dp)
                ) {
                    Text(
                        text = duas[currentIndex].duaNumber,
                        fontSize = 14.sp,
                        color = colorResource(R.color.heading_color),
                        fontFamily = title,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 10.dp)
                    )

                    Text(
                        text = duas[currentIndex].textheading,
                        fontSize = 14.sp,
                        color = colorResource(R.color.heading_color),
                        fontFamily = title,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }


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

            fun playAudio(audioResId: Int) {
                stopAudioPlayback()
                globalMediaPlayer = MediaPlayer.create(context, audioResId).apply {
                    start()
                    setOnCompletionListener {
                        release()
                        globalMediaPlayer = null
                    }
                }
            }
            DuaTabs(
                dua = duas[currentIndex],
                onCompleteDuaClick = { audioResId ->
                    playAudio(audioResId)
                }
            )

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
                                IconButton(onClick = { }) {
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
                                        val effectiveDuration =
                                            (rawDuration - silencePadding).coerceAtLeast(100)

                                        setOnCompletionListener {
                                            showListening = true
                                            Handler(Looper.getMainLooper()).postDelayed({
                                                if (index + 1 < dua.wordAudioPairs.size) {
                                                    playWord(index + 1)
                                                } else {
                                                    if (isRepeatMode && (repeatCount == Int.MAX_VALUE || currentRepeat < repeatCount)) {
                                                        currentRepeat++
                                                        isRepeatingNow = true
                                                        currentlyRepeatingDuaIndex =
                                                            currentPlayingIndex
                                                        playWord(0)
                                                    } else {
                                                        isPlaying = false
                                                        showListening = false
                                                        globalWordIndex = -1
                                                        isRepeatMode = false
                                                        repeatCount = 0
                                                        currentRepeat = 0
                                                        isRepeatingNow = false
                                                        currentlyRepeatingDuaIndex = -1
                                                    }
                                                }
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

                                Box(
                                    modifier = Modifier.padding(4.dp)
                                ) {
                                    IconButton(
                                        onClick = {
                                            if (repeatCount < 5) {
                                                repeatCount++
                                            } else {
                                                repeatCount = Int.MAX_VALUE
                                            }
                                            isRepeatMode = true
                                            currentRepeat = 0
                                        },
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.repeat_off_btn),
                                            contentDescription = "Repeat",
                                            modifier = Modifier.size(33.dp)
                                        )
                                    }

                                    if (isRepeatingNow && currentPlayingIndex == i && repeatCount > 0) {
                                        val badgeText =
                                            if (repeatCount == Int.MAX_VALUE) "∞" else repeatCount.toString()

                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier
                                                .size(18.dp)
                                                .background(Color.Red, shape = CircleShape)
                                                .align(Alignment.TopEnd)
                                                .offset(x = (-2).dp, y = 2.dp)
                                        ) {
                                            Text(
                                                text = badgeText,
                                                color = Color.White,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

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

//                                if ("Urdu" in selectedLanguages.value) {
//                                    Text(
//                                        text = dua.urdu,
//                                        fontSize = 14.sp,
//                                        textAlign = TextAlign.Center,
//                                        lineHeight = 18.sp,
//                                        color = colorResource(R.color.translation_color),
//                                        modifier = Modifier.padding(horizontal = 20.dp)
//                                    )
//                                    Spacer(modifier = Modifier.height(7.dp))
//                                }

                                    Text(
                                        text = dua.hinditranslation,
                                        fontSize = 14.sp,
                                        textAlign = TextAlign.Center,
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
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(bottom = 20.dp)
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
                            stopAudioPlayback()
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
                            stopAudioPlayback()
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
fun SettingsScreen(navController: NavController, innerPadding: PaddingValues) {
    val selectedLanguages = remember { mutableStateListOf("English", "Urdu") }
    val fontSize = remember { mutableStateOf(32f) }
    val pauseSeconds = remember { mutableStateOf(2) }
    val selectedVoice = remember { mutableStateOf("Male") }
    val MyArabicFont = FontFamily(Font(R.font.lateef_regular))
    var wordByWordPause by remember { mutableStateOf(2) }
    val MyArabicFont1 = FontFamily(Font(R.font.doodlestrickers))
    val systemUiController = rememberSystemUiController()
    val translationtext = FontFamily(Font(R.font.poppins_regular))
    val text_font = FontFamily(Font(R.font.montserrat_regular))
    val settings = FontFamily(Font(R.font.mochypop_regular))

    val toggleOptions =
        listOf("Reading Out Dua Title", "Rewards", "Auto Next Dua's", "Word-by-Word Pause")
    val scrollState = rememberScrollState()

    val NavigationBarColor = colorResource(id = R.color.background_screen)
    val statusBarColor = colorResource(id = R.color.top_nav_new)

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
            modifier = Modifier.fillMaxWidth()
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
            Row(
                modifier = Modifier.padding(horizontal = 6.dp)
            ) {
                Text(
                    text = "Settings",
                    fontSize = 16.sp,
                    fontFamily = settings,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.heading_color),
                    textAlign = TextAlign.Center,
                )
            }
            IconButton(
                onClick = { navController.navigate("SettingsScreen") },
                modifier = Modifier.padding(end = 4.dp, top = 4.dp)
            ) {
            }
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
                    Text("Dua Translation", fontFamily = text_font, fontWeight = W600, color = colorResource(R.color.heading_color))

                    val selectedLanguages = remember { mutableStateListOf<String>() }

                    listOf("English", "Urdu", "Hindi").forEach { lang ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Image(
                                painter = painterResource(
                                    id = when (lang) {
                                        "English" -> R.drawable.eng_flag
                                        "Urdu" -> R.drawable.urdu_flag
                                        else -> R.drawable.hindi_flag
                                    }
                                ),
                                contentDescription = lang,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(lang, fontSize = 16.sp)
                            Spacer(modifier = Modifier.weight(1f))
                            Checkbox(
                                checked = selectedLanguages.contains(lang),
                                onCheckedChange = {
                                    if (it) selectedLanguages.add(lang) else selectedLanguages.remove(lang)
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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Font Size",  fontFamily = text_font, fontWeight = W600, color = colorResource(R.color.heading_color), fontSize = 14.sp)

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { if (fontSize.value > 10) fontSize.value -= 2f }) {
                                Image(
                                    painter = painterResource(id = R.drawable.minus_icon),
                                    contentDescription = "Minus"
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))

                            Text(text = "${fontSize.value.toInt()}", fontSize = 20.sp, fontFamily = text_font,color = colorResource(R.color.heading_color), fontWeight = W700)

                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(onClick = { fontSize.value += 2f }) {
                                Image(
                                    painter = painterResource(id = R.drawable.plus_icon),
                                    contentDescription = "Plus"
                                )
                            }
                        }
                    }

                    Text(
                        text = "سُبْحَانَ اللّٰہِ",
                        fontSize = fontSize.value.sp,
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
                            Column(Modifier.weight(1f)) {
                                Text(title,  fontFamily = text_font, fontWeight = W600, color = colorResource(R.color.heading_color), fontSize = 15.sp)
                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    when (title) {
                                        "Reading Out Dua Title" -> "Reads out the dua title automatically"
                                        "Rewards" -> "Gives a reward when a dua is completed"
                                        "Auto Next Dua's" -> "Automatically move to the next dua"
                                        "Word-by-Word Pause" -> ""
                                        else -> ""
                                    },
                                    fontSize = 10.sp,
                                    color = colorResource(R.color.heading_color)
                                )
                            }
                            var isSwitchOn by remember { mutableStateOf(false) }
                            Switch(
                                checked = isSwitchOn,
                                onCheckedChange = { isSwitchOn = it },
                                modifier = Modifier.scale(1.0f),
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = colorResource(R.color.check_box),
                                    uncheckedThumbColor = colorResource(R.color.uncheckedThumbColor),
                                    uncheckedTrackColor = colorResource(R.color.white)
                                )
                            )
                        }
                        if (title != "Word-by-Word Pause") {
                            Divider(Modifier.padding(vertical = 6.dp))
                        }

                        if (title == "Word-by-Word Pause") {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.align(Alignment.Center)
                                ) {
                                    IconButton(onClick = { if (wordByWordPause > 1) wordByWordPause-- }) {
                                        Image(
                                            painter = painterResource(id = R.drawable.minus_icon),
                                            contentDescription = "Minus"
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("$wordByWordPause sec", fontFamily = text_font, fontWeight = W700,color = colorResource(R.color.heading_color),fontSize=15.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    IconButton(onClick = { wordByWordPause++ }) {
                                        Image(
                                            painter = painterResource(id = R.drawable.plus_icon),
                                            contentDescription = "Plus"
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Divider(Modifier.padding(vertical = 6.dp))
                    Spacer(modifier = Modifier.height(17.dp))

                    Text("Choose Voice",  fontFamily = text_font, fontWeight = W600, color = colorResource(R.color.heading_color))
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
                                        if (selectedVoice.value == gender) colorResource(R.color.check_box) else Color.Transparent,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .padding(
                                        top = 15.dp,
                                        start = 25.dp,
                                        end = 25.dp,
                                        bottom = 10.dp
                                    )
                                    .clickable { selectedVoice.value = gender }
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.align(Alignment.Center)
                                ) {
                                    Image(
                                        painter = painterResource(
                                            id = if (gender == "Male") R.drawable.male_icon else R.drawable.female_icon
                                        ),
                                        contentDescription = gender,
                                        modifier = Modifier.size(60.dp)
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(gender)
                                }
                                if (selectedVoice.value == gender) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_check_circle),
                                        contentDescription = "Selected",
                                        modifier = Modifier
                                            .size(16.dp)
                                            .align(Alignment.TopEnd)
                                            .offset(
                                                x = 17.dp,
                                                y = (-6).dp
                                            )
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(17.dp))

                        }
                    }
                }
            }

            val context = LocalContext.current

            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(0.9f)
                    .height(46.dp)
                    .clickable {
                        CoroutineScope(Dispatchers.IO).launch {
                            LanguagePreferences.saveLanguages(context, selectedLanguages.toSet())
                            withContext(Dispatchers.Main) {
                                navController.popBackStack()
                            }
                        }
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.save_changes_btn),
                    contentDescription = "Background",
                    modifier = Modifier.fillMaxSize()
                )
                Text(
                    text = "SAVE CHANGES",
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    fontFamily = MyArabicFont1,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

        }
    }
}

@Composable
fun DuaTabs(
    dua: Dua,
    onWordByWordClick: () -> Unit = {},
    onCompleteDuaClick: (Int) -> Unit = {}
) {
    val MyArabicFont = FontFamily(Font(R.font.doodlestrickers))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.complete_dua_word_by_word_dua_button),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp),
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
                    .clickable {
                        onCompleteDuaClick(dua.fullAudioResId)
                    },
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
    SettingsScreen(navController = rememberNavController(), innerPadding = PaddingValues())
}

@Preview(showBackground = true)
@Composable
fun DuaScreenPreview() {
    val fakeNavController = rememberNavController()
    DuaScreen(index = 0, navController = fakeNavController, innerPadding = PaddingValues())
}



