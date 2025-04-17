package com.example.animationdemo.activities

import android.media.MediaPlayer
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
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
    val twoDuaIndices = setOf(0, 1, 2, 3, 6, 7, 15, 16, 18, 21,22,27,28,30,31)
    val threeDuaIndices = setOf(18, 19, 20,35,36,37)

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
    var lastStep by remember { mutableStateOf(1) }

    SideEffect {
        systemUiController.setStatusBarColor(color = statusBarColor)
        systemUiController.setNavigationBarColor(color = NavigationBarColor)
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
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
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

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 45.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(innerPadding)
                            .verticalScroll(rememberScrollState())
                    ) {
                        for (i in currentIndex until (currentIndex + showCount).coerceAtMost(duas.size)) {
                            val dua = duas[i]

                            var localMediaPlayer by remember(dua) {
                                mutableStateOf<MediaPlayer?>(
                                    null
                                )
                            }
                            var localWordIndex by remember(dua) { mutableStateOf(-1) }
                            var localIsPlaying by remember(dua) { mutableStateOf(false) }
                            var localIsPaused by remember(dua) { mutableStateOf(false) }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp),
                                horizontalArrangement = Arrangement.spacedBy(
                                    -4.dp,
                                    Alignment.CenterHorizontally
                                )
                            ) {

                                IconButton(onClick = {
                                }) {
                                    Image(
                                        painter = painterResource(id = R.drawable.favourite_icon),
                                        contentDescription = "Play Full Audio",
                                        modifier = Modifier.size(33.dp)
                                    )
                                }

                                PlayWordByWordButton {
                                    fun playWord(index: Int) {
                                        if (index >= dua.wordAudioPairs.size) {
                                            localWordIndex = -1
                                            return
                                        }
                                        val (_, audioResId) = dua.wordAudioPairs[index]
                                        localMediaPlayer?.release()
                                        localMediaPlayer = MediaPlayer.create(context, audioResId)
                                        localWordIndex = index
                                        localMediaPlayer?.setOnCompletionListener {
                                            playWord(index + 1)
                                        }
                                        localMediaPlayer?.start()
                                    }
                                    playWord(0)
                                }

                                IconButton(onClick = {
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
                                            color = if (localWordIndex == index)
                                                colorResource(R.color.highlited_color)
                                            else colorResource(R.color.arabic_color),
                                            fontWeight = if (localWordIndex == index)
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
                                        .padding(start = 25.dp,end=25.dp),
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
                                                localMediaPlayer?.release()
                                                localMediaPlayer = MediaPlayer.create(
                                                    context,
                                                    dua.wordAudioPairs[clickedIndex].second
                                                )
                                                localWordIndex = clickedIndex
                                                localMediaPlayer?.setOnCompletionListener {
                                                    localWordIndex = -1
                                                }
                                                localMediaPlayer?.start()
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
                                    fontSize = 15.sp,
                                    textAlign = TextAlign.Center,
                                    fontFamily = translationtext,
                                    lineHeight = 18.sp,
                                    color = colorResource(R.color.translation_color),
                                    modifier = Modifier.padding(start = 15.dp, end = 15.dp)
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
                    Spacer(modifier = Modifier.height(8.dp))
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

                        IconButton(
                            onClick = {
                                val step = when {
                                    (currentIndex - 1) in threeDuaIndices -> 3
                                    (currentIndex - 1) in twoDuaIndices -> 2
                                    else -> 1
                                }

                                currentIndex = (currentIndex - step).coerceAtLeast(0)
                            },
                            modifier = Modifier.padding(start = 15.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_backarrow),
                                contentDescription = "Previous Dua",
                                modifier = Modifier.size(29.dp, 30.dp)
                            )
                        }

                        IconButton(
                            onClick = {
                                val step = when {
                                    currentIndex in threeDuaIndices -> 3
                                    currentIndex in twoDuaIndices -> 2
                                    else -> 1
                                }
                                val newIndex = currentIndex + step
                                if (newIndex < duas.size) {
                                    currentIndex = newIndex
                                    lastStep = step
                                }
                            },
                            modifier = Modifier.padding(end = 15.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_nextarrow),
                                contentDescription = "Next Dua",
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
    onClick: () -> Unit
) {
    var isPlaying by remember { mutableStateOf(false) }
    var localWordIndex by remember { mutableIntStateOf(0) }
    var localMediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.85f else 1f,
        animationSpec = tween(durationMillis = 100),
        label = "ButtonPressScale"
    )

    Box(
        modifier = Modifier
            .size(width = 45.dp, height = 46.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        pressed = true
                        tryAwaitRelease()
                        pressed = false
                        onClick()
                    }
                )
            }
    ) {
        val iconRes = if (isPlaying) R.drawable.pause_icon else R.drawable.icon_playy
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = if (isPlaying) "Pause" else "Play",
            modifier = Modifier.fillMaxSize()
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            localMediaPlayer?.release()
            localMediaPlayer = null
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
fun PraiseAndGloryScreen1(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .background(Color(0xFFFFF9C4)) // Light yellow background
                .fillMaxSize()
        ) {
            // Top Bar
            TopAppBarContent()

            // Kaaba Illustration Placeholder
            KaabaIllustration()

            // Tabs
            DuaTabs()

            Spacer(Modifier.height(12.dp))

            // Dua Sections
            DuaSection(
                arabicText = listOf(
                    "سُبْحَانَ اللَّهِ",
                    "وَبِحَمْدِهِ",
                    "سُبْحَانَ اللَّهِ",
                    "الْعَظِيمِ"
                ),
                translation = "Glory be to Allah and all praise be to Him; Glory be to Allah, the Most Great.",
                reference = "[Sahih Muslim]"
            )

            Spacer(Modifier.height(16.dp))

            DuaSection(
                title = "Recite in the Morning",
                arabicText = listOf(
                    "اللَّهُ أَكْبَرُ",
                    "كَبِيرًا",
                    "وَالْحَمْدُ لِلَّهِ",
                    "كَثِيرًا",
                    "وَسُبْحَانَ اللَّهِ",
                    "بُكْرَةً",
                    "وَأَصِيلًا"
                ),
                translation = "Allah is truly Great, praise be to Allah in abundance and glory be to Allah in the morning and evening."
            )

            Spacer(modifier = Modifier.height(80.dp))
        }

        // Bottom Navigation
        BottomNavControls()
    }
}

