package com.example.educonnect

import android.app.Activity
import android.app.Application
import java.lang.ref.WeakReference

class MyApplication : Application() {

    private var currentActivity: WeakReference<Activity>? = null

    override fun onCreate() {
        super.onCreate()
//        ThemeManager.applyTheme(this)



        fun setCurrentActivity(activity: Activity) {
            currentActivity = WeakReference(activity)
        }

        fun currentActivity(): Activity? {
            return currentActivity?.get()
        }

    }
}