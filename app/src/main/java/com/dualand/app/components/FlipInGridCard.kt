package com.dualand.app.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.delay

@Composable
fun FlipInGridCard(index: Int, content: @Composable () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    val rotationY by animateFloatAsState(targetValue = if (visible) 0f else 90f)

    LaunchedEffect(Unit) {
        delay(index * 5L)
        visible = true  // mutable var - allowed
    }

    Box(
        modifier = Modifier
            .graphicsLayer {
                this.rotationY = rotationY  // mutable property of graphicsLayer - allowed
                cameraDistance = 8 * density
            }
    ) {
        if (rotationY <= 90f) {
            content()
        }
    }
}

@Composable
fun BouncyGridCard(index: Int, content: @Composable () -> Unit) {
    var visible by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "gridBounce"
    )

    LaunchedEffect(Unit) {
        delay(index * 5L)
        visible = true
    }

    Box(
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
    ) {
        content()
    }
}
