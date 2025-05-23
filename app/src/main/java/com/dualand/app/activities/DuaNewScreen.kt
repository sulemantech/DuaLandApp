package com.dualand.app.activities

import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dualand.app.R
import androidx.compose.material3.Text // keep this one (preferred)
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import com.dualand.app.activities.DuaDataProvider.duaList
import com.dualand.app.components.AudioPlayerManager
import com.dualand.app.components.DuaContent
import com.dualand.app.components.DuaContentFooter
import com.dualand.app.components.DuaTabs
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun DuaNewScreen(
    navController: NavController,
    duaTitle: String = "",
    innerPadding: PaddingValues
) {
    val systemUiController = rememberSystemUiController()
    val navigationBarColor = colorResource(id = R.color.top_nav_new)
    val statusBarColor = colorResource(id = R.color.top_nav_new)
    val context = LocalContext.current
    var isPlayingWordByWord by remember { mutableStateOf(false) }
    var isPlayingFullAudio by remember { mutableStateOf(false) }
    val duaAudioController = remember { AudioPlayerManager(context) }
    var highlightedIndex by remember { mutableStateOf(-1) }


    val mediaPlayer = remember { MediaPlayer() }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }


    SideEffect {
        systemUiController.setStatusBarColor(color = statusBarColor)
        systemUiController.setNavigationBarColor(color = navigationBarColor)
    }

    val groupedAndSortedDuas = duaList.groupBy {
        it.duaNumber.substringBefore(".").trim().toInt()
    }.toSortedMap()

    val duaKeys = groupedAndSortedDuas.keys.toList()
    var currentIndex by remember { mutableStateOf(0) }
    val currentDua = groupedAndSortedDuas[duaKeys.getOrNull(currentIndex)] ?: emptyList()
    var selectedTab by rememberSaveable { mutableStateOf("WORD") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(currentIndex) {
                detectHorizontalDragGestures { _, dragAmount ->
                    if (dragAmount > 50 && currentIndex > 0) {
                        currentIndex--
                    } else if (dragAmount < -50 && currentIndex < duaKeys.lastIndex) {
                        currentIndex++
                    }
                }
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = colorResource(R.color.top_nav_new)),
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

                Row(modifier = Modifier.padding(horizontal = 6.dp)) {
                    Text(
                        text = currentDua.firstOrNull()?.duaNumber ?: duaTitle,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = currentDua.firstOrNull()?.textheading ?: duaTitle,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }

                IconButton(
                    onClick = { navController.navigate("InfoScreen") },
                    modifier = Modifier.padding(start = 4.dp, top = 5.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.info_icon),
                        contentDescription = "Info",
                        modifier = Modifier.size(29.dp, 30.dp)
                    )
                }
            }

            Image(
                painter = painterResource(id = currentDua.firstOrNull()?.image ?: R.drawable.kaaba),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            )

            Box(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
//                DuaTabs(
//                    dua = currentDua,
//                    selectedTab = selectedTab,
//                    onTabSelected = { selectedTab = it },
//                    onStopCompleteDua = { duaAudioController.stopAllAudio() },
//                    onPlayWordByWordButton = {
//                        duaAudioController.playWordByWordAudio(
//                            wordAudioPairs = currentDua.firstOrNull()?.wordAudioPairs,
//                            onHighlightIndex = { highlightedIndex = it },
//                            onComplete = { highlightedIndex = -1 }
//                        )
//                    },
//                    onCompleteDuaClick = {
//                        duaAudioController.playFullDuaAudio(
//                            audioResId = currentDua.firstOrNull()?.fullAudioResId,
//                            onComplete = {}
//                        )
//                    }
//                )

            }

            Spacer(modifier = Modifier.height(10.dp))

            DuaContent(
                duas = currentDua,
                innerPadding = PaddingValues(),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                highlightedIndex = highlightedIndex
            )
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            DuaContentFooter(
                onPreviousClick = { if (currentIndex > 0) currentIndex-- },
                onNextClick = { if (currentIndex < duaKeys.lastIndex) currentIndex++ }
            )
        }
    }
}

//@Composable
//fun TabSwitcher() {
//    var selectedTab by remember { mutableStateOf("word") }
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(top = 12.dp, bottom = 10.dp, start = 5.dp, end = 5.dp),
//        horizontalArrangement = Arrangement.SpaceEvenly,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Box(
//            modifier = Modifier
//                .height(45.dp)
//                .weight(1f)
//                .padding(top = 2.dp, bottom = 2.dp)
//                .clickable { selectedTab = "word" }
//        ) {
//            Image(
//                painter = painterResource(R.drawable.rectangle_tab),
//                contentDescription = null,
//                contentScale = ContentScale.FillBounds,
//                modifier = Modifier.matchParentSize()
//            )
//            Text(
//                text = "Word By Word",
//                color = if (selectedTab == "word") Color.White else Color.Black,
//                modifier = Modifier.align(Alignment.Center),
//                fontWeight = FontWeight.Bold
//            )
//        }
//
//        Spacer(modifier = Modifier.width(16.dp))
//        Box(
//            modifier = Modifier
//                .height(40.dp)
//                .weight(1f)
//                .clickable { selectedTab = "complete" }
//        ) {
//            Image(
//                painter = painterResource(R.drawable.rectangle_tab),
//                contentDescription = null,
//                contentScale = ContentScale.FillBounds,
//                modifier = Modifier.matchParentSize()
//            )
//            Text(
//                text = "Complete Dua",
//                color = if (selectedTab == "complete") Color.White else Color.Black,
//                modifier = Modifier.align(Alignment.Center),
//                fontWeight = FontWeight.Bold
//            )
//        }
//    }
//}

@Preview(showBackground = true)
@Composable
fun DuaNewScreenPreview() {
    DuaNewScreen(navController = rememberNavController(), innerPadding = PaddingValues(),)

}
