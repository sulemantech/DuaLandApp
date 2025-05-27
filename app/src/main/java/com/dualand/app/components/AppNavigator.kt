package com.dualand.app.components

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.dualand.app.DuaViewModel
import com.dualand.app.activities.DuaNewScreen
import com.dualand.app.activities.DuaScreen
import com.dualand.app.activities.InfoScreen
import com.dualand.app.activities.MainScreen
import com.dualand.app.activities.MediaPlayerManager
import com.dualand.app.activities.MyDuaStatusScreen
import com.dualand.app.activities.PlaceholderScreen
import com.dualand.app.activities.SettingsScreen
import com.dualand.app.activities.SplashScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigator(navController: NavHostController,  duaViewModel: DuaViewModel) {
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
    ) {
        composable(
            "splash",
            enterTransition = { fadeIn(animationSpec = tween(700)) },
            exitTransition = { fadeOut(animationSpec = tween(500)) }
        ) {
            SplashScreen(
                onFinished = {
                    navController.navigate("learn") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable(
            "learn",
            enterTransition = { fadeIn(animationSpec = tween(500)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            MainScreen(navController, duaViewModel)
        }

        composable(
            "dua/{index}",
            enterTransition = { slideInHorizontally { it } },
            exitTransition = { slideOutHorizontally { -it } },
            popEnterTransition = { slideInHorizontally { -it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) { backStackEntry ->
            val index = backStackEntry.arguments?.getString("index")?.toIntOrNull() ?: 0
            DuaScreen(
                index = index,
                navController = navController,
                innerPadding = PaddingValues(),
                stopAudioPlayback = {},
                duaViewModel = duaViewModel
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

        composable("InfoScreen") {
            InfoScreen(navController = navController, innerPadding = PaddingValues())
        }
        composable("DuaNewScreen/{index}",
            enterTransition = { slideInHorizontally { it } },
            exitTransition = { slideOutHorizontally { -it } },
            popEnterTransition = { slideInHorizontally { -it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) { _ ->
            DuaNewScreen(navController = navController, innerPadding = PaddingValues(), duaViewModel = duaViewModel)
        }

        composable(
            "SettingsScreen",
            enterTransition = { slideInHorizontally { it } },
            exitTransition = { slideOutHorizontally { -it } },
            popEnterTransition = { slideInHorizontally { -it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) {
            SettingsScreen(
                navController = navController,
                innerPadding = PaddingValues(),duaViewModel
            )
        }
    }
}

