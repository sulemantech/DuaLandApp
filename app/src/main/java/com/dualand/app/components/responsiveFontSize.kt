package com.dualand.app.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp


@Composable
fun responsiveFontSize(): TextUnit {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    return when {
        screenWidthDp >= 720 -> 18.sp // tablets
        screenWidthDp >= 600 -> 16.sp // large phones or small tablets
        else -> 13.sp // normal phones
    }
}
