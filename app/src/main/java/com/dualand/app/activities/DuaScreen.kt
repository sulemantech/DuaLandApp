package com.dualand.app.activities

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dualand.app.DuaViewModel
import com.dualand.app.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
    var mediaPlayer: MediaPlayer? by remember { mutableStateOf(null) }
    var currentIndex by remember { mutableStateOf(index.coerceIn(0, duas.lastIndex)) }

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
    var isReadTitleEnabled by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf("") }


//    val lifecycleOwner = LocalLifecycleOwner.current
//
//    DisposableEffect(lifecycleOwner) {
//        val observer = LifecycleEventObserver { _, event ->
//            if (event == Lifecycle.Event.ON_PAUSE) {
//                MediaPlayerManager.stopAudio()  // stop audio when app goes to background
//            }
//        }
//
//        lifecycleOwner.lifecycle.addObserver(observer)
//
//        onDispose {
//            lifecycleOwner.lifecycle.removeObserver(observer)
//        }
//    }

    fun stopAudioPlayback() {
        globalMediaPlayer?.apply {
            try {
                stop()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            } finally {
                release()
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
                            globalMediaPlayer?.release()
                            globalMediaPlayer = null
                            onComplete()
                        }
                        globalMediaPlayer?.start()
                        isPlaying = true
                    }

                    fun getDuasForIndex(index: Int): List<Dua> {
                        return when {
                            index in threeDuaIndices -> duas.subList(
                                index,
                                minOf(index + 3, duas.size)
                            )

                            index in twoDuaIndices -> duas.subList(
                                index,
                                minOf(index + 2, duas.size)
                            )

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

                    fun playFromIndex(index: Int) {
                        if (index >= duas.size) {
                            isPlaying = false
                            return
                        }

                        currentIndex = index
                        isPlaying = true

                        val audioQueue = buildAudioQueue(index)

                        playQueue(audioQueue, index) {
                            Log.d("DEBUG", "After playQueue function index: $index")

                            if (isAutoNextEnabled) {
                                val nextIndex = index + getDuasForIndex(index).size
                                if (nextIndex < duas.size) {
                                    Log.d("DEBUG", "nextIndex: $nextIndex, index: $index")

                                    playFromIndex(nextIndex)
                                } else {
                                    isPlaying = false
                                }
                            } else {
                                isPlaying = false
                            }
                        }
                    }
                    playFromIndex(currentIndex)
                }

            )

            val scope = rememberCoroutineScope()
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
    val context = LocalContext.current
    val systemUiController = rememberSystemUiController()
    var wordByWordPause by remember { mutableStateOf(2) }

    val MyArabicFont = FontFamily(Font(R.font.lateef_regular))
    val text_font = FontFamily(Font(R.font.montserrat_regular))
    val text_font1 = FontFamily(Font(R.font.doodlestrickers))
    val settings = FontFamily(Font(R.font.mochypop_regular))

    val sharedPref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val savedVoice = sharedPref.getString("selected_voice", "Female")
    val selectedVoice = remember { mutableStateOf(savedVoice ?: "Female") }

    val NavigationBarColor = colorResource(id = R.color.background_screen)
    val statusBarColor = colorResource(id = R.color.top_nav_new)

    val fontSize = remember { mutableStateOf(sharedPref.getFloat("font_size", 24f)) }

    val scrollState = rememberScrollState()
    val toggleOptions =
        listOf("Reading Out Dua Title", "Rewards", "Auto Next Dua's", "Word-by-Word Pause")

    val savedLanguages =
        remember { mutableStateOf(LanguagePreferences.getSelectedLanguages(context)) }
    val selectedLanguages = remember { mutableStateListOf(*savedLanguages.value.toTypedArray()) }

    var readTitleEnabled by remember {
        mutableStateOf(
            context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                .getBoolean("read_title_enabled", false)
        )
    }

    var rewardsEnabled by remember {
        mutableStateOf(
            context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                .getBoolean("rewards_enabled", false)
        )
    }

    var autoNextDuasEnabled by remember {
        mutableStateOf(
            context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                .getBoolean("auto_next_duas_enabled", false)
        )
    }
    var WordbyWordPauseEnabled by remember {
        mutableStateOf(
            context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                .getBoolean("Word_by_Word_Pause_Enabled", false)
        )
    }

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
                            IconButton(onClick = { if (fontSize.value > 10) fontSize.value -= 2f }) {
                                Image(
                                    painter = painterResource(id = R.drawable.minus_icon),
                                    contentDescription = "Minus"
                                )
                            }
                            Text(
                                text = "${fontSize.value.toInt()}",
                                fontSize = 20.sp,
                                fontFamily = text_font,
                                color = colorResource(R.color.heading_color),
                                fontWeight = FontWeight.W700
                            )
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
                    var pauseSeconds by remember { mutableStateOf(2) }

                    toggleOptions.forEach { title ->
                        var isSwitchOn by remember { mutableStateOf(false) }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(
                                    title,
                                    fontFamily = text_font,
                                    fontWeight = FontWeight.W600,
                                    color = colorResource(R.color.heading_color),
                                    fontSize = 15.sp
                                )

                                if (title != "Word-by-Word Pause") {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        when (title) {
                                            "Reading Out Dua Title" -> "Reads out the dua title automatically"
                                            "Rewards" -> "Gives a reward when a dua is completed"
                                            "Auto Next Dua's" -> "Automatically move to the next dua"
                                            "Word-by-Word Pause" -> "Automatically move to the next dua"
                                            else -> ""
                                        },
                                        fontSize = 10.sp,
                                        color = colorResource(R.color.heading_color)
                                    )
                                } else {

                                    Spacer(modifier = Modifier.height(18.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        IconButton(
                                            onClick = { if (pauseSeconds > 1) pauseSeconds-- },
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
                                            onClick = { pauseSeconds++ },
                                            modifier = Modifier.size(44.dp)
                                        ) {
                                            Image(
                                                painter = painterResource(id = R.drawable.plus_icon),
                                                contentDescription = "Plus"
                                            )
                                        }
                                    }
                                    Divider(Modifier.padding(vertical = 8.dp))

                                }
                            }

                            if (title != "Word-by-Word Pause") {
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
                                            "Reading Out Dua Title" -> readTitleEnabled = isChecked
                                            "Rewards" -> rewardsEnabled = isChecked
                                            "Auto Next Dua's" -> autoNextDuasEnabled = isChecked
                                            "Word-by-Word Pause" -> WordbyWordPauseEnabled =isChecked
                                        }
                                    },
                                    modifier = Modifier.scale(1.0f),
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = colorResource(R.color.check_box),
                                        uncheckedThumbColor = colorResource(R.color.uncheckedThumbColor),
                                        uncheckedTrackColor = colorResource(R.color.white)
                                    )
                                )
                            }
                        }
                        if (title != "Word-by-Word Pause") {
                            Divider(Modifier.padding(vertical = 8.dp))
                        }
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
                                        if (selectedVoice.value == gender) colorResource(R.color.check_box) else Color.Transparent,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .padding(
                                        start = 25.dp,
                                        end = 25.dp,
                                        top = 15.dp,
                                        bottom = 10.dp
                                    )
                                    .clickable { selectedVoice.value = gender }
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
                                if (selectedVoice.value == gender) {
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
                                .putFloat("font_size", fontSize.value)
                                .putStringSet("selected_languages", selectedLanguages.toSet())
                                .putString("selected_voice", selectedVoice.value)
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

@Composable
fun DuaTabs(
    dua: Dua,
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    onStopCompleteDua: () -> Unit = {},
    onStartWordByWord: () -> Unit = {},
    onPlayWordByWordButton: () -> Unit,
    onCompleteDuaClick: (Int) -> Unit = {}
) {
    val currentTab = if (selectedTab.isEmpty()) "WORD" else selectedTab
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
                    .clickable {
                        onStopCompleteDua()
                        onTabSelected("WORD")
                        onPlayWordByWordButton()
                    }
            ) {
                if (currentTab == "WORD") {
                    Image(
                        painter = painterResource(id = R.drawable.selected_tab1),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.matchParentSize()
                    )
                }
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "WORD BY WORD",
                        color = if (currentTab == "WORD") Color.Black else Color.White,
                        fontFamily = MyArabicFont,
                        fontSize = 18.sp
                    )
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable {
                        onTabSelected("COMPLETE")
                        onStopCompleteDua()
                        onCompleteDuaClick(dua.fullAudioResId)
                    }
            ) {
                if (currentTab == "COMPLETE") {
                    Image(
                        painter = painterResource(id = R.drawable.selected_tab2),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.matchParentSize()
                    )
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "COMPLETE DUA",
                        color = if (currentTab == "COMPLETE") Color.Black else Color.White,
                        fontFamily = MyArabicFont,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDuaStatusScreen(navController: NavController, innerPadding: PaddingValues,  viewModel: DuaViewModel) {

    val systemUiController = rememberSystemUiController()
    val NavigationBarColor = colorResource(id = R.color.top_nav_new)
    val statusBarColor = colorResource(id = R.color.top_nav_new)
    var searchText by remember { mutableStateOf("") }
    val context = LocalContext.current
    val title = FontFamily(Font(R.font.mochypop_regular))

//    val viewModel: DuaViewModel = viewModel()
//
    SideEffect {
        systemUiController.setStatusBarColor(color = statusBarColor)
        systemUiController.setNavigationBarColor(color = NavigationBarColor)
    }

    val MyArabicFont = FontFamily(Font(R.font.doodlestrickers))

    // Observe the list of favorite duas
    val favoriteDuas by viewModel.favoriteDuas.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(colorResource(R.color.white))
    ) {
        Image(
            painter = painterResource(id = R.drawable.top_bd),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
        )

        Column(modifier = Modifier.fillMaxSize()) {
            // Header with Back and Settings button
            Box(
                modifier = Modifier,
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.padding(start = 6.dp, top = 12.dp)
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
                            text = "My Dua Status",
                            fontSize = 14.sp,
                            color = colorResource(R.color.heading_color),
                            fontFamily = title,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }

                    IconButton(
                        onClick = { navController.navigate("SettingsScreen") },
                        modifier = Modifier.padding(end = 6.dp, top = 12.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.setting_btn),
                            contentDescription = "Settings",
                            modifier = Modifier.size(29.dp, 30.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Search bar
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(start = 16.dp, end = 16.dp),
                trailingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "Voice Search",
                        modifier = Modifier
                            .size(42.dp)
                            .clickable {
                                // Handle search action
                            }
                    )
                },
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    containerColor = colorResource(R.color.top_nav_new)
                )
            )

            // Display list of favorite duas
            LazyColumn {
                items(favoriteDuas) { dua ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = dua.title,
                            fontSize = 16.sp,
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(
                            onClick = { viewModel.toggleFavorite(dua) }
                        ) {
                            val icon = if (dua.isFavorite) {
                                painterResource(id = R.drawable.favourite_active_icon)
                            } else {
                                painterResource(id = R.drawable.favourite_icon)
                            }
                            Image(
                                painter = icon,
                                contentDescription = "Favorite",
                                modifier = Modifier.size(33.dp)
                            )
                        }
                    }
                }
            }
        }

        // Bottom bar with share button
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
                modifier = Modifier
                    .matchParentSize()
                    .height(53.dp)
            )

            Box(
                modifier = Modifier,
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp, top = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { /* Handle previous action */ }) {
                        Image(
                            painter = painterResource(id = R.drawable.favourite_icon),
                            contentDescription = "Previous",
                            modifier = Modifier.size(33.dp, 40.dp)
                        )
                    }

                    IconButton(onClick = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "Check out this amazing app: https://play.google.com/store/apps/details?id=${context.packageName}"
                            )
                            type = "text/plain"
                        }

                        val shareIntent = Intent.createChooser(sendIntent, "Share App")
                        context.startActivity(shareIntent)
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.share_icon),
                            contentDescription = "Share",
                            modifier = Modifier.size(33.dp, 40.dp)
                        )
                    }

                    IconButton(onClick = { /* Handle next action */ }) {
                        Image(
                            painter = painterResource(id = R.drawable.info_icon),
                            contentDescription = "Next",
                            modifier = Modifier.size(33.dp, 40.dp)
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun MyduaStatusScreenPreview() {
    val viewModel = DuaViewModel(Application())
    MyDuaStatusScreen(
        navController = rememberNavController(),
        innerPadding = PaddingValues(),
        viewModel = viewModel
    )
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
    DuaScreen(
        index = 0,
        navController = fakeNavController,
        innerPadding = PaddingValues(),
        stopAudioPlayback = {}
    )
}



