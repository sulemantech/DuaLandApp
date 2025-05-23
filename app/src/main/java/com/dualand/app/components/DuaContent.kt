package com.dualand.app.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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

@Composable
fun DuaContent(
    duas: List<Dua>,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .padding(innerPadding)
            .verticalScroll(scrollState)
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
                        text = dua.arabic,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1E88E5),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = dua.translation,
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = dua.urdu,
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
