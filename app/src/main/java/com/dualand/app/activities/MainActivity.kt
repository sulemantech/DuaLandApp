package com.dualand.app.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.dualand.app.R
import com.dualand.app.models.Dua
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_AnimationDemo)
        setContent {
            AppNavigator(navController = rememberNavController())
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigator(navController: NavHostController) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                Log.d("AppNavigator", "App is in background, stopping audio...")
                MediaPlayerManager.stopAudio()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

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
            DuaScreen(
                index = index,
                navController = navController,
                innerPadding = PaddingValues(),
                stopAudioPlayback = {}
            )
        }
        composable("home") {
            PlaceholderScreen(title = "Home Screen")
        }
        composable("favorites?filterType={filterType}") { backStackEntry ->
            val filterType = backStackEntry.arguments?.getString("filterType") ?: "All"
            MyDuaStatusScreen(
                navController = navController,
                innerPadding = PaddingValues(),
                initialFilter = filterType
            )
        }
        composable("profile") {
            PlaceholderScreen(title = "Profile Screen")
        }
        composable("SettingsScreen") {
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

    val NavigationBarColor = colorResource(id = R.color.splash_color)
    val statusBarColor = colorResource(id = R.color.splash_color)

    SideEffect {
        systemUiController.setStatusBarColor(color = statusBarColor)
        systemUiController.setNavigationBarColor(color = NavigationBarColor)
    }

    LaunchedEffect(true) {
        delay(2000)
        onFinished()
    }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash),
            contentDescription = "Splash Screen",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        Image(
            painter = painterResource(id = R.drawable.splash_logo),
            contentDescription = "App Logo",
            modifier = Modifier
                .align(Alignment.Center)
                .size(280.dp)
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
    val textheading: String,
    val onClick: () -> Unit
) {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnWithEaseScreen(navController: NavController, innerPadding: PaddingValues) {
    val systemUiController = rememberSystemUiController()
    val statusBarColor = colorResource(id = R.color.top_nav_new)
    val navBarColor = colorResource(id = R.color.top_nav_new)
    val context = LocalContext.current
    val text_font = FontFamily(Font(R.font.montserrat_regular))


    SideEffect {
        systemUiController.setStatusBarColor(statusBarColor)
        systemUiController.setNavigationBarColor(navBarColor)
    }

    var searchText by remember { mutableStateOf("") }
    var showSearchBar by remember { mutableStateOf(false) }
    val duaCardMappings = listOf(
        listOf(0, 1), listOf(2, 3), listOf(4), listOf(5), listOf(6, 7), listOf(8),
        listOf(9), listOf(10), listOf(11), listOf(12), listOf(13),
        listOf(14), listOf(15,16), listOf(17), listOf(18,19,20), listOf(21,22), listOf( 23),
        listOf(24), listOf(25), listOf(26), listOf(27,28), listOf(29), listOf(30,31),
        listOf(32), listOf(33), listOf(34), listOf(35,36,37), listOf(38),
        listOf(39), listOf(40), listOf(41), listOf(42)
    )

    val allDuas: List<Dua> = DuaDataProvider.duaList

    val duaList = duaCardMappings.mapIndexed { cardIndex, indexGroup ->
        val firstIndex = indexGroup.first()
        val dua = allDuas.getOrNull(firstIndex)

        val drawableName = if (cardIndex == 0) "card" else "card${cardIndex + 1}"
        val resId = remember(drawableName) {
            R.drawable::class.java.getDeclaredField(drawableName).getInt(null)
        }

        DuaItem(
            imageRes = resId,
            title = dua?.textheading ?: "Dua ${cardIndex + 1}", 
            textheading = dua?.textheading ?: "",
            onClick = {
                navController.navigate("dua/$firstIndex")
            }
        )
    }

    val filteredDuaList = remember(searchText) {
        duaList.filter {
            it.textheading.contains(searchText, ignoreCase = true)
        }
    }

    val suggestionList = remember(searchText) {
        if (searchText.isBlank()) emptyList()
        else allDuas.filter { dua ->
            dua.textheading?.contains(searchText, ignoreCase = true) == true
        }
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 5.dp, top = 20.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = { showSearchBar = !showSearchBar },
                    modifier = Modifier.size(width = 60.dp, height = 50.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.setting_btn),
                        contentDescription = "Search",
                        modifier = Modifier.size(width = 29.dp, height = 30.dp)
                    )
            }
//                IconButton(onClick = { navController.navigate("SettingsScreen") }) {
//                    Image(
//                        painter = painterResource(id = R.drawable.setting_icon_home),
//                        contentDescription = "Settings",
//                        modifier = Modifier.size(22.dp)
//                    )
//                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (showSearchBar) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.White, Color.LightGray)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.top_bd),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.matchParentSize()
                    )

                    Column {
                        TextField(
                            value = searchText,
                            onValueChange = { searchText = it },
                            placeholder = { Text("Search Dua...",fontFamily = text_font) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Transparent),
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                                imeAction = ImeAction.Search
                            ),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    showSearchBar = false
                                }
                            ),
                            singleLine = true,
                            trailingIcon = {
                                if (searchText.isNotEmpty()) {
                                    IconButton(onClick = { searchText = "" }) {
                                        Image(
                                            painter = painterResource(id = R.drawable.close_btn),
                                            contentDescription = "Search",
                                            modifier = Modifier.size(width = 29.dp, height = 30.dp)
                                        )
                                    }
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 50.dp),
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
                modifier = Modifier.matchParentSize()
            )

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, top = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(onClick = {
                    navController.navigate("favorites?filterType=Favorite")
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.favourite_icon),
                        contentDescription = "Favorites From menu",
                        modifier = Modifier.size(33.dp, 40.dp)
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

                IconButton(onClick = {
                    // TODO: Add info screen logic
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.info_icon),
                        contentDescription = "Info",
                        modifier = Modifier.size(33.dp, 40.dp)
                    )
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
       // elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent // <-- transparent background
        )
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