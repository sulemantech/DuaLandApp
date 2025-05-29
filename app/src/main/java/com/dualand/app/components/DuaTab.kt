package com.dualand.app.components

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
        MaterialText(
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
    val activeColor = colorResource(id = R.color.highlited_color)
    val inactiveColor = Color(0xFFF5F5F5) // or your `tab_selected` color
    val tabShape = RoundedCornerShape(25.dp)
    val text_font1 = FontFamily(Font(R.font.doodlestrickers))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(inactiveColor, tabShape)
            .height(50.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Word by Word Tab
            Box(
                modifier = Modifier
                    .size(45.dp)               // width = height = 45.dp for a perfect circle
                    .clip(CircleShape)
                    .weight(1f)
                    .background(if (selectedTab == "WORD") activeColor else Color.Transparent)
                    .clickable { onTabSelected("WORD") },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "WORD BY WORD",
                    color = if (selectedTab == "WORD") Color.White else colorResource(R.color.heading_color),
                    fontFamily = text_font1,
                    fontSize = 18.sp
                )
            }

            // Complete Dua Tab
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(tabShape)
                    .background(if (selectedTab == "COMPLETE") activeColor else Color.Transparent)
                    .clickable { onTabSelected("COMPLETE") }
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "COMPLETE DUA",
                    color = if (selectedTab == "COMPLETE") Color.White else colorResource(R.color.heading_color),
                    fontFamily = text_font1,
                    fontSize = 18.sp
                )
            }
        }
    }
}
