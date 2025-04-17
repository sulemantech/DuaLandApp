package com.example.animationdemo.activities

import android.media.MediaPlayer
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
import com.example.animationdemo.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.foundation.lazy.grid.*
import kotlinx.coroutines.delay
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
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
            DuaScreen(index = index, navController = navController, innerPadding =  PaddingValues())
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
    val backgroundColor = colorResource(id = R.color.top_nav_new)


    SideEffect {
        systemUiController.setStatusBarColor(color = backgroundColor)
        systemUiController.setNavigationBarColor(color = backgroundColor)
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
        bottomBar = {
            CustomBottomNavigationBar(
                onHomeClick = { navController.navigate("home") },
                onStarClick = { navController.navigate("favorites") },
                onUserClick = { navController.navigate("profile") },
                onSettingsClick = { navController.navigate("SettingsScreen") }
            )
        }
    ) { innerPadding ->
        LearnWithEaseScreen(navController, innerPadding)
    }
}


data class DuaItem(
    val imageRes: Int,
    val onClick: () -> Unit
)

@Composable
fun LearnWithEaseScreen(navController: NavController, innerPadding: PaddingValues) {
    val systemUiController = rememberSystemUiController()
    val backgroundColor = colorResource(id = R.color.splash_bg)

    val MyArabicFont = FontFamily(Font(R.font.doodlestrickers))

    SideEffect {
        systemUiController.setStatusBarColor(color = backgroundColor)
        systemUiController.setNavigationBarColor(color = backgroundColor)
    }

    val duaList = (0 until 21).map { index ->
        val drawableName = if (index == 0) "card" else "card${index + 1}"
        val resId = remember(drawableName) {
            val res = R.drawable::class.java.getDeclaredField(drawableName).getInt(null)
            res
        }
        DuaItem(
            imageRes = resId,
            onClick = { navController.navigate("dua/$index") }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        Image(
            painter = painterResource(id = R.drawable.dashboard_new),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(modifier = Modifier.fillMaxSize()) {
            CustomTopBar(
                onMenuClick = { },
                onRightIconClick = { }
            )

            Spacer(modifier = Modifier.height(50.dp))

            Box(
                modifier = Modifier
                    .width(320.dp)
                    .height(70.dp)

            ) {
                Image(
                    painter = painterResource(id = R.drawable.top_bd),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.matchParentSize()
                )

                Text(
                    text = "BELIEVER’S SUPERPOWER",
                    fontSize = 15.sp,
                    fontFamily = MyArabicFont,
                    color = colorResource(R.color.doodlecolor),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }


            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp, top = 30.dp),
                contentPadding = PaddingValues(bottom = 50.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item(span = { GridItemSpan(2) }) {
                }

                items(duaList) { dua ->
                    DuaCard(imageRes = dua.imageRes, onClick = dua.onClick)
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
@Composable
fun CustomBottomNavigationBar(
    onHomeClick: () -> Unit,
    onStarClick: () -> Unit,
    onUserClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(68.dp)
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.bottom_dash),
            contentDescription = "Bottom Nav Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(0.5.dp, Alignment.CenterHorizontally) // very tight
        ) {
            NavIcon(R.drawable.info_btn, onClick = onHomeClick, size = 30.dp)
            NavIcon(R.drawable.share_btn, onClick = onStarClick, size = 30.dp)
            NavIcon(R.drawable.btn_star, onClick = onUserClick, size = 30.dp)
        }
    }
}



@Composable
fun NavIcon(@DrawableRes iconRes: Int, onClick: () -> Unit, size: Dp = 30.dp) {
    Box(
        modifier = Modifier
            .size(68.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(41.dp),
        )
    }
}

@Composable
fun CustomTopBar(
    onMenuClick: () -> Unit,
    onRightIconClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding( end = 20.dp)
    ) {
        IconButton(
            onClick = onRightIconClick,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(38.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.setting_btn),
                contentDescription = "Settings",
                modifier = Modifier.size(27.dp)
            )
        }
    }
}


@Composable
fun DuaLandDashboardScreen(navController: NavController, onRightIconClick: () -> Unit) {
    val systemUiController = rememberSystemUiController()
    val backgroundColor = colorResource(id = R.color.splash_bg)

    SideEffect {
        systemUiController.setStatusBarColor(color = backgroundColor)
        systemUiController.setNavigationBarColor(color = backgroundColor)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.dashboard_bg), // Your 2nd image
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, end = 8.dp)
        ) {
            IconButton(
                onClick = onRightIconClick,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(40.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_setting),
                    contentDescription = "Settings",
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {

            }

            Spacer(modifier = Modifier.height(124.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(6) { index ->
                    val duaTitles = listOf(
                        "Praise and Glory",
                        "Peace and Blessing upon the Prophet Muhammad",
                        "Du’a of Morning",
                        "Du’a of Evening",
                        "Before Sleeping",
                        "After Waking Up"
                    )

                    val duaImages = listOf(
                        R.drawable.kaaba,
                        R.drawable.kaaba,
                        R.drawable.kaaba,
                        R.drawable.kaaba,
                        R.drawable.kaaba,
                        R.drawable.kaaba,

                        )

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clickable {

                            },
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier

                        ) {
                            Image(
                                painter = painterResource(id = duaImages[index]),
                                contentDescription = duaTitles[index],
                                modifier = Modifier
                                    .height(170.dp)
                            )
                            Spacer(modifier = Modifier.height(18.dp))
                            Text(
                                text = duaTitles[index],
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                fontSize = 14.sp,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                val icons = listOf(
                    R.drawable.setting,
                    R.drawable.icon_home,
                    R.drawable.icon_playy
                )

                icons.forEachIndexed { index, iconRes ->
                    IconButton(onClick = { /* Navigate */ }) {
                        Image(
                            painter = painterResource(id = iconRes),
                            contentDescription = null,
                            modifier = Modifier.size(44.dp)
                        )
                    }

                    if (index < icons.lastIndex) {
                    }
                }
            }

        }
    }
}


@Composable
fun bottomDuaTabs(
    onWordByWordClick: () -> Unit = {},
    onCompleteDuaClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.bottom_dash),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(-4.dp, Alignment.CenterHorizontally)
        ) {

            IconButton(onClick = {
            }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_pause),
                    contentDescription = "Play Full Audio",
                    modifier = Modifier.size(33.dp)
                )
            }

            IconButton(onClick = {
            }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_pause),
                    contentDescription = "Play Full Audio",
                    modifier = Modifier.size(33.dp)
                )
            }

            IconButton(onClick = {
            }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_stop_btn_pink),
                    contentDescription = "Stop",
                    modifier = Modifier.size(33.dp)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DuaLandDashboardScreenPreview() {
    DuaLandDashboardScreen(navController = rememberNavController(), onRightIconClick = {})
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