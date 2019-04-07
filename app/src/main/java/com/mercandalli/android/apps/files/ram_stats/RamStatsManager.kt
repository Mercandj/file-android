package com.mercandalli.android.apps.files.ram_stats

interface RamStatsManager {

    fun getTotalMemory(): Long

    fun getFreeMemory(): Long

    fun getBusyMemory(): Long
}
