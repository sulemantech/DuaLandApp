package com.dualand.app.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dualand.app.DuaViewModel
import com.dualand.app.R
import com.dualand.app.activities.DuaDataProvider.duaList
import com.dualand.app.components.FilterDropdownMenu
import com.dualand.app.components.InfoDialogContent
import com.dualand.app.components.TagButton
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDuaStatusScreen(navController: NavController, innerPadding: PaddingValues, initialFilter: String = "All",  duaViewModel: DuaViewModel = viewModel()) {
    val viewModel: DuaViewModel = viewModel()
    val systemUiController = rememberSystemUiController()
    val NavigationBarColor = colorResource(id = R.color.top_nav_new)
    val statusBarColor = colorResource(id = R.color.top_nav_new)
    var searchText by remember { mutableStateOf("") }
    val context = LocalContext.current
    val title = FontFamily(Font(R.font.mochypop_regular))
    val MyArabicFont = FontFamily(Font(R.font.doodlestrickers))
    val duaStatuses by viewModel.allDuas.collectAsState()
//    val allDuas = duaList.filter { !it.textheading.isNullOrBlank() }
    val allDuas = duaList
        .filter { !it.textheading.isNullOrBlank() }
        .distinctBy { it.textheading!!.trim().lowercase() }
    var showDialog by remember { mutableStateOf(false) }
    var selectedFilters by remember { mutableStateOf(listOf<String>()) }
    var expanded by remember { mutableStateOf(false) }
    //  var selectedFilter by remember { mutableStateOf("All") }
    var index by remember { mutableStateOf(0) }
    val text_font = FontFamily(Font(R.font.montserrat_regular))
    var selectedFilter by remember { mutableStateOf(initialFilter) }

    val sharedPref = context.getSharedPreferences("your_pref_name", Context.MODE_PRIVATE)
    val rewardsEnabled = sharedPref.getBoolean("rewards_enabled", true)
    var currentIndex by remember { mutableStateOf(0) }
    var showRewardAnimation by remember { mutableStateOf(false) }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1,
        isPlaying = showRewardAnimation
    )

    LaunchedEffect(progress) {
        if (progress >= 0.99f) {
            showRewardAnimation = false
        }
    }


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = { showRewardAnimation = true }) {
            Text("Show Animation")
        }
        if (showRewardAnimation) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
                    .zIndex(2f),
                contentAlignment = Alignment.Center
            ) {
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.size(250.dp)
                )
            }
        }
    }
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
                        onClick = { navController.navigate("learn") },
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
                            text = if (selectedFilter == "Favorite") "My Favorite Dua" else "My Dua Status",
                            fontSize = 14.sp,
                            color = colorResource(R.color.heading_color),
                            fontFamily = title,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                    var showDialog by remember { mutableStateOf(false) }

                    IconButton(
                        onClick = { showDialog = true },
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
            }

            Spacer(modifier = Modifier.height(30.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (selectedFilter == "Favorite") {
                    Button(
                        onClick = { selectedFilter = "Favorite" },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.highlited_color)
                        )
                    ) {
                        Text(text = "All Duas", color = Color.White)
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Filter By",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = colorResource(R.color.heading_color),
                        fontFamily = text_font
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Box {
                        Image(
                            painter = painterResource(R.drawable.filter_icon),
                            contentDescription = "Filter Icon",
                            modifier = Modifier
                                .size(24.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { expanded = true }
                        )

                        FilterDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            selectedFilter = selectedFilter,
                            onFilterSelected = { selectedFilter = it }
                        )
                    }
                }
            }
            Divider(modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp))

            val filteredGroupedDuas = duaViewModel.groupedAndSortedDuas.mapValues { entry ->
                entry.value.filter { dua ->
                    val status = duaStatuses.find { it.duaNumber == dua.duaNumber }

                    val matchesSearch = dua.textheading?.contains(searchText, ignoreCase = true) == true

                    val matchesFilter = when (selectedFilter) {
                        "All" -> true
                        "Favorite" -> status?.favorite == true
                        "Memorized" -> status?.status == "Memorized"
                        "In Practice" -> status?.status == "In Practice"
                        else -> false
                    }

                    matchesSearch && matchesFilter
                }
            }.filter { it.value.isNotEmpty() }
            val newDuaListFromGroup = filteredGroupedDuas.mapNotNull { (_, duas) ->
                duas.firstOrNull()
            }
            LazyColumn(
                contentPadding = PaddingValues(bottom = 80.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(newDuaListFromGroup) { dua ->
                    val currentDuaStatus = duaStatuses.find { it.duaNumber == dua.duaNumber }
                    val isFavorite = currentDuaStatus?.favorite == true
                    val currentStatus = currentDuaStatus?.status ?: "In Practice"
                    val isMemorized = currentStatus == "Memorized"

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = dua.image),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(4.dp))
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${dua.duaNumber}${dua.textheading}",
                                fontWeight =W400,
                                fontSize = 12.sp,
                                fontFamily = title,
                                color = colorResource(R.color.heading_color),
                            )

                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                 val originalIndex = newDuaListFromGroup.indexOfFirst { it.duaNumber == dua.duaNumber }
                                val actualStatus = if (currentStatus == "Memorized") "Memorized" else "In Practice"
