package com.dualand.app.activities

import androidx.compose.material3.MaterialTheme
import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dualand.app.DuaViewModel
import com.dualand.app.R
import com.dualand.app.activities.DuaDataProvider.duaList
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.time.format.TextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDuaStatusScreen(navController: NavController, innerPadding: PaddingValues) {

    val viewModel: DuaViewModel = viewModel()
    val systemUiController = rememberSystemUiController()
    val NavigationBarColor = colorResource(id = R.color.top_nav_new)
    val statusBarColor = colorResource(id = R.color.top_nav_new)
    var searchText by remember { mutableStateOf("") }
    val context = LocalContext.current
    val title = FontFamily(Font(R.font.mochypop_regular))
    val MyArabicFont = FontFamily(Font(R.font.doodlestrickers))
    val duaStatuses by viewModel.allDuas.collectAsState()
    val allDuas = duaList.filter { !it.textheading.isNullOrBlank() }
    var showDialog by remember { mutableStateOf(false) }
    var selectedFilters by remember { mutableStateOf(listOf<String>()) } // Track selected filters
    var expanded by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("All") }
    // In your ViewModel or composable, track the index of the current Dua
    var index by remember { mutableStateOf(0) }


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
                            painter = painterResource(id = R.drawable.icon_dua_setting),
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp, start = 20.dp, end = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Filter By",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Image(
                    painter = painterResource(R.drawable.filter_icon),
                    contentDescription = "Filter Icon",
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { expanded = true }
                )
            }

            Divider(modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp))
            val filteredDuas = allDuas.filter { dua ->
                val status = duaStatuses.find { it.duaNumber == dua.duaNumber }
                when (selectedFilter) {
                    "All" -> true
                    "Favorite" -> status?.favorite == true
                    "Memorized" -> status?.status == "Memorized"
                    "In Practice" -> status?.status == "In Practice"
                    else -> false
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(bottom = 80.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredDuas) { dua ->
                    val currentDuaStatus = duaStatuses.find { it.duaNumber == dua.duaNumber }
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
                                .clip(RoundedCornerShape(6.dp))
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${dua.duaNumber}${dua.textheading}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color.Black
                            )

                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                val indexInOriginalList = duaList.indexOfFirst { it.duaNumber == dua.duaNumber }

                                TagButton(
                                    text = when (currentStatus) {
                                        "Memorized" -> "Memorized"
                                        "In Practice" -> "In Practice"
                                        else -> "Not Started"
                                    },
                                    backgroundDrawable = when (currentStatus) {
                                        "Memorized" -> R.drawable.memorized_btn
                                        "In Practice" -> R.drawable.practice_now_btn
                                        else -> null
                                    },
                                    width = when (currentStatus) {
                                        "Memorized" -> 94.dp
                                        "In Practice" -> 75.dp
                                        else -> 80.dp
                                    },
                                    height = 25.dp,
                                    onClick = {
                                        if (currentStatus == "In Practice" && indexInOriginalList != -1) {
                                            navController.navigate("dua/$indexInOriginalList")
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
                                    viewModel.updateDuaStatus(
                                        duaNumber = dua.duaNumber,
                                        newStatus = newStatus
                                    )
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = colorResource(R.color.check_box),
                                    uncheckedThumbColor = colorResource(R.color.uncheckedThumbColor),
                                    uncheckedTrackColor = colorResource(R.color.white)
                                )
                            )
                        }
                    }
                    Divider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                }
            }
        }
        val filterIconModifier = Modifier
            .align(Alignment.TopEnd)

        FilterDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            selectedFilter = selectedFilter,
            onFilterSelected = { selectedFilter = it }
        )
    }
}

@Composable
fun FilterDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    val filters = listOf("All", "Memorized", "In Practice", "Favorite")

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = Modifier.background(Color.White)
    ) {
        filters.forEachIndexed { index, filter ->
            DropdownMenuItem(
                onClick = {
                    onFilterSelected(filter)
                    onDismissRequest()
                },
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = filter,
                            modifier = Modifier.weight(1f),
                        )

                        if (filter == selectedFilter) {
                            Text(
                                text = "✓",
                                color = colorResource(R.color.highlited_color),
                            )
                        }
                    }
                }
            )
            if (index < filters.size - 1) {
                Divider()
            }
        }
    }
}

@Composable
fun TagButton(
    text: String,
    bgColor: Color? = null,
    backgroundDrawable: Int? = null, // e.g., R.drawable.in_practice_bg
    width: Dp? = null,
    height: Dp? = null,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .then(
                if (backgroundDrawable != null) {
                    Modifier.background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Transparent)
                        )
                    ) // Placeholder to maintain shape
                } else Modifier.background(bgColor ?: Color.Gray)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .let {

                Modifier.then(
                    if (width != null && height != null) {
                        Modifier.size(width, height)
                    } else Modifier
                )
            }
    ) {
        if (backgroundDrawable != null) {
            Image(
                painter = painterResource(id = backgroundDrawable),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(12.dp))
            )
        }

    }
}
//in practice me FilterDropdownMenu select py in pratcie wly hi any chahiye
@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun MyduaStatusScreenPreview() {
    MyDuaStatusScreen(navController = rememberNavController(), innerPadding = PaddingValues(16.dp))
}
