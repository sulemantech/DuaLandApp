package com.dualand.app.models

data class Dua(
    val arabic: String,
    val translation: String,
    val hinditranslation: String,
    val urdu: String,
    val reference: String,
    val backgroundResId: Int,
    val statusBarColorResId: Int,
    val fullAudioResId: Int,
    val image: Int,
    val textheading: String,
    val duaNumber: String,
    val steps: String? = null ,
    val titleAudioResId: Int? = null,
    val wordAudioPairs: List<Pair<String, Int>>
)
