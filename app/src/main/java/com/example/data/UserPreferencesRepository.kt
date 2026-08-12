package com.example.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.calculator.engine.AngleUnit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_settings")

enum class ThemeMode {
    SYSTEM, LIGHT, DARK
}

data class UserPreferences(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val hapticsEnabled: Boolean = true,
    val soundEnabled: Boolean = false,
    val numberFormattingEnabled: Boolean = true,
    val angleUnit: AngleUnit = AngleUnit.DEG,
    val scientificModeExpanded: Boolean = false
)

class UserPreferencesRepository(private val context: Context) {

    private object PreferenceKeys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val HAPTICS_ENABLED = booleanPreferencesKey("haptics_enabled")
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val FORMATTING_ENABLED = booleanPreferencesKey("formatting_enabled")
        val ANGLE_UNIT = stringPreferencesKey("angle_unit")
        val SCIENTIFIC_EXPANDED = booleanPreferencesKey("scientific_expanded")
    }

    val preferences: Flow<UserPreferences> = context.dataStore.data.map { prefs ->
        val themeStr = prefs[PreferenceKeys.THEME_MODE] ?: ThemeMode.SYSTEM.name
        val themeMode = try { ThemeMode.valueOf(themeStr) } catch (e: Exception) { ThemeMode.SYSTEM }

        val angleStr = prefs[PreferenceKeys.ANGLE_UNIT] ?: AngleUnit.DEG.name
        val angleUnit = try { AngleUnit.valueOf(angleStr) } catch (e: Exception) { AngleUnit.DEG }

        UserPreferences(
            themeMode = themeMode,
            hapticsEnabled = prefs[PreferenceKeys.HAPTICS_ENABLED] ?: true,
            soundEnabled = prefs[PreferenceKeys.SOUND_ENABLED] ?: false,
            numberFormattingEnabled = prefs[PreferenceKeys.FORMATTING_ENABLED] ?: true,
            angleUnit = angleUnit,
            scientificModeExpanded = prefs[PreferenceKeys.SCIENTIFIC_EXPANDED] ?: false
        )
    }

    suspend fun updateThemeMode(mode: ThemeMode) {
        context.dataStore.edit { prefs ->
            prefs[PreferenceKeys.THEME_MODE] = mode.name
        }
    }

    suspend fun updateHapticsEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[PreferenceKeys.HAPTICS_ENABLED] = enabled
        }
    }

    suspend fun updateSoundEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[PreferenceKeys.SOUND_ENABLED] = enabled
        }
    }

    suspend fun updateNumberFormattingEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[PreferenceKeys.FORMATTING_ENABLED] = enabled
        }
    }

    suspend fun updateAngleUnit(unit: AngleUnit) {
        context.dataStore.edit { prefs ->
            prefs[PreferenceKeys.ANGLE_UNIT] = unit.name
        }
    }

    suspend fun updateScientificExpanded(expanded: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[PreferenceKeys.SCIENTIFIC_EXPANDED] = expanded
        }
    }
}
