package com.example.educonnect

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.educonnect.util.Constants.Companion.THEME_DARK
import com.example.educonnect.util.Constants.Companion.THEME_LIGHT
import com.example.educonnect.util.Constants.Companion.THEME_PREFERENCE

object ThemeManager {

    fun applyTheme(theme: String) {
        when (theme) {
            THEME_LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            THEME_DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    fun saveThemePreference(context: Context, theme: String) {
        val preferences = context.getSharedPreferences(THEME_PREFERENCE, Context.MODE_PRIVATE)
        preferences.edit().putString(THEME_PREFERENCE, theme).apply()
    }

    fun getThemePreference(context: Context): String {
        val preferences = context.getSharedPreferences(THEME_PREFERENCE, Context.MODE_PRIVATE)
        return preferences.getString(THEME_PREFERENCE, THEME_LIGHT) ?: THEME_LIGHT
    }

}