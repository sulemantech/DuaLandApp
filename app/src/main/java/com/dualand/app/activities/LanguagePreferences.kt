package com.dualand.app.activities

import android.content.Context
import android.content.SharedPreferences

object LanguagePreferences {

    private const val PREF_NAME = "language_preferences"
    private const val KEY_LANGUAGES = "selected_languages"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveLanguages(context: Context, languages: Set<String>) {
        val sharedPreferences = getSharedPreferences(context)
        with(sharedPreferences.edit()) {
            putStringSet(KEY_LANGUAGES, languages)
            apply()
        }
    }

    fun getLanguages(context: Context): Set<String> {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getStringSet(KEY_LANGUAGES, setOf()) ?: setOf()
    }
}
