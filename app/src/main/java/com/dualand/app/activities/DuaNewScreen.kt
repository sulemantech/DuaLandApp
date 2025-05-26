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
import com.dualand.app.DuaViewModel

@Composable
fun DuaNewScreen(
    navController: NavController,
    duaTitle: String = "",
    innerPadding: PaddingValues
) {
    val systemUiController = rememberSystemUiController()
    val navigationBarColor = colorResource(id = R.color.top_nav_new)
    val statusBarColor = colorResource(id = R.color.top_nav_new)
    val context = LocalContext.current

    val viewModel: DuaViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(DuaViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return DuaViewModel(context.applicationContext as Application) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    )

    SideEffect {
        systemUiController.setStatusBarColor(color = statusBarColor)
        systemUiController.setNavigationBarColor(color = navigationBarColor)
    }

    val currentDua = viewModel.currentDua
    val highlightedIndex = viewModel.highlightedIndex
    val currentIndex = viewModel.currentIndex

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(currentIndex) {
                detectHorizontalDragGestures { _, dragAmount ->
                    if (dragAmount > 50 && currentIndex > 0) {
                        viewModel.previousDua()
                    } else if (dragAmount < -50 && currentIndex < viewModel.duaKeys.lastIndex) {
                        viewModel.nextDua()
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
                    onClick = { navController.navigate("learn") },
                    modifier = Modifier.padding(start = 4.dp, top = 5.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.home_btn),
                        contentDescription = "Back",
                        modifier = Modifier.size(29.dp, 30.dp)
                    )
                }

                Row(modifier = Modifier.padding(horizontal = 6.dp)) {
                    Text(
                        text = currentDua.firstOrNull()?.duaNumber ?: duaTitle,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = currentDua.firstOrNull()?.textheading ?: duaTitle,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }
                androidx.compose.material3.IconButton(
                    onClick = { navController.navigate("InfoScreen") },
                    modifier = Modifier.padding(start = 4.dp, top = 5.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.info_icon),
                        contentDescription = "Info",
                        modifier = Modifier.size(29.dp, 30.dp)
                    )
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

            Box(modifier = Modifier.fillMaxWidth()) {
                DuaTabs(
                    selectedTab = viewModel.selectedTab.collectAsState().value,
                    onTabSelected = { tab ->
                        viewModel.setSelectedTab(tab)
                    },
                    dua = duaList
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            DuaContent(
                duas = currentDua,
                innerPadding = PaddingValues(),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                highlightedIndex = highlightedIndex,
                viewModel
            )
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            DuaContentFooter(
                onPreviousClick = { viewModel.previousDua() },
                onNextClick = { viewModel.nextDua() }
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun DuaNewScreenPreview() {
    DuaNewScreen(navController = rememberNavController(), innerPadding = PaddingValues())

}
