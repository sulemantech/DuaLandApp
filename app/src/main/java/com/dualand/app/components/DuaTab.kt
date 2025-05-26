package com.dualand.app.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text as MaterialText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dualand.app.DuaViewModel
import com.dualand.app.R
import com.dualand.app.models.Dua
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
private fun TabItem(
    text: String,
    isSelected: Boolean,
    fontFamily: FontFamily,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        MaterialText(
            text = text,
            color = if (isSelected) colorResource(R.color.white) else colorResource(R.color.heading_color),
            fontFamily = fontFamily,
            fontSize = 16.sp,
        )
    }
}

@Composable
fun DuaTabs(
    dua: List<Dua>,
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {

    val viewModel: DuaViewModel = viewModel()
    val selectedTab by viewModel.selectedTab.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.tab_bg_pink),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.matchParentSize().padding(horizontal = 12.dp).height(50.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            //  horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Button(
                onClick = { viewModel.setSelectedTab("WORD") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab == "WORD") colorResource(R.color.highlited_color)
                    else colorResource(R.color.tab_selected)
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                MaterialText("WORD BY WORD")
            }

            Button(
                onClick = { viewModel.setSelectedTab("COMPLETE") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab == "COMPLETE") colorResource(R.color.highlited_color)
                    else colorResource(R.color.tab_selected)
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                MaterialText("COMPLETE DUA")
            }
        }


    }
}
