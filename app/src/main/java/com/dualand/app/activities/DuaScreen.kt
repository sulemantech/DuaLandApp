package com.dualand.app.activities

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
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
import com.dualand.app.components.DuaTabs
import com.dualand.app.components.PlayWordByWordButton
import com.dualand.app.models.Dua
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterialApi::class)
@Composable
fun DuaScreen(
    innerPadding: PaddingValues,
    index: Int,
    navController: NavController,
    stopAudioPlayback: () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    val duas = duaList
    val context = LocalContext.current
    var currentIndex by remember { mutableStateOf(index.coerceIn(0, duas.lastIndex)) }
    val NavigationBarColor = colorResource(id = R.color.top_nav_new)
    val statusBarColor = colorResource(id = duas[currentIndex].statusBarColorResId)
    val MyArabicFont = FontFamily(Font(R.font.al_quran))
    val translationtext = FontFamily(Font(R.font.poppins_regular))
    val reference = FontFamily(Font(R.font.poppins_semibold))
    val title = FontFamily(Font(R.font.mochypop_regular))
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
    var selectedTab by remember { mutableStateOf("") }

    val selectedLanguages = remember {
        mutableStateListOf<String>().apply {
            CoroutineScope(Dispatchers.IO).launch {
                val languages = LanguagePreferences.getSelectedLanguages(context)
                withContext(Dispatchers.Main) {
                    clear()
                    addAll(languages)
                }
            }
        }
    }

    val twoDuaIndices = setOf(0, 1, 2, 3, 6, 7, 15, 16, 19, 21, 22, 23, 28, 29, 31, 32)
    val threeDuaIndices = setOf(19, 20, 21, 36, 37, 38)

    val showCount = when {
        currentIndex in threeDuaIndices -> 3
        currentIndex in twoDuaIndices -> 2
        else -> 1
    }

    SideEffect {
        systemUiController.setStatusBarColor(color = statusBarColor)
        systemUiController.setNavigationBarColor(color = NavigationBarColor)
    }

    fun stopAudioPlayback(player: MediaPlayer?): MediaPlayer? {
        player?.let {
            try {
                if (it.isPlaying) {
                    it.stop()
                }
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            } finally {
                it.release()
            }
        }

        isPlaying = false
        showListening = false
        globalWordIndex = -1

        wordHandler?.removeCallbacks(wordRunnable ?: Runnable {})
        wordHandler = null
        wordRunnable = null

        return null
    }

    fun stopAudioPlayback() {
        globalMediaPlayer?.let { player ->
            try {
                if (player.isPlaying) {
                    player.stop()
                }
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            } finally {
                player.release()
            }
        }

        globalMediaPlayer = null
        isPlaying = false
        showListening = false
        globalWordIndex = -1

        wordHandler?.removeCallbacks(wordRunnable ?: Runnable {})
        wordHandler = null
        wordRunnable = null
    }


    DisposableEffect(Unit) {
        onDispose {
            stopAudioPlayback()
        }
    }
    LaunchedEffect(currentIndex) {
       // stopAudioPlayback()
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
                    modifier = Modifier.padding(end = 4.dp, top = 5.dp)
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

            DuaTabs(
                dua = duas[currentIndex],
                selectedTab = selectedTab,
                onTabSelected = { selected ->
                    selectedTab = selected
                },
                onStopCompleteDua = {
                    stopAudioPlayback()
                },
                onPlayWordByWordButton = {
                    //playWord(0)
                },
                onCompleteDuaClick = {
                    selectedTab = "COMPLETE"

                    val sharedPref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                    val isReadTitleEnabled = sharedPref.getBoolean("read_title_enabled", false)
                    val isAutoNextEnabled = sharedPref.getBoolean("auto_next_duas_enabled", false)

                    fun playNextAudio(audioResId: Int, onComplete: () -> Unit) {
                        stopAudioPlayback()
                        globalMediaPlayer = MediaPlayer.create(context, audioResId)
                        globalMediaPlayer?.setOnCompletionListener {
                            stopAudioPlayback() // Clean up when done
                            globalMediaPlayer?.release()
                            globalMediaPlayer = null
                            onComplete()
                        }
                        globalMediaPlayer?.start()
                        isPlaying = true
                        Log.d("DEBUG", "Playing audio: $audioResId")
                    }

                    fun getDuasForIndex(index: Int): List<Dua> {
                        return when {
                            index in threeDuaIndices -> duas.subList(index, minOf(index + 3, duas.size))
                            index in twoDuaIndices -> duas.subList(index, minOf(index + 2, duas.size))
                            else -> listOfNotNull(duas.getOrNull(index))
                        }
                    }

                    fun buildAudioQueue(index: Int): List<Int> {
                        val queue = mutableListOf<Int>()
                        val duasForIndex = getDuasForIndex(index)

                        if (isReadTitleEnabled) {
                            duasForIndex.firstOrNull()?.titleAudioResId?.let { queue.add(it) }
                        }

                        for (dua in duasForIndex) {
                            queue.add(dua.fullAudioResId)
                        }

                        Log.d("DEBUG", "Audio Queue for index $index: $queue")
                        return queue
                    }

                    fun playQueue(queue: List<Int>, index: Int, onFinished: () -> Unit) {
                        fun playAt(i: Int) {
                            if (i >= queue.size) {
                                onFinished()
                                return
                            }
                            playNextAudio(queue[i]) {
                                playAt(i + 1)
                            }
                        }
                        playAt(0)
                    }

                    fun stopAudioPlayback() {
                        if (globalMediaPlayer?.isPlaying == true) {
                            Log.d("DEBUG", "Stopping previous audio playback")
                            globalMediaPlayer?.stop()
                        }
                    }

                    fun playFromIndex(index: Int) {
                        if (index >= duas.size) {
                            Log.d("DEBUG", "Index out of bounds: $index")
                            isPlaying = false
                            return
                        }

                        currentIndex = index
                        isPlaying = true

                        Log.d("DEBUG", "Playing from index: $index")

                        val audioQueue = buildAudioQueue(index)
                        
                        stopAudioPlayback()

                        playQueue(audioQueue, index) {
                            Log.d("DEBUG", "After playQueue function for index: $index")

                            if (isAutoNextEnabled) {
                                val nextIndex = index + getDuasForIndex(index).size
                                if (nextIndex < duas.size) {
                                    Log.d("DEBUG", "nextIndex: $nextIndex, index: $index")
                                    playFromIndex(nextIndex)
                                } else {
                                    Log.d("DEBUG", "No more duas to play, stopping.")
                                    isPlaying = false
                                }
                            } else {
                                isPlaying = false
                            }
                        }
                    }

                    // When index changes, ensure that playFromIndex is called for the new index
                    Log.d("DEBUG", "Starting to play from current index: $currentIndex")
                    playFromIndex(currentIndex)
                }
            )

            Box(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(currentIndex) {
                            detectHorizontalDragGestures { _, dragAmount ->
                                if (dragAmount < 0) {

                                    val increment = when {
                                        currentIndex in threeDuaIndices -> 3
                                        currentIndex in twoDuaIndices -> 2
                                        else -> 1
                                    }
                                    if (currentIndex + increment <= duas.lastIndex) {
                                        currentIndex += increment
                                    }
                                } else if (dragAmount > 0) {

                                    val possiblePrevIndex =
                                        (0 until currentIndex).lastOrNull { index ->
                                            index in threeDuaIndices || index in twoDuaIndices || index !in threeDuaIndices && index !in twoDuaIndices
                                        }
                                    val decrement = when {
                                        possiblePrevIndex != null && possiblePrevIndex in threeDuaIndices -> 3
                                        possiblePrevIndex != null && possiblePrevIndex in twoDuaIndices -> 2
                                        else -> 1
                                    }
                                    val newIndex = currentIndex - decrement
                                    if (newIndex >= 0) {
                                        currentIndex = newIndex
                                    }
                                }
                            }
                        }
                ) {
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
                            for (i in currentIndex until (currentIndex + showCount).coerceAtMost(
                                duas.size
                            )) {
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
                                    Box(
                                        modifier = Modifier
                                    ) {
                                        IconButton(onClick = { }) {
                                            Image(
                                                painter = painterResource(id = R.drawable.favourite_icon),
                                                contentDescription = "Favourite",
                                                modifier = Modifier.size(33.dp)
                                            )
                                        }
                                    }

                                    fun playWord(
                                        index: Int,
                                        isAutoNextEnabled: Boolean = false,
                                        isReadTitleEnabled: Boolean = false
                                    ) {
                                        if (currentPlayingIndex >= duas.size) {
                                            isPlaying = false
                                            return
                                        }

                                        val dua = duas[currentPlayingIndex]
                                        Log.d("DuaPlayback", "Playing Dua index: $currentPlayingIndex, word index: $index")

                                        if (index == 0 && isReadTitleEnabled) {
                                            dua.titleAudioResId?.let { titleAudioId ->
                                                globalMediaPlayer?.release()
                                                globalMediaPlayer = MediaPlayer.create(context, titleAudioId)

                                                globalMediaPlayer?.setOnCompletionListener {
                                                    playWord(0, isAutoNextEnabled, false)
                                                }

                                                globalMediaPlayer?.start()
                                                return
                                            }
                                        }

                                        if (index >= dua.wordAudioPairs.size) {
                                            if (isRepeatMode && (repeatCount == Int.MAX_VALUE || currentRepeat < repeatCount)) {
                                                currentRepeat++
                                                isRepeatingNow = true
                                                currentlyRepeatingDuaIndex = currentPlayingIndex
                                                playWord(0, isAutoNextEnabled, false)
                                            } else {
                                                isRepeatMode = false
                                                currentRepeat = 0
                                                isRepeatingNow = false
                                                currentlyRepeatingDuaIndex = -1

                                                if (isAutoNextEnabled && currentPlayingIndex + 1 < duas.size) {
                                                    currentPlayingIndex++
                                                    globalWordIndex = -1
                                                    isPlaying = true
                                                    showListening = true

                                                    val nextDua = duas[currentPlayingIndex]
                                                    Log.d("DuaPlayback", "Moving to next dua: index $currentPlayingIndex")

                                                    if (isReadTitleEnabled && nextDua.titleAudioResId != null) {
                                                        globalMediaPlayer?.release()
                                                        globalMediaPlayer = MediaPlayer.create(context, nextDua.titleAudioResId)

                                                        globalMediaPlayer?.setOnCompletionListener {
                                                            playWord(0, isAutoNextEnabled, false)
                                                        }

                                                        globalMediaPlayer?.start()
                                                    } else {
                                                        playWord(0, isAutoNextEnabled, isReadTitleEnabled)
                                                    }
                                                } else {
                                                    isPlaying = false
                                                    showListening = false
                                                    globalWordIndex = -1
                                                }
                                            }
                                            return
                                        }
                                        val (_, audioResId) = dua.wordAudioPairs[index]

                                        wordHandler?.removeCallbacks(wordRunnable ?: Runnable {})
                                        wordHandler = null
                                        wordRunnable = null

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
                                                wordHandler = Handler(Looper.getMainLooper())
                                                wordRunnable = Runnable {
                                                    playWord(index + 1, isAutoNextEnabled, false)
                                                }
                                                wordHandler?.postDelayed(wordRunnable!!, effectiveDuration.toLong())
                                            }

                                            start()
                                        }
                                    }


                                    val sharedPref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                                    val isReadTitleEnabled = sharedPref.getBoolean("read_title_enabled", false)
                                    val isAutoNextEnabled = sharedPref.getBoolean("auto_next_duas_enabled", false)

                                    PlayWordByWordButton(
                                        isPlaying = isPlaying && currentPlayingIndex == i,
                                        showListening = showListening && currentPlayingIndex == i,
                                        onClick = {
                                            if (isPlaying && currentPlayingIndex == i) {
                                                globalMediaPlayer?.pause()
                                                isPlaying = false
                                                showListening = false
                                            } else {
                                                stopAudioPlayback()
                                                globalMediaPlayer?.release()

                                                currentPlayingIndex = i
                                                globalWordIndex = -1
                                                isPlaying = true
                                                showListening = true

                                                val selectedDua = duas[currentPlayingIndex]

                                                if (isReadTitleEnabled && selectedDua.titleAudioResId != null) {
                                                    globalMediaPlayer = MediaPlayer.create(context, selectedDua.titleAudioResId)
                                                    globalMediaPlayer?.setOnCompletionListener {
                                                        playWord(0, isAutoNextEnabled, false)
                                                    }
                                                    globalMediaPlayer?.start()
                                                } else {
                                                    playWord(0, isAutoNextEnabled, isReadTitleEnabled)
                                                }
                                            }
                                        }
                                    )

                                    Box(
                                        modifier = Modifier.padding(top = 2.dp)
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
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                            .padding(start = 20.dp, end = 20.dp)
                                    )
                                }

                                val sharedPref =
                                    context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                                val savedFontSize = sharedPref.getFloat("font_size", 24f)

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
                                            fontSize = savedFontSize.sp,
                                            fontFamily = MyArabicFont,
                                            textDirection = TextDirection.Rtl,
                                            textAlign = TextAlign.Center
                                        ),
                                        onClick = { offset ->
                                            annotatedText.getStringAnnotations(
                                                "WORD",
                                                offset,
                                                offset
                                            )
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
                                Spacer(modifier = Modifier.height(5.dp))
                                Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp)) {
                                    if (selectedLanguages.isEmpty()) {
                                        Box(modifier = Modifier.fillMaxWidth()) {
                                            Text(
                                                text = "No translation language selected.",
                                                fontFamily = translationtext,
                                                color = Color.Gray,
                                                textAlign = TextAlign.Center,
                                                fontSize = 14.sp,
                                                modifier = Modifier
                                                    .align(Alignment.Center)
                                            )
                                        }
                                    } else {
                                        selectedLanguages.forEach { lang ->
                                            when {
                                                lang.equals("English", ignoreCase = true) -> {
                                                    dua.translation?.let {
                                                        Box(modifier = Modifier.fillMaxWidth()) {
                                                            Text(
                                                                text = it,
                                                                fontFamily = translationtext,
                                                                fontSize = 13.sp,
                                                                textAlign = TextAlign.Center,
                                                                modifier = Modifier
                                                                    .align(Alignment.Center)

                                                            )

                                                        }
                                                    }
                                                    Spacer(modifier = Modifier.height(10.dp))
                                                }

                                                lang.equals("Urdu", ignoreCase = true) -> {
                                                    dua.urdu?.let {
                                                        Text(
                                                            text = it,
                                                            fontFamily = translationtext,
                                                            fontSize = 13.sp,
                                                            textAlign = TextAlign.Center,
                                                        )
                                                    }
                                                    Spacer(modifier = Modifier.height(15.dp))

                                                }

                                                lang.equals("Hindi", ignoreCase = true) -> {
                                                    dua.hinditranslation?.let {
                                                        Text(
                                                            text = it,
                                                            fontFamily = translationtext,
                                                            fontSize = 13.sp,
                                                            textAlign = TextAlign.Center,
                                                        )
                                                    }
                                                    Spacer(modifier = Modifier.height(15.dp))
                                                }
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(5.dp))

                                }
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = dua.reference,
                                        fontSize = 10.sp,
                                        fontFamily = reference,
                                        color = colorResource(R.color.reference_color),
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                            .padding(bottom = 20.dp)
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
//                                selectedTab = ""
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
}

private var wordHandler: Handler? = null
private var wordRunnable: Runnable? = null


@Preview(showBackground = true)
@Composable
fun DuaScreenPreview() {
    val fakeNavController = rememberNavController()
    DuaScreen(
        index = 0,
        navController = fakeNavController,
        innerPadding = PaddingValues(),
        stopAudioPlayback = {}
    )
}



