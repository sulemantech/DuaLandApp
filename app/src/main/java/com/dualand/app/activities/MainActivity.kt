package com.dualand.app.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import com.dualand.app.R

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
        enterTransition = { fadeIn(animationSpec = tween(700)) },
        exitTransition = { fadeOut(animationSpec = tween(500)) }

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
        composable(
            route = "SettingsScreen",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
                ) + fadeIn(animationSpec = tween(durationMillis = 1000))
            },
//            exitTransition = {
//                slideOutHorizontally(
//                    targetOffsetX = { fullWidth -> -fullWidth },
//                    animationSpec = tween(durationMillis = 1000, easing = FastOutLinearInEasing)
//                ) + fadeOut(animationSpec = tween(durationMillis = 1000))
//            }
        ) {
            SettingsScreen(navController = navController, innerPadding = PaddingValues())
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
    val context = LocalContext.current

    SideEffect {
        systemUiController.setStatusBarColor(color = statusBarColor)
        systemUiController.setNavigationBarColor(color = NavigationBarColor)
    }

    val MyArabicFont = FontFamily(Font(R.font.doodlestrickers))

    val duaCardMappings = listOf(
        listOf(0, 1),
        listOf(2, 3),           // card 2
        listOf(4),              // card 3
        listOf(5),              // card 4
        listOf(6, 7),           // card 5
        listOf(8),              // card 6
        listOf(9),              // card 7
        listOf(10),             // card 8
        listOf(11),             // card 9
        listOf(12),             // card 10
        listOf(13),             // card 11
        listOf(14),             // card 12
        listOf(15, 16),         // card 13
        listOf(17),
        listOf(18),             // card 14
        listOf(19, 20,21),     // card 15
        listOf(22, 23),         // card 16
        listOf(24),             // card 18
        listOf(25),             // card 19
        listOf(26),             // card 20
        listOf(27),         // card 21
        listOf(28,29),             // card 22
        listOf(30),         // card 23
        listOf(31, 32),         // card 23
        listOf(33),             // card 24
        listOf(34),             // card 25
        listOf(35),             // card 26
        listOf(36, 37, 38) ,     // card 27
        listOf(39) ,     // card 27
        listOf(40),      // card 27
        listOf(41)  ,    // card 27
        listOf(42)  ,    // card 27
         listOf(43)  ,    // card 27
    )

    val duaList = duaCardMappings.mapIndexed { cardIndex, indexGroup ->
        val drawableName = if (cardIndex == 0) "card" else "card${cardIndex + 1}"
        val resId = remember(drawableName) {
            R.drawable::class.java.getDeclaredField(drawableName).getInt(null)
        }

        DuaItem(
            imageRes = resId,
            title = "Dua ${cardIndex + 1}",
            onClick = {
                navController.navigate("dua/${indexGroup.first()}")
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
                    .height(48.dp)
                    .padding(start = 16.dp, end = 16.dp),
               // placeholder = { Text("Search Duas...", fontSize = 14.sp ) },
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
            val filteredDuaList = remember(searchText) {
                duaList.filter {
                    it.title.contains(searchText, ignoreCase = true)
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 45.dp),
                contentPadding = PaddingValues(bottom = 50.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(filteredDuaList) { index, dua ->
                    BouncyGridCard(index = index) {
                        DuaCard(imageRes = dua.imageRes, onClick = dua.onClick)
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
                    IconButton(onClick = {

                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.favourite_icon),
                            contentDescription = "Previous",
                            modifier = Modifier.size(33.dp, 40.dp)
                        )
                    }

                    IconButton(onClick = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "Check out this amazing app: https://play.google.com/store/apps/details?id=${context.packageName}")
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


                    IconButton(onClick = {
                    }) {
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

@Composable
fun DuaCard(
    imageRes: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                onClick = onClick,
                indication = rememberRipple(bounded = true),
                interactionSource = remember { MutableInteractionSource() }
            ),
        shape = RoundedCornerShape(6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Dua card image",
            contentScale = ContentScale.Fit,
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