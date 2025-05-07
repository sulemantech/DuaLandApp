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
            .height(55.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.complete_dua_word_by_word_dua_button),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable {
                        onStopCompleteDua()
                        onTabSelected("WORD")
                        onPlayWordByWordButton()
                    }
            ) {
                if (currentTab == "WORD") {
                    Image(
                        painter = painterResource(id = R.drawable.selected_tab1),
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
                        text = "WORD BY WORD",
                        color = if (currentTab == "WORD") Color.Black else Color.White,
                        fontFamily = MyArabicFont,
                        fontSize = 18.sp
                    )
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable {
                        onTabSelected("COMPLETE")
                        onStopCompleteDua()
                        onCompleteDuaClick(dua.fullAudioResId)
                    }
            ) {
                if (currentTab == "COMPLETE") {
                    Image(
                        painter = painterResource(id = R.drawable.selected_tab2),
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
                        color = if (currentTab == "COMPLETE") Color.Black else Color.White,
                        fontFamily = MyArabicFont,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}
