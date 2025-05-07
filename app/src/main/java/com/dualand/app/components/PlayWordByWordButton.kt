package com.dualand.app.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dualand.app.R


@Composable
fun PlayWordByWordButton(
    isPlaying: Boolean,
    showListening: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier,
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(width = 60.dp, height = 50.dp)
        ) {
            val iconSize = 47.dp
            val iconRes = when {
                showListening -> R.drawable.icon_listening
                isPlaying -> R.drawable.pause_icon
                else -> R.drawable.icon_playy
            }

            Image(
                painter = painterResource(id = iconRes),
                contentDescription = "Play/Pause/Listening",
                modifier = Modifier.size(iconSize)
            )
        }
    }
}