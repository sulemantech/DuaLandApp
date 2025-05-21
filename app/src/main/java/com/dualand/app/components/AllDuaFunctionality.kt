//package com.dualand.app.components
//
//import android.content.Context
//import android.media.MediaPlayer
//import android.os.Handler
//import android.os.Looper
//import android.util.Log
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.offset
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.text.ClickableText
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.CompositionLocalProvider
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateListOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.onGloballyPositioned
//import androidx.compose.ui.layout.positionInParent
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalLayoutDirection
//import androidx.compose.ui.res.colorResource
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.SpanStyle
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.buildAnnotatedString
//import androidx.compose.ui.text.font.Font
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextDirection
//import androidx.compose.ui.text.withStyle
//import androidx.compose.ui.unit.LayoutDirection
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.dualand.app.DuaViewModel
//import com.dualand.app.R
//import com.dualand.app.activities.DuaDataProvider.duaList
//import com.dualand.app.activities.LanguagePreferences
//import com.dualand.app.models.Dua
//import com.google.accompanist.systemuicontroller.rememberSystemUiController
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
//@Composable
//fun AllDuaFunctionality(
//    innerPadding: PaddingValues,
//    index: Int,
//    currentPlayingIndex: Int
//){
//    val viewModel: DuaViewModel = viewModel()
//
//    val scrollState = rememberScrollState()
//    val systemUiController = rememberSystemUiController()
//    val duas = duaList
//    val context = LocalContext.current
//    var currentIndex by remember { mutableStateOf(index.coerceIn(0, duas.lastIndex)) }
//    val NavigationBarColor = colorResource(id = R.color.top_nav_new)
//    val statusBarColor = colorResource(id = duas[currentIndex].statusBarColorResId)
//    val MyArabicFont = FontFamily(Font(R.font.vazirmatn_regular))
//    val translationtext = FontFamily(Font(R.font.poppins_regular))
//    val reference = FontFamily(Font(R.font.poppins_semibold))
//    val title = FontFamily(Font(R.font.mochypop_regular))
//    var currentPlayingIndex by remember { mutableStateOf(-1) }
//    var globalWordIndex by remember { mutableStateOf(-1) }
//    var isPlaying by remember { mutableStateOf(false) }
//    var showListening by remember { mutableStateOf(false) }
//    var globalMediaPlayer: MediaPlayer? by remember { mutableStateOf(null) }
//    var repeatCount by remember { mutableStateOf(0) }
//    var currentRepeat by remember { mutableStateOf(0) }
//    var isRepeatingNow by remember { mutableStateOf(false) }
//    var isRepeatMode by remember { mutableStateOf(false) }
//    var currentlyRepeatingDuaIndex by remember { mutableStateOf(-1) }
//    // var selectedTab by remember { mutableStateOf("") }
//    var selectedTab by rememberSaveable { mutableStateOf("WORD") }
//    var currentAudioQueueIndex by remember { mutableStateOf(0) }
//    var currentAudioPosition by remember { mutableStateOf(0) }
//    var currentAudioQueue by remember { mutableStateOf<List<Int>>(emptyList()) }
//    var wasPaused by remember { mutableStateOf(false) }
//    val duaPositions = remember { mutableMapOf<Int, Int>() }
//
//    fun stopAudioPlayback() {
//        globalMediaPlayer?.let { player ->
//            try {
//                if (player.isPlaying) {
//                    player.stop()
//                }
//            } catch (e: IllegalStateException) {
//                e.printStackTrace()
//            } finally {
//                player.release()
//            }
//        }
//
//        globalMediaPlayer = null
//        isPlaying = false
//        showListening = false
//        globalWordIndex = -1
//
//        wordHandler?.removeCallbacks(wordRunnable ?: Runnable {})
//        wordHandler = null
//        wordRunnable = null
//    }
//
//    val selectedLanguages = remember {
//        mutableStateListOf<String>().apply {
//            CoroutineScope(Dispatchers.IO).launch {
//                val languages = LanguagePreferences.getSelectedLanguages(context)
//                withContext(Dispatchers.Main) {
//                    clear()
//                    addAll(languages)
//                }
//            }
//        }
//    }
//
//    val twoDuaIndices = setOf(0, 1, 2, 3, 6, 7, 15, 16, 21, 22, 27, 28, 30, 31)
//    val threeDuaIndices = setOf(18, 19, 20, 35, 36, 37)
//
//    val showCount = when {
//        currentIndex in threeDuaIndices -> 3
//        currentIndex in twoDuaIndices -> 2
//        else -> 1
//    }
//
//    Column(
//    modifier = Modifier
//    .fillMaxSize()
//    .padding(bottom = 50.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .weight(1f)
//                .padding(innerPadding)
//                .verticalScroll(scrollState)
//        ) {
//
//            for (i in currentIndex until (currentIndex + showCount).coerceAtMost(
//                duas.size
//            )) {
//                val dua = duas[i]
//
//                Box(
//                    modifier = Modifier.onGloballyPositioned { layoutCoordinates ->
//                        duaPositions[i] =
//                            layoutCoordinates.positionInParent().y.toInt()
//                    }
//                ) {
//                    Column {
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(top = 12.dp),
//                            horizontalArrangement = Arrangement.spacedBy(
//                                -10.dp,
//                                Alignment.CenterHorizontally
//                            )
//                        ) {
//                            FavouriteButton(
//                                duaNumber = dua.duaNumber,
//                                textHeading = dua.textheading,
//                                imageResId = dua.image,
//                                viewModel = viewModel
//                            )
//
//                            fun playWord(
//                                index: Int,
//                                isAutoNextEnabled: Boolean = false,
//                                isReadTitleEnabled: Boolean = false
//                            ) {
//                                if (currentPlayingIndex >= duas.size) {
//                                    isPlaying = false
//                                    return
//                                }
//                                val dua = duas[currentPlayingIndex]
//                                Log.d(
//                                    "DuaPlayback",
//                                    "Playing Dua index: $currentPlayingIndex, word index: $index"
//                                )
//
//                                if (index == 0 && isReadTitleEnabled) {
//                                    dua.titleAudioResId?.let { titleAudioId ->
//                                        globalMediaPlayer?.release()
//                                        globalMediaPlayer = MediaPlayer.create(
//                                            context,
//                                            titleAudioId
//                                        )
//
//                                        globalMediaPlayer?.setOnCompletionListener {
//                                            playWord(0, isAutoNextEnabled, false)
//                                        }
//
//                                        globalMediaPlayer?.start()
//                                        return
//                                    }
//                                }
//
//                                if (index >= dua.wordAudioPairs.size) {
//                                    if (isRepeatMode && (repeatCount == Int.MAX_VALUE || currentRepeat < repeatCount)) {
//                                        currentRepeat++
//                                        isRepeatingNow = true
//                                        currentlyRepeatingDuaIndex =
//                                            currentPlayingIndex
//                                        playWord(0, isAutoNextEnabled, false)
//                                    } else {
//                                        isRepeatMode = false
//                                        currentRepeat = 0
//                                        isRepeatingNow = false
//                                        currentlyRepeatingDuaIndex = -1
//
//                                        if (isAutoNextEnabled) {
//                                            val showCount = when {
//                                                currentIndex in threeDuaIndices -> 3
//                                                currentIndex in twoDuaIndices -> 2
//                                                else -> 1
//                                            }
//
//                                            val groupStartIndex = currentIndex
//                                            val groupEndIndexExclusive =
//                                                (groupStartIndex + showCount).coerceAtMost(
//                                                    duas.size
//                                                )
//
//                                            val nextDuaInGroup =
//                                                currentPlayingIndex + 1
//                                            if (nextDuaInGroup < groupEndIndexExclusive) {
//                                                currentPlayingIndex = nextDuaInGroup
//                                                globalWordIndex = -1
//                                                isPlaying = true
//                                                showListening = true
//
//                                                val nextDua =
//                                                    duas[currentPlayingIndex]
//                                                Log.d(
//                                                    "DuaPlayback",
//                                                    "Playing next Dua in same group: index $currentPlayingIndex"
//                                                )
//
//                                                if (isReadTitleEnabled && nextDua.titleAudioResId != null) {
//                                                    globalMediaPlayer?.release()
//                                                    globalMediaPlayer =
//                                                        MediaPlayer.create(
//                                                            context,
//                                                            nextDua.titleAudioResId
//                                                        )
//                                                    globalMediaPlayer?.setOnCompletionListener {
//                                                        playWord(
//                                                            0,
//                                                            isAutoNextEnabled,
//                                                            false
//                                                        )
//                                                    }
//                                                    globalMediaPlayer?.start()
//                                                } else {
//                                                    playWord(
//                                                        0,
//                                                        isAutoNextEnabled,
//                                                        isReadTitleEnabled
//                                                    )
//                                                }
//                                            } else {
//                                                val nextGroupStartIndex =
//                                                    groupEndIndexExclusive
//                                                if (nextGroupStartIndex < duas.size) {
//                                                    currentPlayingIndex =
//                                                        nextGroupStartIndex
//                                                    currentIndex =
//                                                        currentPlayingIndex
//                                                    globalWordIndex = -1
//                                                    isPlaying = true
//                                                    showListening = true
//
//                                                    val nextDua =
//                                                        duas[currentPlayingIndex]
//                                                    Log.d(
//                                                        "DuaPlayback",
//                                                        "Moving to next group: index $currentPlayingIndex"
//                                                    )
//
//                                                    if (isReadTitleEnabled && nextDua.titleAudioResId != null) {
//                                                        globalMediaPlayer?.release()
//                                                        globalMediaPlayer =
//                                                            MediaPlayer.create(
//                                                                context,
//                                                                nextDua.titleAudioResId
//                                                            )
//                                                        globalMediaPlayer?.setOnCompletionListener {
//                                                            playWord(
//                                                                0,
//                                                                isAutoNextEnabled,
//                                                                false
//                                                            )
//                                                        }
//                                                        globalMediaPlayer?.start()
//                                                    } else {
//                                                        playWord(
//                                                            0,
//                                                            isAutoNextEnabled,
//                                                            isReadTitleEnabled
//                                                        )
//                                                    }
//                                                } else {
//                                                    isPlaying = false
//                                                    showListening = false
//                                                    globalWordIndex = -1
//                                                }
//                                            }
//                                        } else {
//                                            // AutoNext disabled — stop here
//                                            isPlaying = false
//                                            showListening = false
//                                            globalWordIndex = -1
//                                        }
//                                    }
//                                    return
//                                }
//
//                                val (_, audioResId) = dua.wordAudioPairs[index]
//
//                                wordHandler?.removeCallbacks(
//                                    wordRunnable ?: Runnable {})
//                                wordHandler = null
//                                wordRunnable = null
//
//                                globalMediaPlayer?.release()
//                                globalMediaPlayer =
//                                    MediaPlayer.create(context, audioResId)
//
//                                globalWordIndex = index
//                                showListening = false
//
//                                globalMediaPlayer?.apply {
//                                    val rawDuration = duration.toLong()
//
//                                    val pauseMillis = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
//                                        .getInt("word_by_word_pause_seconds", 1) * 1000L
//
//
//                                    setOnCompletionListener {
//                                        showListening = true
//                                        wordHandler =
//                                            Handler(Looper.getMainLooper())
//                                        wordRunnable = Runnable {
//                                            playWord(
//                                                index + 1,
//                                                isAutoNextEnabled,
//                                                false
//                                            )
//                                        }
//                                        wordHandler?.postDelayed(wordRunnable!!, pauseMillis)
//                                    }
//
//                                    start()
//                                }
//                            }
//
//                            PlayWordByWordButton(
//                                isPlaying = isPlaying && currentPlayingIndex == i,
//                                showListening = showListening && currentPlayingIndex == i,
//                                onClick = {
//                                    if (selectedTab == "WORD") {
//
//                                        if (isPlaying && currentPlayingIndex == i) {
//                                            globalMediaPlayer?.pause()
//                                            isPlaying = false
//                                            showListening = false
//                                            stopAudioPlayback()
//                                        } else {
//                                            stopAudioPlayback()
//                                            globalMediaPlayer?.release()
//
//                                            currentPlayingIndex = i
//                                            globalWordIndex = -1
//                                            isPlaying = true
//                                            showListening = true
//
//                                            val selectedDua = duas[currentPlayingIndex]
//
//                                            val sharedPref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
//                                            val isReadTitleEnabled = sharedPref.getBoolean("read_title_enabled", false)
//                                            val isAutoNextEnabled = sharedPref.getBoolean("auto_next_duas_enabled", false)
//                                            val pauseDuration = sharedPref.getInt("word_by_word_pause_seconds", 2)
//
//                                            if (isReadTitleEnabled && selectedDua.titleAudioResId != null) {
//                                                globalMediaPlayer = MediaPlayer.create(context, selectedDua.titleAudioResId)
//                                                globalMediaPlayer?.setOnCompletionListener {
//                                                    playWord(0, isAutoNextEnabled, false)
//                                                }
//                                                globalMediaPlayer?.start()
//                                            } else {
//                                                playWord(0, isAutoNextEnabled, isReadTitleEnabled)
//                                            }
//                                        }
//                                    }
//                                    else if (selectedTab == "COMPLETE") {
//                                        selectedTab = "COMPLETE"
//                                        val sharedPref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
//                                        val isReadTitleEnabled = sharedPref.getBoolean("read_title_enabled", false)
//                                        val isAutoNextEnabled = sharedPref.getBoolean("auto_next_duas_enabled", false)
//
//                                        fun stopAudioPlayback() {
//                                            if (globalMediaPlayer?.isPlaying == true) {
//                                                globalMediaPlayer?.stop()
//                                            }
//                                            globalMediaPlayer?.release()
//                                            globalMediaPlayer = null
//                                        }
//
//                                        fun playNextAudio(audioResId: Int, resumeFrom: Int = 0, onComplete: () -> Unit) {
//                                            stopAudioPlayback()
//                                            globalMediaPlayer = MediaPlayer.create(context, audioResId)
//                                            globalMediaPlayer?.seekTo(resumeFrom)
//                                            globalMediaPlayer?.setOnCompletionListener {
//                                                currentAudioPosition = 0
//                                                stopAudioPlayback()
//                                                onComplete()
//                                            }
//                                            globalMediaPlayer?.start()
//                                            isPlaying = true
//                                        }
//
//                                        fun getDuasForIndex(index: Int): List<Dua> {
//                                            return when {
//                                                index in threeDuaIndices -> duas.subList(index, minOf(index + 3, duas.size))
//                                                index in twoDuaIndices -> duas.subList(index, minOf(index + 2, duas.size))
//                                                else -> listOfNotNull(duas.getOrNull(index))
//                                            }
//                                        }
//
//                                        fun buildAudioQueue(index: Int): List<Int> {
//                                            val queue = mutableListOf<Int>()
//                                            val duasForIndex = getDuasForIndex(index)
//                                            if (isReadTitleEnabled) {
//                                                duasForIndex.firstOrNull()?.titleAudioResId?.let { queue.add(it) }
//                                            }
//                                            for (dua in duasForIndex) {
//                                                queue.add(dua.fullAudioResId)
//                                            }
//                                            return queue
//                                        }
//
//                                        fun playQueue(queue: List<Int>, index: Int, startFrom: Int, resumeFrom: Int = 0, onFinished: () -> Unit) {
//                                            val duasForIndex = getDuasForIndex(index)
//
//                                            fun playAt(i: Int, resumeFrom: Int) {
//                                                if (i >= queue.size) {
//                                                    onFinished()
//                                                    return
//                                                }
//
//                                                currentAudioQueueIndex = i
//                                                currentPlayingIndex = when {
//                                                    isReadTitleEnabled && i == 0 -> index
//                                                    else -> {
//                                                        val duaStart = if (isReadTitleEnabled) 1 else 0
//                                                        val duaIndex = index + (i - duaStart)
//                                                        duaIndex
//                                                    }
//                                                }
//
//                                                playNextAudio(queue[i], resumeFrom) {
//                                                    playAt(i + 1, 0)
//                                                }
//                                            }
//
//                                            playAt(startFrom, resumeFrom)
//                                        }
//
//                                        fun playFromIndex(index: Int) {
//                                            if (index >= duas.size) {
//                                                isPlaying = false
//                                                showListening = false
//                                                return
//                                            }
//
//                                            currentIndex = index
//                                            currentPlayingIndex = index
//                                            isPlaying = true
//                                            showListening = false
//
//                                            if (!wasPaused) {
//                                                currentAudioQueue = buildAudioQueue(index)
//                                                currentAudioQueueIndex = 0
//                                                currentAudioPosition = 0
//                                            }
//
//                                            playQueue(currentAudioQueue, index, currentAudioQueueIndex, currentAudioPosition) {
//                                                wasPaused = false
//                                                if (isAutoNextEnabled) {
//                                                    val nextIndex = index + getDuasForIndex(index).size
//                                                    if (nextIndex < duas.size) {
//                                                        currentAudioQueueIndex = 0
//                                                        currentAudioPosition = 0
//                                                        playFromIndex(nextIndex)
//                                                    } else {
//                                                        isPlaying = false
//                                                    }
//                                                } else {
//                                                    isPlaying = false
//                                                }
//                                            }
//                                        }
//
//                                        if (isPlaying) {
//                                            // Pause and save state
//                                            stopAudioPlayback()
//                                            currentAudioPosition = globalMediaPlayer?.currentPosition ?: 0
//                                            globalMediaPlayer?.pause()
//                                            isPlaying = false
//                                            showListening = false
//                                            wasPaused = true
//                                        } else {
//                                            if (wasPaused && currentIndex == i) {
//                                                // Resume same dua
//                                                playFromIndex(currentIndex)
//                                            } else {
//                                                // New dua or first-time play
//                                                wasPaused = false
//                                                currentAudioQueue = buildAudioQueue(i)     // REBUILD for new index
//                                                currentAudioQueueIndex = 0
//                                                currentAudioPosition = 0
//                                                playFromIndex(i)
//                                            }
//                                        }
//
//                                    }
//
//                                }
//                            )
//                            Box(
//                                modifier = Modifier.padding(top = 2.dp)
//                            ) {
//                                IconButton(
//                                    onClick = {
//                                        when {
//                                            repeatCount < 5 -> repeatCount++
//                                            repeatCount == 5 -> repeatCount = Int.MAX_VALUE
//                                            repeatCount == Int.MAX_VALUE -> {
//                                                repeatCount = 0
//                                                isRepeatMode = false
//                                            }
//                                        }
//                                        if (repeatCount > 0) {
//                                            isRepeatMode = true
//                                            currentRepeat = 0
//                                        }
//                                    },
//                                    modifier = Modifier.align(Alignment.TopEnd)
//                                ) {
//                                    val iconRes = if (repeatCount > 0) {
//                                        R.drawable.repeat_1_time_btn
//                                    } else {
//                                        R.drawable.repeat_off_btn
//                                    }
//
//                                    Image(
//                                        painter = painterResource(id = iconRes),
//                                        contentDescription = "Repeat",
//                                        modifier = Modifier.size(33.dp)
//                                    )
//                                }
//
//                                if (repeatCount > 0 && currentPlayingIndex == i) {
//                                    val badgeText = if (repeatCount == Int.MAX_VALUE) "∞" else repeatCount.toString()
//
//                                    Box(
//                                        contentAlignment = Alignment.Center,
//                                        modifier = Modifier
//                                            .size(18.dp)
//                                            .background(
//                                                colorResource(R.color.badge_color),
//                                                shape = CircleShape
//                                            )
//                                            .align(Alignment.TopEnd)
//                                            .offset(x = (-2).dp, y = 2.dp)
//                                    ) {
//                                        Text(
//                                            text = badgeText,
//                                            color = colorResource(R.color.white),
//                                            fontSize = 10.sp,
//                                            fontWeight = FontWeight.Bold,
//                                            lineHeight = 10.sp
//                                        )
//                                    }
//                                }
//                            }
//                        }
//                        Spacer(modifier = Modifier.height(9.dp))
//
//                        dua.steps?.let {
//                            Text(
//                                text = it,
//                                fontSize = 12.sp,
//                                fontFamily = translationtext,
//                                fontWeight = FontWeight.SemiBold,
//                                textAlign = TextAlign.Center,
//                                color = colorResource(R.color.heading_color),
//                                modifier = Modifier
//                                    .align(Alignment.CenterHorizontally)
//                                    .padding(start = 20.dp, end = 20.dp)
//                            )
//                        }
//
//                        Spacer(modifier = Modifier.height(5.dp))
//
//                        val sharedPref =
//                            context.getSharedPreferences(
//                                "app_prefs",
//                                Context.MODE_PRIVATE
//                            )
//                        val savedFontSize = sharedPref.getFloat("font_size", 24f)
//
//                        val annotatedText = buildAnnotatedString {
//                            dua.wordAudioPairs.forEachIndexed { index, pair ->
//                                pushStringAnnotation("WORD", index.toString())
//                                withStyle(
//                                    style = SpanStyle(
//                                        color = if (globalWordIndex == index && currentPlayingIndex == i)
//                                            colorResource(R.color.highlited_color)
//                                        else colorResource(R.color.arabic_color),
//                                        fontWeight = if (globalWordIndex == index && currentPlayingIndex == i)
//                                            FontWeight.Bold
//                                        else FontWeight.Normal
//                                    )
//                                ) {
//                                    append(pair.first + " ")
//                                }
//                                pop()
//                            }
//                        }
//
//                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
//                            ClickableText(
//                                text = annotatedText,
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(start = 25.dp, end = 25.dp),
//                                style = TextStyle(
//                                    fontSize = savedFontSize.sp,
//                                    fontFamily = MyArabicFont,
//                                    textDirection = TextDirection.Rtl,
//                                    textAlign = TextAlign.Center
//                                ),
//                                onClick = { offset ->
//                                    annotatedText.getStringAnnotations(
//                                        "WORD",
//                                        offset,
//                                        offset
//                                    )
//                                        .firstOrNull()?.let { annotation ->
//                                            val clickedIndex =
//                                                annotation.item.toInt()
//                                            globalMediaPlayer?.release()
//                                            globalMediaPlayer = MediaPlayer.create(
//                                                context,
//                                                dua.wordAudioPairs[clickedIndex].second
//                                            )
//                                            currentPlayingIndex = i
//                                            globalWordIndex = clickedIndex
//                                            globalMediaPlayer?.setOnCompletionListener {
//                                                globalWordIndex = -1
//                                            }
//                                            globalMediaPlayer?.start()
//                                        }
//                                }
//                            )
//                        }
//                        Spacer(modifier = Modifier.height(5.dp))
//                        Column(
//                            modifier = Modifier.padding(
//                                start = 20.dp,
//                                end = 20.dp
//                            )
//                        ) {
//                            if (selectedLanguages.isEmpty()) {
//                                Box(modifier = Modifier.fillMaxWidth()) {
//                                    Text(
//                                        text = "No translation language selected.",
//                                        fontFamily = translationtext,
//                                        color = Color.Gray,
//                                        textAlign = TextAlign.Center,
//                                        fontSize = 14.sp,
//                                        modifier = Modifier
//                                            .align(Alignment.Center)
//                                    )
//                                }
//                            } else {
//                                selectedLanguages.forEach { lang ->
//                                    when {
//                                        lang.equals(
//                                            "English",
//                                            ignoreCase = true
//                                        ) -> {
//                                            dua.translation?.let {
//                                                Box(modifier = Modifier.fillMaxWidth()) {
//                                                    Text(
//                                                        text = it,
//                                                        fontFamily = translationtext,
//                                                        fontSize = responsiveFontSize(),
//                                                        textAlign = TextAlign.Center,
//                                                        modifier = Modifier
//                                                            .align(Alignment.Center)
//
//                                                    )
//
//                                                }
//                                            }
//                                            Spacer(modifier = Modifier.height(10.dp))
//                                        }
//
//                                        lang.equals("Urdu", ignoreCase = true) -> {
//                                            dua.urdu?.let {
//                                                Text(
//                                                    text = it,
//                                                    fontFamily = translationtext,
//                                                    fontSize = responsiveFontSize(),
//                                                    textAlign = TextAlign.Center,
//                                                )
//                                            }
//                                            Spacer(modifier = Modifier.height(15.dp))
//
//                                        }
//
//                                        lang.equals("Hindi", ignoreCase = true) -> {
//                                            dua.hinditranslation?.let {
//                                                Text(
//                                                    text = it,
//                                                    fontFamily = translationtext,
//                                                    fontSize = responsiveFontSize(),
//                                                    textAlign = TextAlign.Center,
//                                                )
//                                            }
//                                            Spacer(modifier = Modifier.height(15.dp))
//                                        }
//                                    }
//                                }
//                            }
//                            Spacer(modifier = Modifier.height(5.dp))
//
//                        }
//                        Box(modifier = Modifier.fillMaxWidth()) {
//                            Text(
//                                text = dua.reference,
//                                fontSize = 10.sp,
//                                fontFamily = reference,
//                                color = colorResource(R.color.reference_color),
//                                textAlign = TextAlign.Center,
//                                modifier = Modifier
//                                    .align(Alignment.Center)
//                                    .padding(bottom = 20.dp)
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//private var wordHandler: Handler? = null
//private var wordRunnable: Runnable? = null
