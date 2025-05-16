package com.example.shoppinglistapp_flerko.ui

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.Window

object ThemeManager {
    fun setDarkMode(activity: Activity, isDarkMode: Boolean) {
        if (isDarkMode) {
            activity.setTheme(androidx.appcompat.R.style.Theme_AppCompat_DayNight_NoActionBar)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                activity.window.statusBarColor = 0xFF121212.toInt()
            }
        } else {
            activity.setTheme(androidx.appcompat.R.style.Theme_AppCompat_Light_NoActionBar)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                activity.window.statusBarColor = 0xFFFFFFFF.toInt()
            }
        }
    }
}