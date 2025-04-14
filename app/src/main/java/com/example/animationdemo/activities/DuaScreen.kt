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
    index: Int,
    navController: NavController
) {
    val systemUiController = rememberSystemUiController()
    val context = LocalContext.current

    val duas = duaList

    var currentIndex by remember { mutableStateOf(index.coerceIn(0, duas.lastIndex)) }
    val NavigationBarColor = colorResource(id = R.color.top_nav_new)
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    val statusBarColor = colorResource(id = duas[currentIndex].statusBarColorResId)
    val showCount = if (currentIndex < 4) 2 else 1
    var isPlaying by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) }
    var shouldStopPlayback by remember { mutableStateOf(false) }

    val MyArabicFont = FontFamily(Font(R.font.al_quran))
    val translationtext = FontFamily(Font(R.font.poppins_regular))
    val reference = FontFamily(Font(R.font.poppins_semibold))
    val title = FontFamily(Font(R.font.mochypop_regular))



    SideEffect {
        systemUiController.setStatusBarColor(color = statusBarColor)
        systemUiController.setNavigationBarColor(color = NavigationBarColor)

    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = duas[currentIndex].backgroundResId),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize(),
            //.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.top_nav_new))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.padding(start = 4.dp, top = 15.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.home_btn),
                        contentDescription = "Back",
                        modifier = Modifier.size(27.dp)
                    )
                }

                Text(
                    text = duas[currentIndex].textheading,
                    fontSize = 14.sp,
                    color = colorResource(R.color.heading_color),
                    fontFamily = title,
                    modifier = Modifier.padding(top = 15.dp)
                )

                IconButton(
                    onClick = {
                        navController.navigate("SettingsScreen")
                    },
                    modifier = Modifier.padding(end = 4.dp, top = 15.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.setting_btn),
                        contentDescription = "Settings",
                        modifier = Modifier.size(27.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(255.dp))

            DuaTabs()

            Column(
                modifier = Modifier
                    .height(375.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                for (i in currentIndex until (currentIndex + showCount).coerceAtMost(duas.size)) {
                    val dua = duas[i]
                    var wordIndex by remember { mutableStateOf(-1) }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(-4.dp, Alignment.CenterHorizontally
                        )
                    ) {
                        IconButton(onClick = {
                            if (mediaPlayer != null && isPlaying) {
                                if (isPaused) {

                                    mediaPlayer?.start()
                                    isPaused = false
                                } else {
                                    mediaPlayer?.pause()
                                    isPaused = true
                                }
                            } else {
                                mediaPlayer?.release()
                                mediaPlayer = MediaPlayer.create(context, dua.fullAudioResId)
                                mediaPlayer?.setOnCompletionListener {
                                    wordIndex = -1
                                    isPlaying = false
                                    isPaused = false
                                }
                                mediaPlayer?.start()
                                isPlaying = true
                                isPaused = false
                            }
                        }) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_pause),
                                contentDescription = "Play/Pause Full",
                                modifier = Modifier.size(33.dp)
                            )
                        }

                        PlayWordByWordButton {
                            // Your play logic
                            fun playWord(index: Int) {
                                if (index >= dua.wordAudioPairs.size) {
                                    wordIndex = -1
                                    return
                                }
                                val (_, audioResId) = dua.wordAudioPairs[index]
                                mediaPlayer?.release()
                                mediaPlayer = MediaPlayer.create(context, audioResId)
                                wordIndex = index
                                mediaPlayer?.setOnCompletionListener {
                                    playWord(index + 1)
                                }
                                mediaPlayer?.start()
                            }
                            playWord(0)
                        }



                        IconButton(onClick = {
                            shouldStopPlayback = true
                            mediaPlayer?.stop()
                            mediaPlayer?.release()
                            mediaPlayer = null
                            wordIndex = -1
                        }) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_stop_btn_pink),
                                contentDescription = "Stop Word-by-Word",
                                modifier = Modifier.size(33.dp)
                            )
                        }

                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    dua.steps?.let { stepsText ->
                        Text(
                            text = stepsText,
                            fontSize = 12.sp,
                            fontFamily = translationtext,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.heading_color),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    val annotatedText = buildAnnotatedString {
                        dua.wordAudioPairs.forEachIndexed { index, pair ->
                            pushStringAnnotation(tag = "WORD", annotation = index.toString())
                            withStyle(
                                style = SpanStyle(
                                    color = if (wordIndex == index) colorResource(R.color.highlited_color) else colorResource(
                                        R.color.arabic_color
                                    ),
                                    fontWeight = if (wordIndex == index) FontWeight.Bold else FontWeight.Normal
                                )
                            ) {
                                append(pair.first)
                                append(" ")
                            }
                            pop()
                        }
                    }

                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                        ClickableText(
                            text = annotatedText,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp),
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
                                        mediaPlayer?.release()
                                        mediaPlayer = MediaPlayer.create(
                                            context,
                                            dua.wordAudioPairs[clickedIndex].second
                                        )
                                        wordIndex = clickedIndex
                                        mediaPlayer?.setOnCompletionListener {
                                            wordIndex = -1
                                        }
                                        mediaPlayer?.start()
                                    }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = dua.translation,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            fontFamily = translationtext,
                            lineHeight = 18.sp,
                            color = colorResource(R.color.translation_color),
                            modifier = Modifier.padding(start = 20.dp, end = 20.dp)
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        Text(
                            text = dua.reference,
                            fontSize = 10.sp,
                            fontFamily = reference,
                            color = colorResource(R.color.reference_color),
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                val step = if (currentIndex < 4) 2 else 1
                currentIndex = if (currentIndex - step < 0) 0 else currentIndex - step
                mediaPlayer?.release()
            }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_backarrow),
                    contentDescription = "Previous Dua",
                    modifier = Modifier.size(height = 27.dp, width = 27.dp)
                )
            }

            IconButton(onClick = {
                val step = if (currentIndex < 4) 2 else 1
                currentIndex =
                    if (currentIndex + step >= duas.size) 0 else currentIndex + step
                mediaPlayer?.release()
            }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_nextarrow),
                    contentDescription = "Next Dua",
                    modifier = Modifier.size(height = 27.dp, width = 27.dp)
                )
            }
        }
    }
}

@Composable
fun PlayWordByWordButton(
    onClick: () -> Unit
) {
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
        Image(
            painter = painterResource(id = R.drawable.play_icon1),
            contentDescription = "Play Word-by-Word",
            modifier = Modifier.fillMaxSize()
        )
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
    // You can load an actual image from painterResource(R.drawable.kaaba)
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
fun DuaTabs() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TabButton("WORD BY WORD")

            Spacer(modifier = Modifier.width(15.dp))

            Spacer(modifier = Modifier.width(15.dp))

            TabButton("COMPLETE DUA")
        }
    }
}

@Composable
fun TabButton(text: String, modifier: Modifier = Modifier) {
    val MyDoodleFont = FontFamily(Font(R.font.doodlestrickers))

    Box(
        modifier = modifier
            .padding(bottom = 2.dp),
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
    DuaScreen(index = 0, navController = fakeNavController)
}