//                                val originalIndex = newDuaListFromGroup.indexOfFirst {
//                                    it.duaNumber == dua.duaNumber &&
//                                            it.textheading == dua.textheading &&
//                                            it.translation == dua.translation
//                                }

                                TagButton(
                                    text = actualStatus,
                                    backgroundDrawable = if (actualStatus == "Memorized") {
                                        R.drawable.memorized_btn_new
                                    } else {
                                        R.drawable.practice_now_btn
                                    },
                                    width = if (actualStatus == "Memorized") 100.dp else 84.dp,
                                    height = 28.dp,
                                    onClick = {
                                        if (originalIndex >= 0) {
                                            duaViewModel.updateCurrentIndex(originalIndex)
                                            navController.navigate("DuaNewScreen/$originalIndex")
                                        } else {
                                            Toast.makeText(context, "Dua not found", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                )

                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier.size(24.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (currentDuaStatus?.favorite == true) {
                                        Image(
                                            painter = painterResource(id = R.drawable.favourite_active_icon),
                                            contentDescription = "Favorite",
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(horizontalAlignment = Alignment.End) {
                            Switch(
                                checked = isMemorized,
                                onCheckedChange = { isChecked ->
                                    val newStatus = if (isChecked) "Memorized" else "In Practice"
                                    viewModel.updateDuaStatus(dua.duaNumber, newStatus)

                                    if (isChecked && rewardsEnabled) {
                                        showRewardAnimation = true
                                    }
                                },
                                modifier = Modifier.scale(1.0f),
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = colorResource(R.color.check_box),
                                    uncheckedThumbColor = colorResource(R.color.uncheckedThumbColor),
                                    uncheckedTrackColor = colorResource(R.color.white)
                                )
                            )

//                            if (showRewardAnimation) {
//                                Box(
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                      //  .background(Color.Black.copy(alpha = 0.4f))
//                                        .zIndex(2f),
//                                    contentAlignment = Alignment.Center
//                                ) {
//                                    LottieAnimation(
//                                        composition = composition,
//                                        progress = { progress },
//                                       // modifier = Modifier.size(250.dp)
//                                    )
//                                }
//                            }
                        }
                    }
                    Divider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                }
            }
        }
        LaunchedEffect(Unit) {
            viewModel.ensureAllDuasAreTracked(allDuas)
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

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val currentFilterType = navBackStackEntry?.arguments?.getString("filterType") ?: "All"

                IconButton(onClick = {
                    val targetFilter = "Favorite"
                    if (currentRoute?.startsWith("favorites") == true && currentFilterType == targetFilter) {
                        return@IconButton
                    }
                    navController.navigate("favorites?filterType=$targetFilter") {
                        launchSingleTop = true
                    }
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.favourite_icon),
                        contentDescription = "Favorites From menu",
                        modifier = Modifier.size(33.dp, 40.dp)
                    )
                }

                IconButton(onClick = {
                    val targetFilter = "All"
                    if (currentRoute?.startsWith("favorites") == true && currentFilterType == targetFilter) {
                        return@IconButton
                    }
                    navController.navigate("favorites?filterType=$targetFilter") {
                        launchSingleTop = true
                    }
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

                IconButton(
                    onClick = { navController.navigate("SettingsScreen") },
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icon_dua_setting),
                        contentDescription = "Settings",
                        modifier = Modifier.size(33.dp, 40.dp)
                    )
                }
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun MyduaStatusScreenPreview() {
    MyDuaStatusScreen(navController = rememberNavController(), innerPadding = PaddingValues(16.dp))
}
