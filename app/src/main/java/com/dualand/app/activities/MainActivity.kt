package com.dualand.app.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.dualand.app.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.compose.foundation.lazy.grid.*
import kotlinx.coroutines.delay
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
<<<<<<< HEAD:app/src/main/java/com/example/animationdemo/activities/MainActivity.kt
=======
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.dualand.app.activities.DuaScreen
import com.dualand.app.activities.SettingsScreen
>>>>>>> 256f35f89a3385a9f879b796b1bc4eabe78831ee:app/src/main/java/com/dualand/app/activities/MainActivity.kt
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.systemuicontroller.rememberSystemUiController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigator(navController = rememberNavController())
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigator(navController: NavHostController) {
    AnimatedNavHost(
        navController = navController,
        startDestination = "splash",
        enterTransition = { fadeIn(animationSpec = tween(500)) },
        exitTransition = { fadeOut(animationSpec = tween(300)) }
    ) {
        composable("splash") {
            SplashScreen(
                onFinished = {
                    navController.navigate("learn") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }
        composable("learn") {
            MainScreen(navController)
        }
        composable("dua/{index}") { backStackEntry ->
            val index = backStackEntry.arguments?.getString("index")?.toIntOrNull() ?: 0
            DuaScreen(index = index, navController = navController, innerPadding = PaddingValues())
        }
        composable("home") {
            PlaceholderScreen(title = "Home Screen")
        }
        composable("favorites") {
            PlaceholderScreen(title = "Favorites Screen")
        }
        composable("profile") {
            PlaceholderScreen(title = "Profile Screen")
        }
        composable("SettingsScreen") {
            SettingsScreen(navController)
        }
    }
}

@Composable
fun PlaceholderScreen(title: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(text = title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val systemUiController = rememberSystemUiController()

    val NavigationBarColor = colorResource(id = R.color.top_nav_new)
    val statusBarColor = colorResource(id = R.color.top_nav_new)
    SideEffect {
        systemUiController.setStatusBarColor(color = statusBarColor)
        systemUiController.setNavigationBarColor(color = NavigationBarColor)
    }

    LaunchedEffect(true) {
        delay(2000)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_screen),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun MainScreen(navController: NavController) {
    Scaffold(
//        bottomBar = {
//            CustomBottomNavigationBar(
//                onHomeClick = { navController.navigate("home") },
//                onStarClick = { navController.navigate("favorites") },
//                onUserClick = { navController.navigate("profile") },
//                onSettingsClick = { navController.navigate("SettingsScreen") }
//            )
//        }
    ) { innerPadding ->
        LearnWithEaseScreen(navController, innerPadding)
    }
}


data class DuaItem(
    val imageRes: Int,
    val title: String,
    val onClick: () -> Unit
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnWithEaseScreen(navController: NavController, innerPadding: PaddingValues) {

    val systemUiController = rememberSystemUiController()
    val NavigationBarColor = colorResource(id = R.color.top_nav_new)
    val statusBarColor = colorResource(id = R.color.top_nav_new)
    var searchText by remember { mutableStateOf("") }

    SideEffect {
        systemUiController.setStatusBarColor(color = statusBarColor)
        systemUiController.setNavigationBarColor(color = NavigationBarColor)
    }

    val MyArabicFont = FontFamily(Font(R.font.doodlestrickers))

    val duaList = (0 until 21).map { index ->
        val drawableName = if (index == 0) "card" else "card${index + 1}"
        val resId = remember(drawableName) {
            val res = R.drawable::class.java.getDeclaredField(drawableName).getInt(null)
            res
        }
        DuaItem(
            imageRes = resId,
            title = "Dua ${index + 1}",
            onClick = {
                navController.navigate("dua/$index")
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(colorResource(R.color.dashboard_color))
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
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .height(50.dp)
        )

        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier,
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp, top = 14.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = { navController.navigate("SettingsScreen") },
                        modifier = Modifier
                            .size(width = 60.dp, height = 50.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.setting_btn),
                            contentDescription = "Settings",
                            modifier = Modifier.size(39.dp, 40.dp)
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
                    .padding(start = 16.dp, end = 16.dp),
                placeholder = { Text("Search Duas...") },
                trailingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "Voice Search",
                        modifier = Modifier
                            .size(34.dp)
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
            val filteredDuaList = duaList.filter {
                it.title.contains(searchText, ignoreCase = true)
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp, top = 10.dp),
                contentPadding = PaddingValues(bottom = 50.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item(span = { GridItemSpan(2) }) {
                }

                items(filteredDuaList) { dua ->
                    DuaCard(imageRes = dua.imageRes, onClick = dua.onClick)
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
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {

                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.favourite_icon),
                            contentDescription = "Previous",
                            modifier = Modifier.size(29.dp, 40.dp)
                        )
                    }

                    IconButton(onClick = {

                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.share_icon),
                            contentDescription = "Previous",
                            modifier = Modifier.size(29.dp, 40.dp)
                        )
                    }

                    IconButton(onClick = {
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.info_icon),
                            contentDescription = "Next",
                            modifier = Modifier.size(29.dp, 40.dp)
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun DuaCard(
    imageRes: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                indication = rememberRipple(bounded = true),
                interactionSource = remember { MutableInteractionSource() }
            ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Dua card image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

    }
}

@Preview(showBackground = true)
@Composable
fun LearnWithEaseScreenPreview() {
    LearnWithEaseScreen(
        navController = rememberNavController(),
        innerPadding = PaddingValues()
    )
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(onFinished = {})
}