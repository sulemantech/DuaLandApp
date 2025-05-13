package com.dualand.app.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dualand.app.R
import com.dualand.app.models.Dua

@Composable
private fun TabItem(
    text: String,
    isSelected: Boolean,
    fontFamily: FontFamily,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else Color.Black,
            fontFamily = fontFamily,
            fontSize = 16.sp,
        )
    }
}

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

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val horizontalPadding = 20.dp * 2
    val tabContainerWidth = screenWidth - horizontalPadding
    val tabWidth = tabContainerWidth / 2
    val tabOffset = if (currentTab == "WORD") 0.dp else tabWidth

    val animatedOffset by animateDpAsState(targetValue = tabOffset, label = "TabOffset")

    val totalWidth = 158.dp
    val totalHeight = 50.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(totalHeight)
            .padding(horizontal = 20.dp)
    )
    {
        Image(
            painter = painterResource(id = R.drawable.tab_bg_pink),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )

        Box(
            modifier = Modifier
                .height(55.dp) // match parent height
                .width(tabWidth)
                .offset(x = animatedOffset),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.rectangle_tab),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
            )
        }

        Row(
            modifier = Modifier.fillMaxSize(),
        ) {
            TabItem(
                text = "WORD BY WORD",
                isSelected = currentTab == "WORD",
                fontFamily = MyArabicFont,
                modifier = Modifier.weight(1f),
                onClick = {
                    onStopCompleteDua()
                    onTabSelected("WORD")
                    onPlayWordByWordButton()
                }
            )
            TabItem(
                text = "COMPLETE DUA",
                isSelected = currentTab == "COMPLETE",
                fontFamily = MyArabicFont,
                modifier = Modifier.weight(1f),
                onClick = {
                    onTabSelected("COMPLETE")
                    onStopCompleteDua()
                    onCompleteDuaClick(dua.fullAudioResId)
                }
            )
        }
    }
}
