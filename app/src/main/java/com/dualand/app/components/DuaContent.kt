package com.dualand.app.components

import android.annotation.SuppressLint
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dualand.app.R
import com.dualand.app.models.Dua

@OptIn(ExperimentalLayoutApi::class)
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun DuaContent(
    duas: List<Dua>,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
    highlightedIndex: Int

) {
    val scrollState = rememberScrollState()
    Column(modifier = modifier.padding(16.dp)) {
        val phrases = duas.firstOrNull()?.wordAudioPairs ?: emptyList()
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            phrases.forEachIndexed { index, (phrase, _) ->
                Text(
                    text = phrase,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (index == highlightedIndex) Color.Red else Color.Black
                )
            }
        }
        BoxWithConstraints(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize() // ensures full height context
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .fillMaxWidth()
            ) {
                duas.forEach { dua ->
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
                                IconButton(onClick = { /* Play full */ }) {
                                    Image(
                                        painterResource(R.drawable.favourite_icon),
                                        contentDescription = "Play"
                                    )
                                }

                                IconButton(onClick = { /* Play word by word */ }) {
                                    Image(
                                        painterResource(R.drawable.icon_playy),
                                        contentDescription = "Word by Word"
                                    )
                                }

                                IconButton(onClick = { /* Stop */ }) {
                                    Image(
                                        painterResource(R.drawable.repeat_off_btn),
                                        contentDescription = "Stop"
                                    )
                                }
                            }

                            Text(
                                text = dua.wordAudioPairs.joinToString(" ") { it.first },
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1E88E5),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 7.dp),
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = dua.translation,
                                fontSize = 14.sp,
                                color = Color.DarkGray,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 7.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}
