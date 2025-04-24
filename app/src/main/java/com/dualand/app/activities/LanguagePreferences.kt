package com.dualand.app.activities

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow

object LanguagePreferences {
    private val Context.dataStore by preferencesDataStore("settings")

    val SELECTED_LANGUAGES = stringSetPreferencesKey("selected_languages")

    suspend fun saveLanguages(context: Context, languages: Set<String>) {
        context.dataStore.edit { prefs ->
            prefs[SELECTED_LANGUAGES] = languages
        }
    }

    fun getSelectedLanguages(context: Context): Flow<Set<String>> {
        return context.dataStore.data.map { prefs ->
            prefs[SELECTED_LANGUAGES] ?: setOf("English", "Hindi") // default
        }
    }
}
