package com.dualand.app.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dualand.app.R

@Composable
fun RepeatButton() {
    // 0 = repeat off, 1..5 repeat counts, -1 = infinity
    var repeatCount by remember { mutableStateOf(0) }

    // Decide icon based on repeatCount
    val iconRes = if (repeatCount == 0) {
        R.drawable.repeat_off_btn  // your repeat off icon
    } else {
        R.drawable.repeat_1_time_btn   // your repeat on icon
    }

    Box(modifier = Modifier.size(48.dp)) {
        IconButton(
            onClick = {
                repeatCount = when (repeatCount) {
                    0 -> 1
                    1 -> 2
                    2 -> 3
                    3 -> 4
                    4 -> 5
                    5 -> -1  // infinity
                    else -> 0 // back to off
                }
            },
            modifier = Modifier.align(Alignment.Center)
        ) {
            Image(
                painter = painterResource(iconRes),
                contentDescription = "Repeat Dua",
                modifier = Modifier.size(34.dp)
            )
        }

        // Badge for repeat count if repeatCount > 0
        if (repeatCount != 0) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(Color.Red, shape = CircleShape)
                    .align(Alignment.TopEnd),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (repeatCount == -1) "∞" else repeatCount.toString(),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}