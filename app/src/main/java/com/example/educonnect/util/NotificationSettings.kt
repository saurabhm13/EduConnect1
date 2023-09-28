package com.example.educonnect.util

import android.content.Context

object NotificationSettings {

    fun getNotificationSetting(context: Context): Boolean {
        val sharedPref = context.getSharedPreferences(Constants.NOTIFICATION_PREFERENCE, Context.MODE_PRIVATE)
        return sharedPref.getBoolean("notification_enabled", true)
    }

    fun setNotificationSetting(enabled: Boolean, context: Context) {
        val sharedPref = context.getSharedPreferences(Constants.NOTIFICATION_PREFERENCE, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("notification_enabled", enabled)
            apply()
        }
    }

}