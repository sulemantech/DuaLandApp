package com.dualand.app.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed

@Composable
fun DuaContent(
    duas: List<Dua>,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
    highlightedIndex: Int,
    duaViewModel: DuaViewModel
) {
    val listState = rememberLazyListState()
    LaunchedEffect(highlightedIndex) {
        if (highlightedIndex in duas.indices) {
            listState.animateScrollToItem(highlightedIndex)
        }
    }

    val fontSize by duaViewModel.fontSize.collectAsState()
    val selectedTab by duaViewModel.selectedTab.collectAsState()
    val MyArabicFont = FontFamily(Font(R.font.vazirmatn_regular))
    val translationtext = FontFamily(Font(R.font.poppins_regular))

    LazyColumn(
        state = listState,
        contentPadding = innerPadding,
        modifier = modifier.padding(start = 10.dp, end = 10.dp)
    ) {
        itemsIndexed(duas) { index, dua ->

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
                        val isFavorite by duaViewModel.isFavorite(dua.duaNumber).collectAsState(initial = false)
                        IconButton(
                            onClick = { duaViewModel.toggleFavoriteStatus(dua) }
                        ) {
                            val iconRes = if (isFavorite) R.drawable.favourite_active_icon else R.drawable.favourite_icon
                            Image(
                                painterResource(iconRes),
                                contentDescription = "Favorite",
                                modifier = Modifier.size(34.dp)
                            )
                        }

                        val isCurrent = duaViewModel.currentDuaIndexState.value == index
                        val isListening = duaViewModel.isListeningPause.value && isCurrent
                        val isFullAudioCurrent = duaViewModel.currentFullAudioDuaGroupIndex.value == index
                        IconButton(
                            onClick = {
                                if (duaViewModel.selectedTab.value == "WORD") {
                                    if (duaViewModel.isPlayingWordByWord) duaViewModel.stopAudio() else duaViewModel.playWordByWord()
                                } else if (duaViewModel.selectedTab.value == "COMPLETE") {
                                    if (duaViewModel.isPlayingFullAudio) duaViewModel.stopAudio() else duaViewModel.playFullAudio()
                                }
                            }
                        ) {
                            val iconRes = when {
                                isListening -> R.drawable.icon_listening
                                selectedTab == "WORD" && isCurrent && duaViewModel.isPlayingWordByWord -> R.drawable.pause_icon
                                selectedTab == "COMPLETE" && isFullAudioCurrent && duaViewModel.isPlayingFullAudio -> R.drawable.pause_icon
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
                                // Handle word click if needed
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
