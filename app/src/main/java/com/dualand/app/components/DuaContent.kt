package com.dualand.app.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    duaViewModel: DuaViewModel

) {
    var globalWordIndex by duaViewModel::globalWordIndex
    val fontSize by duaViewModel.fontSize.collectAsState()
    //val currentPlayingIndex by viewModel::currentPlayingIndex

    val selectedTab by duaViewModel.selectedTab.collectAsState()
    val scrollState = rememberScrollState()
    val MyArabicFont = FontFamily(Font(R.font.vazirmatn_regular))
    val translationtext = FontFamily(Font(R.font.poppins_regular))
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
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState) // Move scroll here
                    .fillMaxWidth() // Avoid fillMaxSize()
            ) {
                duas.forEachIndexed { index, dua ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
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

                                val isFavorite by duaViewModel.isFavorite(dua.duaNumber)
                                    .collectAsState(initial = false)
                                IconButton(
                                    onClick = {
                                        duaViewModel.toggleFavoriteStatus(dua)
                                    }
                                ) {
                                    val iconRes =
                                        if (isFavorite) R.drawable.favourite_active_icon else R.drawable.favourite_icon
                                    Image(
                                        painterResource(iconRes),
                                        contentDescription = "Favorite",
                                        modifier = Modifier.size(34.dp)
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        if (duaViewModel.selectedTab.value == "WORD") {
                                            if (duaViewModel.isPlayingWordByWord) {
                                                duaViewModel.stopAudio()
                                            } else {
                                                duaViewModel.playWordByWord()
                                            }
                                        } else if (duaViewModel.selectedTab.value == "COMPLETE") {
                                            if (duaViewModel.isPlayingFullAudio) {
                                                duaViewModel.stopAudio()
                                            } else {
                                                duaViewModel.playFullAudio()
                                            }
                                        }

                                    }
                                ) {
                                    val iconRes = when (selectedTab) {
                                        "WORD" -> if (duaViewModel.isPlayingWordByWord) R.drawable.pause_icon else R.drawable.icon_playy
                                        "COMPLETE" -> if (duaViewModel.isPlayingFullAudio) R.drawable.pause_icon else R.drawable.icon_playy
                                        else -> R.drawable.icon_playy
                                    }
                                    Image(
                                        painter = painterResource(iconRes),
                                        contentDescription = "Play/Pause"
                                    )
                                }

                              RepeatButton()

                            }

                            val annotatedText = buildAnnotatedString {
                                dua.wordAudioPairs.forEachIndexed { wordIndex, pair ->
                                    withStyle(
                                        style = SpanStyle(
                                            color = if (duaViewModel.currentDuaIndex == index &&
                                                duaViewModel.currentWordIndexInDua == wordIndex
                                            ) colorResource(R.color.highlited_color)
                                            else colorResource(R.color.arabic_color),
                                            fontWeight = if (duaViewModel.currentDuaIndex == index &&
                                                duaViewModel.currentWordIndexInDua == wordIndex
                                            ) FontWeight.Bold else FontWeight.Normal
                                        )
                                    ) {
                                        append(pair.first + " ")
                                    }
                                }
                            }

                            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                                ClickableText(
                                    text = annotatedText,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 25.dp, end = 25.dp, top = 10.dp),
                                    style = TextStyle(
                                        fontSize = fontSize.sp,
                                        fontFamily = MyArabicFont,
                                        textDirection = TextDirection.Rtl,
                                        textAlign = TextAlign.Center
                                    ),
                                    onClick = {
                                    }
                                )
                            }

                            Text(
                                text = dua.translation,
                                fontSize = 13.sp,
                                fontFamily = translationtext,
                                color = colorResource(R.color.splash_black),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 7.dp),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = dua.reference,
                                fontSize = 10.sp,
                                color = colorResource(R.color.reference_color),
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
