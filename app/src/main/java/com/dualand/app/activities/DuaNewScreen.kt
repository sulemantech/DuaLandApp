package com.dualand.app.activities

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
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
import androidx.compose.material3.Text // keep this one (preferred)
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import com.dualand.app.activities.DuaDataProvider.duaList
import com.dualand.app.components.DuaContent
import com.dualand.app.components.DuaContentFooter
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun DuaNewScreen(
    navController: NavController,
    duaTitle: String = "",
    innerPadding: PaddingValues,
    kaabaImage: Painter = painterResource(R.drawable.kaaba),
) {
    val systemUiController = rememberSystemUiController()
    val navigationBarColor = colorResource(id = R.color.top_nav_new)
    val statusBarColor = colorResource(id = R.color.top_nav_new)

    SideEffect {
        systemUiController.setStatusBarColor(color = statusBarColor)
        systemUiController.setNavigationBarColor(color = navigationBarColor)
    }

    val groupedAndSortedDuas = duaList.groupBy {
        it.duaNumber.substringBefore(".").trim().toInt()
    }.toSortedMap()

    val duaKeys = groupedAndSortedDuas.keys.toList()
    var currentIndex by remember { mutableStateOf(0) }
    val currentDua = groupedAndSortedDuas[duaKeys.getOrNull(currentIndex)] ?: emptyList()

    Log.d("currentIndex", "$currentIndex, currentDua: $currentDua")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(currentIndex) {
                detectHorizontalDragGestures { _, dragAmount ->
                    if (dragAmount > 50 && currentIndex > 0) {
                        currentIndex--
                    } else if (dragAmount < -50 && currentIndex < duaKeys.lastIndex) {
                        currentIndex++
                    }
                }
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
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
                Row(
                    modifier = Modifier.padding(horizontal = 6.dp)
                ) {
                    Text(
                        text = currentDua.firstOrNull()?.duaNumber ?: duaTitle,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Text(
                        text = currentDua.firstOrNull()?.textheading ?: duaTitle,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }
                IconButton(
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.tab_bg_pink),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                )
                TabSwitcher()
            }

            DuaContent(
                duas = currentDua,
                innerPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
        }
        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            DuaContentFooter(
                onPreviousClick = { if (currentIndex > 0) currentIndex-- },
                onNextClick = { if (currentIndex < duaKeys.lastIndex) currentIndex++ }
            )
        }
    }
}

@Composable
fun TabSwitcher() {
    var selectedTab by remember { mutableStateOf("word") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 10.dp, start = 5.dp, end = 5.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .height(45.dp)
                .weight(1f)
                .padding(top = 2.dp, bottom = 2.dp)
                .clickable { selectedTab = "word" }
        ) {
            Image(
                painter = painterResource(R.drawable.rectangle_tab),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.matchParentSize()
            )
            Text(
                text = "Word By Word",
                color = if (selectedTab == "word") Color.White else Color.Black,
                modifier = Modifier.align(Alignment.Center),
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(16.dp))
        Box(
            modifier = Modifier
                .height(40.dp)
                .weight(1f)
                .clickable { selectedTab = "complete" }
        ) {
            Image(
                painter = painterResource(R.drawable.rectangle_tab),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.matchParentSize()
            )
            Text(
                text = "Complete Dua",
                color = if (selectedTab == "complete") Color.White else Color.Black,
                modifier = Modifier.align(Alignment.Center),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DuaNewScreenPreview() {
    DuaNewScreen(navController = rememberNavController(), innerPadding = PaddingValues(),)

}
