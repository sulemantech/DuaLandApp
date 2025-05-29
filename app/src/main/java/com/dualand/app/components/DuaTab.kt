package com.dualand.app.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text as MaterialText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dualand.app.DuaViewModel
import com.dualand.app.R
import com.dualand.app.models.Dua
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel


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
            color = if (isSelected) colorResource(R.color.white) else colorResource(R.color.heading_color),
            fontFamily = fontFamily,
            fontSize = 16.sp,
        )
    }
}

@Composable
fun DuaTabs(
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    val MyArabicFont = FontFamily(Font(R.font.doodlestrickers))

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val horizontalPadding = 20.dp * 2
    val tabContainerWidth = screenWidth - horizontalPadding
    val tabWidth = tabContainerWidth / 2
    val tabOffset = if (selectedTab == "WORD") 0.dp else tabWidth

    val animatedOffset by animateDpAsState(targetValue = tabOffset, label = "TabOffset")

    val totalHeight = 50.dp

    val activeColor = colorResource(id = R.color.highlited_color)
    val inactiveColor = Color.Transparent

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(totalHeight)
            .padding(horizontal = 20.dp)
    ) {
        // Background container image
        Image(
            painter = painterResource(id = R.drawable.tab_bg_pink),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )

        // Animated sliding highlight image
        Box(
            modifier = Modifier
                .height(totalHeight + 5.dp) // slightly taller to match design
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

        // Tabs row with texts
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onTabSelected("WORD") },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "WORD BY WORD",
                    color = if (selectedTab == "WORD") Color.White else colorResource(R.color.heading_color),
                    fontFamily = MyArabicFont,
                    fontSize = 18.sp
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onTabSelected("COMPLETE") },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "COMPLETE DUA",
                    color = if (selectedTab == "COMPLETE") Color.White else colorResource(R.color.heading_color),
                    fontFamily = MyArabicFont,
                    fontSize = 18.sp
                )
            }
        }
    }
}
