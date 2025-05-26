package com.dualand.app.activities

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalConfiguration
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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dualand.app.DuaViewModel
import com.dualand.app.R
import com.dualand.app.activities.DuaDataProvider.duaList
import com.dualand.app.components.AllDuaFunctionality
import com.dualand.app.components.DuaTabs
import com.dualand.app.components.FavouriteButton
import com.dualand.app.components.PlayWordByWordButton
import com.dualand.app.components.responsiveFontSize
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
    stopAudioPlayback: () -> Unit,
    duaViewModel: DuaViewModel = viewModel()
) {
    val systemUiController = rememberSystemUiController()
    val duas = duaList
    val context = LocalContext.current
    var currentIndex by remember { mutableStateOf(index.coerceIn(0, duas.lastIndex)) }
    val NavigationBarColor = colorResource(id = R.color.top_nav_new)
    val statusBarColor = colorResource(id = duas[currentIndex].statusBarColorResId)
    val MyArabicFont = FontFamily(Font(R.font.vazirmatn_regular))
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
    // var selectedTab by remember { mutableStateOf("") }
    var selectedTab by rememberSaveable { mutableStateOf("WORD") }
    var currentAudioQueueIndex by remember { mutableStateOf(0) }
    var currentAudioPosition by remember { mutableStateOf(0) }
    var currentAudioQueue by remember { mutableStateOf<List<Int>>(emptyList()) }
    var wasPaused by remember { mutableStateOf(false) }


    @Composable
    fun isTablet(): Boolean {
        val configuration = LocalConfiguration.current
        val screenWidthDp = configuration.screenWidthDp
        return screenWidthDp >= 600
    }

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

    val twoDuaIndices = setOf(0, 1, 2, 3, 6, 7, 15, 16, 21, 22, 27, 28, 30, 31)
    val threeDuaIndices = setOf(18, 19, 20, 35, 36, 37)

    val showCount = when {
        currentIndex in threeDuaIndices -> 3
        currentIndex in twoDuaIndices -> 2
        else -> 1
    }

    SideEffect {
        systemUiController.setStatusBarColor(color = statusBarColor)
        systemUiController.setNavigationBarColor(color = NavigationBarColor)
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
                    onClick = { navController.navigate("learn") },
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

                    duas[currentIndex].textheading?.let {
                        Text(
                            text = it,
                            fontSize = 14.sp,
                            color = colorResource(R.color.heading_color),
                            fontFamily = title,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                    }
                }
                IconButton(onClick = {navController.navigate("InfoScreen")},
                    modifier = Modifier.padding(start = 4.dp, top = 5.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.info_icon),
                        contentDescription = "Info",
                        modifier = Modifier.size(29.dp, 30.dp)
                    )
                }
            }

            val isTablet = isTablet()
            val imageHeight = if (isTablet) 500.dp else 230.dp

            Image(
                painter = painterResource(id = duas[currentIndex].image),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
                    .padding(top = 5.dp)
            )

            Spacer(modifier = Modifier.height(9.dp))
