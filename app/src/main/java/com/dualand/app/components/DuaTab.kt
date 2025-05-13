package com.dualand.app.components

import com.dualand.app.R

import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material.Text
import com.dualand.app.models.Dua

@Composable
fun DuaTabs(
    dua: Dua,
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    onStopCompleteDua: () -> Unit = {},
    onStartWordByWord: () -> Unit = {},
    onPlayWordByWordButton: () -> Unit,
    onCompleteDuaClick: (Int) -> Unit = {}
) {
    val currentTab = if (selectedTab.isEmpty()) "WORD" else selectedTab
    val MyArabicFont = FontFamily(Font(R.font.doodlestrickers))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.tab_bg_pink),
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
                    .fillMaxSize()
                    .padding(top = 5.dp)
                    .clickable {
                        onStopCompleteDua()
                        onTabSelected("WORD")
                        onPlayWordByWordButton()
                    },
                contentAlignment = Alignment.Center
            ) {
                if (currentTab == "WORD") {
                    Image(
                        painter = painterResource(id = R.drawable.rectangle_tab),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .matchParentSize()

                    )
                }

                Text(
                    text = "WORD BY WORD",
                    color = if (currentTab == "WORD") Color.White else Color.Black,
                    fontFamily = MyArabicFont,
                    fontSize = 18.sp
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(5.dp)
                    .clickable {
                        onTabSelected("COMPLETE")
                        onStopCompleteDua()
                        onCompleteDuaClick(dua.fullAudioResId)
                    },
                contentAlignment = Alignment.Center
            ) {
                if (currentTab == "COMPLETE") {
                    Image(
                        painter = painterResource(id = R.drawable.rectangle_tab),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.matchParentSize()
                    )
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "COMPLETE DUA",
                        color = if (currentTab == "COMPLETE")Color.White else Color.Black,
                        fontFamily = MyArabicFont,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}
