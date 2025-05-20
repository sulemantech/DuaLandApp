package com.dualand.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dualand.app.R

@Composable
fun FilterDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    val text_font = FontFamily(Font(R.font.montserrat_regular))

    val filters = listOf("All", "Memorized", "In Practice").toMutableList()
    if (selectedFilter != "Favorite") {
        filters.add("Favorite")
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .background(Color.White)
            .width(170.dp)
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
                            fontFamily = text_font,
                            fontSize = 14.sp,
                            color = colorResource(R.color.heading_color),
                            fontWeight = W600
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