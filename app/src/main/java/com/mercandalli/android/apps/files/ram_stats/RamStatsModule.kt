package com.mercandalli.android.apps.files.ram_stats

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE

class RamStatsModule(
    private val context: Context
) {

    fun createRamStatsManager(): RamStatsManager {
        val activityManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        return RamStatsManagerImpl(
            activityManager
        )
    }
}
