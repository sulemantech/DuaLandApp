package com.dualand.app.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dualand.app.DuaViewModel
import com.dualand.app.R
import kotlinx.coroutines.launch

@Composable
fun FavouriteButton(
    duaNumber: String,
    textHeading: String?,
    imageResId: Int,
    viewModel: DuaViewModel
) {
    val scope = rememberCoroutineScope()
    var isFav by remember { mutableStateOf(false) }

//    LaunchedEffect(duaNumber) {
//        isFav = viewModel.isFavorite(duaNumber)
//    }

    IconButton(onClick = {
        scope.launch {
            if (textHeading != null) {
                viewModel.toggleFavorite(duaNumber, status = "")
            }
            isFav = !isFav
        }
    }) {
        Image(
            painter = painterResource(
                id = if (isFav) R.drawable.favourite_active_icon else R.drawable.favourite_icon
            ),
            contentDescription = null,
            modifier = Modifier.size(30.dp)
        )
    }
}
