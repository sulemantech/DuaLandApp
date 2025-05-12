package com.dualand.app.activities

import android.content.Context
import android.content.SharedPreferences

object LanguagePreferences {
    private const val PREF_NAME = "language_prefs"
    private const val SELECTED_LANGUAGES_KEY = "selected_languages"

    fun saveLanguages(context: Context, languages: Set<String>) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putStringSet(SELECTED_LANGUAGES_KEY, languages).apply()
    }

    fun getSelectedLanguages(context: Context): Set<String> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(SELECTED_LANGUAGES_KEY, setOf("English")) ?: setOf("English")
    }
}
