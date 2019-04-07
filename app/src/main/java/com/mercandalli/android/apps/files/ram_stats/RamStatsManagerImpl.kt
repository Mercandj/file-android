package com.mercandalli.android.apps.files.ram_stats

import android.app.ActivityManager

class RamStatsManagerImpl(
    private val activityManager: ActivityManager
) : RamStatsManager {

    override fun getTotalMemory(): Long {
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        return  memoryInfo.totalMem
    }

    override fun getFreeMemory(): Long {
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        return  memoryInfo.availMem
    }

    override fun getBusyMemory(): Long {
        return getTotalMemory() - getFreeMemory()
    }
}
