package com.dualand.app.components

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dualand.app.DuaViewModel
import com.dualand.app.R
import com.dualand.app.models.Dua
import androidx.compose.runtime.getValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.LayoutDirection

@OptIn(ExperimentalLayoutApi::class)
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun DuaContent(
    duas: List<Dua>,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
    highlightedIndex: Int,
    viewModel: DuaViewModel

) {
    var globalWordIndex by viewModel::globalWordIndex
    val fontSize by viewModel.fontSize.collectAsState()
    //val currentPlayingIndex by viewModel::currentPlayingIndex

    val selectedTab by viewModel.selectedTab.collectAsState()
    val scrollState = rememberScrollState()
    val MyArabicFont = FontFamily(Font(R.font.vazirmatn_regular))
    Column(modifier = modifier.padding(start = 10.dp, end = 10.dp)) {
        val phrases = duas.firstOrNull()?.wordAudioPairs ?: emptyList()
//        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//            phrases.forEachIndexed { index, (phrase, _) ->
//                Text(
//                    text = phrase,
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Medium,
//                    color = if (index == highlightedIndex) Color.Red else Color.Black
//                )
//            }
//        }
        BoxWithConstraints(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Column(
                modifier = Modifier
                   // .verticalScroll(scrollState)
                    .fillMaxWidth()
            ) {
                duas.forEach { dua ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
//                            .padding(vertical = 8.dp),
                        elevation = 0.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .background(Color.White)
                                .padding(start = 16.dp, end = 16.dp, bottom = 10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = { /* Play full */ }) {
                                    Image(
                                        painterResource(R.drawable.favourite_icon),
                                        contentDescription = "Play"
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        if (selectedTab == "WORD") {
                                            if (viewModel.isPlayingWordByWord) {
                                                viewModel.stopAudio()
                                            } else {
                                                viewModel.playWordByWord()
                                            }
                                        } else if (selectedTab == "COMPLETE") {
                                            if (viewModel.isPlayingFullAudio) {
                                                viewModel.stopAudio()
                                            } else {
                                                viewModel.playFullAudio()
                                            }
                                        }
                                    }
                                ) {
                                    val iconRes = when (selectedTab) {
                                        "WORD" -> {
                                            if (viewModel.isPlayingWordByWord) R.drawable.pause_icon else R.drawable.icon_playy
                                        }
                                        "COMPLETE" -> {
                                            if (viewModel.isPlayingFullAudio) R.drawable.pause_icon else R.drawable.icon_playy
                                        }
                                        else -> R.drawable.icon_playy
                                    }
                                    Image(
                                        painter = painterResource(iconRes),
                                        contentDescription = "Play/Pause"
                                    )
                                }
                                IconButton(onClick = { /* Stop */ }) {
                                    Image(
                                        painterResource(R.drawable.repeat_off_btn),
                                        contentDescription = "Stop"
                                    )
                                }
                            }

//                            Text(
//                                text = dua.wordAudioPairs.joinToString(" ") { it.first },
//                                fontSize = 20.sp,
//                                fontWeight = FontWeight.SemiBold,
//                                color = Color(0xFF1E88E5),
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(top = 7.dp),
//                                textAlign = TextAlign.Center
//                            )
                            val annotatedText = buildAnnotatedString {
                                dua.wordAudioPairs.forEachIndexed { index, pair ->
                                    pushStringAnnotation("WORD", index.toString())
                                    withStyle(
                                        style = SpanStyle(
                                            color = if (globalWordIndex == index ) // && viewModel.currentIndex == i)
                                                colorResource(R.color.highlited_color)
                                            else colorResource(R.color.arabic_color),
                                            fontWeight = if (globalWordIndex == index ) //&& viewModel.currentIndex == i)
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
                                        fontSize = fontSize.sp,
                                        fontFamily = MyArabicFont,
                                        textDirection = TextDirection.Rtl,
                                        textAlign = TextAlign.Center
                                    ),
                                    onClick = {
                            //                                    offset ->
//
//                                        annotatedText.getStringAnnotations("WORD", offset, offset)
//                                            .firstOrNull()?.let { annotation ->
//                                                val clickedIndex = annotation.item.toInt()
//                                                globalMediaPlayer?.release()
//                                                globalMediaPlayer = MediaPlayer.create(
//                                                    context,
//                                                    dua.wordAudioPairs[clickedIndex].second
//                                                )
//                                                currentPlayingIndex = i
//                                                globalWordIndex = clickedIndex
//                                                globalMediaPlayer?.setOnCompletionListener {
//                                                    globalWordIndex = -1
//                                                }
//                                                globalMediaPlayer?.start()
//                                            }
                                    }
                                )
                            }


                            Text(
                                text = dua.translation,
                                fontSize = 14.sp,
                                color = Color.DarkGray,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 7.dp),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = dua.reference,
                                fontSize = 10.sp,
                                color = Color.DarkGray,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 5.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}