//            DuaTabs(
//                dua = duas[currentIndex],
//                selectedTab = selectedTab,
//                onTabSelected = { selected ->
//                    selectedTab = selected
//                },
//                onStopCompleteDua = {
//                    stopAudioPlayback()
//                    isPlaying = false
//                    showListening = false
//                },
//                onPlayWordByWordButton = {
//                    //playWord(0)
//                },
//            )

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
                                        stopAudioPlayback()
                                        wasPaused = false
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
                                        stopAudioPlayback()
                                        wasPaused = false
                                        currentIndex = newIndex
                                    }
                                }
                            }
                        }
                ) {

                    val scrollState = rememberScrollState()
                    val duaPositions = remember { mutableMapOf<Int, Int>() }

                    LaunchedEffect(currentPlayingIndex) {
                        duaPositions[currentPlayingIndex]?.let { yOffset ->
                            scrollState.animateScrollTo(yOffset)
                        }
                    }

//                    LaunchedEffect(currentIndex) {
//                        scrollState.animateScrollTo(0)
//                    }

                   // AllDuaFunctionality(innerPadding = innerPadding, index = currentIndex,currentPlayingIndex)

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 50.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(innerPadding)
                                .verticalScroll(scrollState)
                        ) {

                            for (i in currentIndex until (currentIndex + showCount).coerceAtMost(
                                duas.size
                            )) {
                                val dua = duas[i]

                                Box(
                                    modifier = Modifier.onGloballyPositioned { layoutCoordinates ->
                                        duaPositions[i] =
                                            layoutCoordinates.positionInParent().y.toInt()
                                    }
                                ) {
                                    Column {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 12.dp),
                                            horizontalArrangement = Arrangement.spacedBy(
                                                -10.dp,
                                                Alignment.CenterHorizontally
                                            )
                                        ) {
                                            FavouriteButton(
                                                duaNumber = dua.duaNumber,
                                                textHeading = dua.textheading,
                                                imageResId = dua.image,
                                                viewModel = duaViewModel
                                            )

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
                                                Log.d(
                                                    "DuaPlayback",
                                                    "Playing Dua index: $currentPlayingIndex, word index: $index"
                                                )

                                                if (index == 0 && isReadTitleEnabled) {
                                                    dua.titleAudioResId?.let { titleAudioId ->
                                                        globalMediaPlayer?.release()
                                                        globalMediaPlayer = MediaPlayer.create(
                                                            context,
                                                            titleAudioId
                                                        )

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
                                                        currentlyRepeatingDuaIndex =
                                                            currentPlayingIndex
                                                        playWord(0, isAutoNextEnabled, false)
                                                    } else {
                                                        isRepeatMode = false
                                                        currentRepeat = 0
                                                        isRepeatingNow = false
                                                        currentlyRepeatingDuaIndex = -1

                                                        if (isAutoNextEnabled) {
                                                            val showCount = when {
                                                                currentIndex in threeDuaIndices -> 3
                                                                currentIndex in twoDuaIndices -> 2
                                                                else -> 1
                                                            }

                                                            val groupStartIndex = currentIndex
                                                            val groupEndIndexExclusive =
                                                                (groupStartIndex + showCount).coerceAtMost(
                                                                    duas.size
                                                                )

                                                            val nextDuaInGroup =
                                                                currentPlayingIndex + 1
                                                            if (nextDuaInGroup < groupEndIndexExclusive) {
                                                                currentPlayingIndex = nextDuaInGroup
                                                                globalWordIndex = -1
                                                                isPlaying = true
                                                                showListening = true

                                                                val nextDua =
                                                                    duas[currentPlayingIndex]
                                                                Log.d(
                                                                    "DuaPlayback",
                                                                    "Playing next Dua in same group: index $currentPlayingIndex"
                                                                )

                                                                if (isReadTitleEnabled && nextDua.titleAudioResId != null) {
                                                                    globalMediaPlayer?.release()
                                                                    globalMediaPlayer =
                                                                        MediaPlayer.create(
                                                                            context,
                                                                            nextDua.titleAudioResId
                                                                        )
                                                                    globalMediaPlayer?.setOnCompletionListener {
                                                                        playWord(
                                                                            0,
                                                                            isAutoNextEnabled,
                                                                            false
                                                                        )
                                                                    }
                                                                    globalMediaPlayer?.start()
                                                                } else {
                                                                    playWord(
                                                                        0,
                                                                        isAutoNextEnabled,
                                                                        isReadTitleEnabled
                                                                    )
                                                                }
                                                            } else {
                                                                val nextGroupStartIndex =
                                                                    groupEndIndexExclusive
                                                                if (nextGroupStartIndex < duas.size) {
                                                                    currentPlayingIndex =
                                                                        nextGroupStartIndex
                                                                    currentIndex =
                                                                        currentPlayingIndex
                                                                    globalWordIndex = -1
                                                                    isPlaying = true
                                                                    showListening = true

                                                                    val nextDua =
                                                                        duas[currentPlayingIndex]
                                                                    Log.d(
                                                                        "DuaPlayback",
                                                                        "Moving to next group: index $currentPlayingIndex"
                                                                    )

                                                                    if (isReadTitleEnabled && nextDua.titleAudioResId != null) {
                                                                        globalMediaPlayer?.release()
                                                                        globalMediaPlayer =
                                                                            MediaPlayer.create(
                                                                                context,
                                                                                nextDua.titleAudioResId
                                                                            )
                                                                        globalMediaPlayer?.setOnCompletionListener {
                                                                            playWord(
                                                                                0,
                                                                                isAutoNextEnabled,
                                                                                false
                                                                            )
                                                                        }
                                                                        globalMediaPlayer?.start()
                                                                    } else {
                                                                        playWord(
                                                                            0,
                                                                            isAutoNextEnabled,
                                                                            isReadTitleEnabled
                                                                        )
                                                                    }
                                                                } else {
                                                                    isPlaying = false
                                                                    showListening = false
                                                                    globalWordIndex = -1
                                                                }
                                                            }
                                                        } else {
                                                            // AutoNext disabled — stop here
                                                            isPlaying = false
                                                            showListening = false
                                                            globalWordIndex = -1
                                                        }
                                                    }
                                                    return
                                                }

                                                val (_, audioResId) = dua.wordAudioPairs[index]

                                                wordHandler?.removeCallbacks(
                                                    wordRunnable ?: Runnable {})
                                                wordHandler = null
                                                wordRunnable = null

                                                globalMediaPlayer?.release()
                                                globalMediaPlayer =
                                                    MediaPlayer.create(context, audioResId)

                                                globalWordIndex = index
                                                showListening = false

                                                globalMediaPlayer?.apply {
                                                    val rawDuration = duration.toLong()

                                                    val pauseMillis = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                                                        .getInt("word_by_word_pause_seconds", 1) * 1000L


                                                    setOnCompletionListener {
                                                        showListening = true
                                                        wordHandler =
                                                            Handler(Looper.getMainLooper())
                                                        wordRunnable = Runnable {
                                                            playWord(
                                                                index + 1,
                                                                isAutoNextEnabled,
                                                                false
                                                            )
                                                        }
                                                        wordHandler?.postDelayed(wordRunnable!!, pauseMillis)
                                                    }

                                                    start()
                                                }
                                            }

                                            PlayWordByWordButton(
                                                isPlaying = isPlaying && currentPlayingIndex == i,
                                                showListening = showListening && currentPlayingIndex == i,
                                                onClick = {
                                                    if (selectedTab == "WORD") {

                                                        if (isPlaying && currentPlayingIndex == i) {
                                                            globalMediaPlayer?.pause()
                                                            isPlaying = false
                                                            showListening = false
                                                            stopAudioPlayback()
                                                        } else {
                                                            stopAudioPlayback()
                                                            globalMediaPlayer?.release()

                                                            currentPlayingIndex = i
                                                            globalWordIndex = -1
                                                            isPlaying = true
                                                            showListening = true

                                                            val selectedDua = duas[currentPlayingIndex]

                                                            val sharedPref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                                                            val isReadTitleEnabled = sharedPref.getBoolean("read_title_enabled", false)
                                                            val isAutoNextEnabled = sharedPref.getBoolean("auto_next_duas_enabled", false)
                                                            val pauseDuration = sharedPref.getInt("word_by_word_pause_seconds", 2)

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
                                                    else if (selectedTab == "COMPLETE") {
                                                        selectedTab = "COMPLETE"
                                                        val sharedPref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                                                        val isReadTitleEnabled = sharedPref.getBoolean("read_title_enabled", false)
                                                        val isAutoNextEnabled = sharedPref.getBoolean("auto_next_duas_enabled", false)

                                                        fun stopAudioPlayback() {
                                                            if (globalMediaPlayer?.isPlaying == true) {
                                                                globalMediaPlayer?.stop()
                                                            }
                                                            globalMediaPlayer?.release()
                                                            globalMediaPlayer = null
                                                        }

                                                        fun playNextAudio(audioResId: Int, resumeFrom: Int = 0, onComplete: () -> Unit) {
                                                            stopAudioPlayback()
                                                            globalMediaPlayer = MediaPlayer.create(context, audioResId)
                                                            globalMediaPlayer?.seekTo(resumeFrom)
                                                            globalMediaPlayer?.setOnCompletionListener {
                                                                currentAudioPosition = 0
                                                                stopAudioPlayback()
                                                                onComplete()
                                                            }
                                                            globalMediaPlayer?.start()
                                                            isPlaying = true
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
                                                            return queue
                                                        }

                                                        fun playQueue(queue: List<Int>, index: Int, startFrom: Int, resumeFrom: Int = 0, onFinished: () -> Unit) {
                                                            val duasForIndex = getDuasForIndex(index)

                                                            fun playAt(i: Int, resumeFrom: Int) {
                                                                if (i >= queue.size) {
                                                                    onFinished()
                                                                    return
                                                                }

                                                                currentAudioQueueIndex = i
                                                                currentPlayingIndex = when {
                                                                    isReadTitleEnabled && i == 0 -> index
                                                                    else -> {
                                                                        val duaStart = if (isReadTitleEnabled) 1 else 0
                                                                        val duaIndex = index + (i - duaStart)
                                                                        duaIndex
                                                                    }
                                                                }

                                                                playNextAudio(queue[i], resumeFrom) {
                                                                    playAt(i + 1, 0)
                                                                }
                                                            }

                                                            playAt(startFrom, resumeFrom)
                                                        }

                                                        fun playFromIndex(index: Int) {
                                                            if (index >= duas.size) {
                                                                isPlaying = false
                                                                showListening = false
                                                                return
                                                            }

                                                            currentIndex = index
                                                            currentPlayingIndex = index
                                                            isPlaying = true
                                                            showListening = false

                                                            if (!wasPaused) {
                                                                currentAudioQueue = buildAudioQueue(index)
                                                                currentAudioQueueIndex = 0
                                                                currentAudioPosition = 0
                                                            }

                                                            playQueue(currentAudioQueue, index, currentAudioQueueIndex, currentAudioPosition) {
                                                                wasPaused = false
                                                                if (isAutoNextEnabled) {
                                                                    val nextIndex = index + getDuasForIndex(index).size
                                                                    if (nextIndex < duas.size) {
                                                                        currentAudioQueueIndex = 0
                                                                        currentAudioPosition = 0
                                                                        playFromIndex(nextIndex)
                                                                    } else {
                                                                        isPlaying = false
                                                                    }
                                                                } else {
                                                                    isPlaying = false
                                                                }
                                                            }
                                                        }

                                                        if (isPlaying) {
                                                            // Pause and save state
                                                            stopAudioPlayback()
                                                            currentAudioPosition = globalMediaPlayer?.currentPosition ?: 0
                                                            globalMediaPlayer?.pause()
                                                            isPlaying = false
                                                            showListening = false
                                                            wasPaused = true
                                                        } else {
                                                            if (wasPaused && currentIndex == i) {
                                                                // Resume same dua
                                                                playFromIndex(currentIndex)
                                                            } else {
                                                                // New dua or first-time play
                                                                wasPaused = false
                                                                currentAudioQueue = buildAudioQueue(i)     // REBUILD for new index
                                                                currentAudioQueueIndex = 0
                                                                currentAudioPosition = 0
                                                                playFromIndex(i)
                                                            }
                                                        }

                                                    }

                                                }
                                            )
                                            Box(
                                                modifier = Modifier.padding(top = 2.dp)
                                            ) {
                                                IconButton(
                                                    onClick = {
                                                        when {
                                                            repeatCount < 5 -> repeatCount++
                                                            repeatCount == 5 -> repeatCount = Int.MAX_VALUE
                                                            repeatCount == Int.MAX_VALUE -> {
                                                                repeatCount = 0
                                                                isRepeatMode = false
                                                            }
                                                        }
                                                        if (repeatCount > 0) {
                                                            isRepeatMode = true
                                                            currentRepeat = 0
                                                        }
                                                    },
                                                    modifier = Modifier.align(Alignment.TopEnd)
                                                ) {
                                                    val iconRes = if (repeatCount > 0) {
                                                        R.drawable.repeat_1_time_btn
                                                    } else {
                                                        R.drawable.repeat_off_btn
                                                    }

                                                    Image(
                                                        painter = painterResource(id = iconRes),
                                                        contentDescription = "Repeat",
                                                        modifier = Modifier.size(33.dp)
                                                    )
                                                }

                                                if (repeatCount > 0 && currentPlayingIndex == i) {
                                                    val badgeText = if (repeatCount == Int.MAX_VALUE) "∞" else repeatCount.toString()

                                                    Box(
                                                        contentAlignment = Alignment.Center,
                                                        modifier = Modifier
                                                            .size(18.dp)
                                                            .background(
                                                                colorResource(R.color.badge_color),
                                                                shape = CircleShape
                                                            )
                                                            .align(Alignment.TopEnd)
                                                            .offset(x = (-2).dp, y = 2.dp)
                                                    ) {
                                                        Text(
                                                            text = badgeText,
                                                            color = colorResource(R.color.white),
                                                            fontSize = 10.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            lineHeight = 10.sp
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

                                        Spacer(modifier = Modifier.height(5.dp))

                                        val sharedPref =
                                            context.getSharedPreferences(
                                                "app_prefs",
                                                Context.MODE_PRIVATE
                                            )
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
                                                            val clickedIndex =
                                                                annotation.item.toInt()
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
                                        Column(
                                            modifier = Modifier.padding(
                                                start = 20.dp,
                                                end = 20.dp
                                            )
                                        ) {
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
                                                        lang.equals(
                                                            "English",
                                                            ignoreCase = true
                                                        ) -> {
                                                            dua.translation?.let {
                                                                Box(modifier = Modifier.fillMaxWidth()) {
                                                                    Text(
                                                                        text = it,
                                                                        fontFamily = translationtext,
                                                                        fontSize = responsiveFontSize(),
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
                                                                    fontSize = responsiveFontSize(),
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
                                                                    fontSize = responsiveFontSize(),
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
                                wasPaused = false
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
                                navController.navigate("favorites?filterType=All")
                            }) {
                                Image(
                                    painter = painterResource(id = R.drawable.favourite_icon_dua),
                                    contentDescription = "Favorites Dua",
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

                            IconButton(onClick = {
                                stopAudioPlayback()
                                wasPaused = false

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



