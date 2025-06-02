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
import com.dualand.app.DuaViewModel
import com.dualand.app.R

@Composable
fun RepeatButton(duaViewModel: DuaViewModel) {
    val repeatCount = duaViewModel.repeatCount

    val iconRes = if (repeatCount == 0)
        R.drawable.repeat_off_btn
    else
        R.drawable.repeat_1_time_btn

    Box(modifier = Modifier.size(48.dp)) {
        IconButton(
            onClick = {
                val nextCount = when (repeatCount) {
                    0 -> 1
                    1 -> 2
                    2 -> 3
                    3 -> 4
                    4 -> 5
                    5 -> -1
                    else -> 0
                }
                duaViewModel.updateRepeatCount(nextCount)
            },
            modifier = Modifier.align(Alignment.Center)
        ) {
            Image(
                painter = painterResource(iconRes),
                contentDescription = "Repeat",
                modifier = Modifier.size(34.dp)
            )
        }

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
