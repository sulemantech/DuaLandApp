package com.dualand.app.models

data class DuaItem(
    val imageRes: Int,
    val title: String,
    val textheading: String,
    val onClick: () -> Unit
) {

}
