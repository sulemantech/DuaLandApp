package com.dualand.app.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp

@Composable
fun TagButton(
    text: String,
    bgColor: Color? = null,
    backgroundDrawable: Int? = null,
    width: Dp? = null,
    height: Dp? = null,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .then(
                if (width != null && height != null) Modifier.size(width, height)
                else Modifier.wrapContentSize()
            )
            // .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .background(
                color = bgColor ?: Color.Transparent,
                // shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (backgroundDrawable != null) {
            Image(
                painter = painterResource(id = backgroundDrawable),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.matchParentSize()
            )
        }
    }
}
