package com.dualand.app.activities

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dualand.app.R
import com.google.android.gms.common.util.DeviceProperties.isTablet

@Composable
fun DuaNewScreen(
    navController: NavController,
    duaTitle: String = "1. Praise and Glory",
    kaabaImage: Painter = painterResource(R.drawable.kaaba),
    onBackClick: () -> Unit = {},
    onCameraClick: () -> Unit = {},
) {
    Column(modifier = Modifier.fillMaxSize().background(color = colorResource(R.color.white))) {

        // Top Bar)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorResource(R.color.top_nav_new)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.navigate("learn") },
                modifier = Modifier.padding(start = 4.dp, top = 5.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.home_btn),
                    contentDescription = "Back",
                    modifier = Modifier.size(29.dp, 30.dp)
                )
            }
            Text(
                duaTitle,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            IconButton(onClick = {navController.navigate("InfoScreen")},
                modifier = Modifier.padding(start = 4.dp, top = 5.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.info_icon),
                    contentDescription = "Info",
                    modifier = Modifier.size(29.dp, 30.dp)
                )
            }
        }

        // Kaaba Image
        Image(
            painter = painterResource(R.drawable.kaaba),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxWidth()
        )

        // Word by Word / Complete Dua Toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { /* TODO */ }, colors = ButtonDefaults.buttonColors(Color(0xFF00BCD4))) {
                Text("WORD BY WORD", color = Color.White)
            }
            Button(onClick = { /* TODO */ }, colors = ButtonDefaults.buttonColors(Color(0xFF00BCD4))) {
                Text("COMPLETE DUA", color = Color.White)
            }
        }

        // Dua Entries
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(2) { index ->
                DuaCard(
                    arabic = "سُبْحَانَ اللهِ وَبِحَمْدِهِ، سُبْحَانَ اللهِ الْعَظِيمِ",
                    english = "Glory be to Allah and all praise be to Him; Glory be to Allah, the Most Great.",
                    urdu = "اللہ پاک ہے اور اس کی تعریف ہے، اللہ پاک ہے، وہ عظمت والا ہے۔"
                )
            }
        }

        // Bottom Navigation
        BottomNavigation(backgroundColor = Color(0xFFFFF9C4)) {
            listOf(R.drawable.home_btn, R.drawable.setting_btn, R.drawable.favourite_icon, R.drawable.share_icon, R.drawable.icon_dua_setting)
                .forEach {
                    BottomNavigationItem(
                        icon = { Icon(painterResource(id = it), contentDescription = null) },
                        selected = false,
                        onClick = {}
                    )
                }
        }
    }
}

@Composable
fun DuaCard(arabic: String, english: String, urdu: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            // Audio Buttons
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { /* Play full */ }) {
                    Icon(painterResource(R.drawable.vector_play), contentDescription = "Play")
                }
                IconButton(onClick = { /* Play word by word */ }) {
                    Icon(painterResource(R.drawable.icon_playy), contentDescription = "Word by Word")
                }
                IconButton(onClick = { /* Stop */ }) {
                    Icon(painterResource(R.drawable.pause_icon), contentDescription = "Stop")
                }
            }

            // Arabic
            Text(
                text = arabic,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1E88E5),
                modifier = Modifier.padding(top = 12.dp),
                textAlign = TextAlign.Center
            )

            // English
            Text(
                text = english,
                fontSize = 14.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(top = 8.dp),
                textAlign = TextAlign.Center
            )

            // Urdu
            Text(
                text = urdu,
                fontSize = 14.sp,
                color = Color.Black,
                fontFamily = FontFamily.Default,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun DuaNewScreenPreview() {
    DuaNewScreen(
        navController = rememberNavController(),

    )
}