@Composable
fun TopAppBarContent() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {}) {
            Icon(Icons.Default.Home, contentDescription = null, tint = Color.Magenta)
        }
        Text("Praise and Glory", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        IconButton(onClick = {}) {
            Icon(Icons.Default.Settings, contentDescription = null, tint = Color.Magenta)
        }
    }
}

@Composable
fun KaabaIllustration() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Text("Kaaba Illustration", color = Color.DarkGray)
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
            painter = painterResource(id = R.drawable.tab_btns),
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


@Composable
fun TabButton(text: String, modifier: Modifier = Modifier) {
    val MyDoodleFont = FontFamily(Font(R.font.doodlestrickers))

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = Color.White, fontSize = 18.sp, fontFamily = MyDoodleFont)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DuaSection(
    title: String? = null,
    arabicText: List<String>,
    translation: String,
    reference: String? = null
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        AudioControls()

        title?.let {
            Text(
                it,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 6.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        FlowRow(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            arabicText.forEach { word ->
                Text(
                    text = word,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (word == "وَبِحَمْدِهِ" || word == "وَسُبْحَانَ اللَّهِ") Color.Red else Color(
                        0xFF2196F3
                    ),
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }

        Text(
            text = translation,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        )

        reference?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Light,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun AudioControls() {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = {}) {
            Icon(Icons.Default.Person, contentDescription = "Prev", tint = Color(0xFFD81B60))
        }
        IconButton(onClick = {}) {
            Image(
                painter = painterResource(id = R.drawable.ic_play), // replace with your actual drawable name
                contentDescription = "Play",
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(Color(0xFFD81B60)) // optional tint
            )
        }
        IconButton(onClick = {}) {
            Icon(Icons.Default.Star, contentDescription = "Stop", tint = Color(0xFFD81B60))
        }
    }
}

@Composable
fun BottomNavControls(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(Color(0xFFFFF9C4))
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {}) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFFD81B60))
        }
        IconButton(onClick = {}) {
            Icon(
                Icons.Default.ArrowForward,
                contentDescription = "Forward",
                tint = Color(0xFFD81B60)
            )
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
fun PraiseAndGloryScreen1Preview() {
    PraiseAndGloryScreen1(navController = rememberNavController())
}

@Preview(showBackground = true)
@Composable
fun DuaScreenPreview() {
    val fakeNavController = rememberNavController()
    DuaScreen(index = 0, navController = fakeNavController, innerPadding = PaddingValues())
}



