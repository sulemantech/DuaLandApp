package com.dualand.app.components

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dualand.app.R

@Composable
fun DuaContentFooter(
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onFavoriteClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    navController: NavController,
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.dua_bottom_bg),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPreviousClick) {
                Image(
                    painter = painterResource(id = R.drawable.ic_backarrow),
                    contentDescription = "Previous",
                    modifier = Modifier.size(29.dp, 30.dp)
                )
            }
            IconButton(onClick = {
                navController.navigate("favorites?filterType=All")
            }) {
                Image(
                    painter = painterResource(id = R.drawable.favourite_icon_dua),
                    contentDescription = "Favorites Dua",
                    modifier = Modifier.size(33.dp, 40.dp)
                )
            }
            IconButton(onClick = {
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "Check out this amazing app: https://play.google.com/store/apps/details?id=${context.packageName}"
                    )
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, "Share App")
                context.startActivity(shareIntent)
            }) {
                Image(
                    painter = painterResource(id = R.drawable.share_icon),
                    contentDescription = "Share",
                    modifier = Modifier.size(33.dp, 40.dp)
                )
            }
            IconButton(onClick = onNextClick) {
                Image(
                    painter = painterResource(id = R.drawable.ic_nextarrow),
                    contentDescription = "Next",
                    modifier = Modifier.size(29.dp, 30.dp)
                )
            }
        }
    }
}
