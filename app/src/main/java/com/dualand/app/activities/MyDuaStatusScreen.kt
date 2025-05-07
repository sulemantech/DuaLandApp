package com.dualand.app.activities

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dualand.app.DuaViewModel
import com.dualand.app.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDuaStatusScreen(navController: NavController, innerPadding: PaddingValues,  viewModel: DuaViewModel) {

    val systemUiController = rememberSystemUiController()
    val NavigationBarColor = colorResource(id = R.color.top_nav_new)
    val statusBarColor = colorResource(id = R.color.top_nav_new)
    var searchText by remember { mutableStateOf("") }
    val context = LocalContext.current
    val title = FontFamily(Font(R.font.mochypop_regular))
    val MyArabicFont = FontFamily(Font(R.font.doodlestrickers))
    val favoriteDuas by viewModel.favoriteDuas.collectAsState()

   // val viewModel: DuaViewModel = viewModel

    SideEffect {
        systemUiController.setStatusBarColor(color = statusBarColor)
        systemUiController.setNavigationBarColor(color = NavigationBarColor)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(colorResource(R.color.white))
    ) {
        Image(
            painter = painterResource(id = R.drawable.top_bd),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
        )

        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier,
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.padding(start = 6.dp, top = 12.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.home_btn),
                            contentDescription = "Back",
                            modifier = Modifier.size(29.dp, 30.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp)
                    ) {
                        Text(
                            text = "My Dua Status",
                            fontSize = 14.sp,
                            color = colorResource(R.color.heading_color),
                            fontFamily = title,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }

                    IconButton(
                        onClick = { navController.navigate("SettingsScreen") },
                        modifier = Modifier.padding(end = 6.dp, top = 12.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.setting_btn),
                            contentDescription = "Settings",
                            modifier = Modifier.size(29.dp, 30.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(start = 16.dp, end = 16.dp),
                trailingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "Voice Search",
                        modifier = Modifier
                            .size(42.dp)
                            .clickable {
                            }
                    )
                },
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    containerColor = colorResource(R.color.top_nav_new)
                )
            )

            // Display list of favorite duas
            LazyColumn {
                items(favoriteDuas) { dua ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = dua.title,
                            fontSize = 16.sp,
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(
                            onClick = { viewModel.toggleFavorite(dua) }
                        ) {
                            val icon = if (dua.isFavorite) {
                                painterResource(id = R.drawable.favourite_active_icon)
                            } else {
                                painterResource(id = R.drawable.favourite_icon)
                            }
                            Image(
                                painter = icon,
                                contentDescription = "Favorite",
                                modifier = Modifier.size(33.dp)
                            )
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .height(50.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.dua_bottom_bg),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .matchParentSize()
                    .height(53.dp)
            )

            Box(
                modifier = Modifier,
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp, top = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { /* Handle previous action */ }) {
                        Image(
                            painter = painterResource(id = R.drawable.favourite_icon),
                            contentDescription = "Previous",
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

                    IconButton(onClick = { /* Handle next action */ }) {
                        Image(
                            painter = painterResource(id = R.drawable.info_icon),
                            contentDescription = "Next",
                            modifier = Modifier.size(33.dp, 40.dp)
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun MyduaStatusScreenPreview() {
    val viewModel = DuaViewModel(Application())
    MyDuaStatusScreen(
        navController = rememberNavController(),
        innerPadding = PaddingValues(),
        viewModel = viewModel
    )
}