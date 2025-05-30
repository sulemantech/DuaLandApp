package com.dualand.app.activities

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dualand.app.R
import androidx.compose.material3.Text
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import com.dualand.app.activities.DuaDataProvider.duaList
import com.dualand.app.components.DuaContent
import com.dualand.app.components.DuaContentFooter
import com.dualand.app.components.DuaTabs
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import android.app.Application
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dualand.app.DuaViewModel
import com.dualand.app.components.InfoDialogContent

@Composable
fun DuaNewScreen(
    navController: NavController,
    duaTitle: String = "",
    innerPadding: PaddingValues,
    duaViewModel: DuaViewModel = viewModel()
) {
    val systemUiController = rememberSystemUiController()
    val navigationBarColor = colorResource(id = R.color.top_nav_new)
    val statusBarColor = colorResource(id = R.color.top_nav_new)
    val context = LocalContext.current
    val title = FontFamily(Font(R.font.mochypop_regular))


    //val allDuas = duaViewModel.duaList

    SideEffect {
        systemUiController.setStatusBarColor(color = statusBarColor)
        systemUiController.setNavigationBarColor(color = navigationBarColor)
    }

    val currentDua = duaViewModel.currentDua
    val highlightedIndex = duaViewModel.highlightedIndex
    val currentIndex = duaViewModel.currentIndex

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(currentIndex) {
                detectHorizontalDragGestures { _, dragAmount ->
                    if (dragAmount > 50 && currentIndex > 0) {
                        duaViewModel.previousDua()
                    } else if (dragAmount < -50 && currentIndex < duaViewModel.duaKeys.lastIndex) {
                        duaViewModel.nextDua()
                    }
                }
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = colorResource(R.color.top_nav_new)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { duaViewModel.stopAudio()
                        navController.navigate("learn") },
                    modifier = Modifier.padding(start = 4.dp, top = 5.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.home_btn),
                        contentDescription = "Back",
                        modifier = Modifier.size(29.dp, 30.dp)
                    )
                }

                Row(modifier = Modifier.padding()) {
                    Text(
                        text = currentDua.firstOrNull()?.duaNumber ?: duaTitle,
                        fontSize = 14.sp,
                        color = colorResource(R.color.heading_color),
                        fontFamily = title,
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = currentDua.firstOrNull()?.textheading ?: duaTitle,
                        fontSize = 14.sp,
                        color = colorResource(R.color.heading_color),
                        fontFamily = title,
                        textAlign = TextAlign.Center
                    )
                }
                var showDialog by remember { mutableStateOf(false) }

                androidx.compose.material3.IconButton(
                    onClick = {
                        duaViewModel.stopAudio()
                        showDialog = true },
                    modifier = Modifier.padding(start = 6.dp, top = 12.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.info_icon),
                        contentDescription = "Info",
                        modifier = Modifier.size(29.dp, 30.dp)
                    )
                }
                if (showDialog) {
                    InfoDialogContent(onDismiss = { showDialog = false })
                }
            }

            Image(
                painter = painterResource(id = currentDua.firstOrNull()?.image ?: R.drawable.kaaba),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Box(modifier = Modifier.fillMaxWidth().padding()) {
                DuaTabs(
                    selectedTab = duaViewModel.selectedTab.collectAsState().value,
                    onTabSelected = { tab ->
                        duaViewModel.stopAudio()
                        duaViewModel.setSelectedTab(tab)
                    }
                )
            }

            DuaContent(
                duas = currentDua,
                innerPadding = PaddingValues(bottom = 50.dp),
                modifier = Modifier
                    //.weight(1f)
                    .fillMaxWidth(),
                highlightedIndex = highlightedIndex,
                duaViewModel
            )
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            DuaContentFooter(
                onStopAudio = { duaViewModel.stopAudio() },
                navController = navController,
                onPreviousClick = { duaViewModel.previousDua() },
                onNextClick = { duaViewModel.nextDua() }
            )
        }


    }
}
@Preview(showBackground = true)
@Composable
fun DuaNewScreenPreview() {
    DuaNewScreen(navController = rememberNavController(), innerPadding = PaddingValues())

}
