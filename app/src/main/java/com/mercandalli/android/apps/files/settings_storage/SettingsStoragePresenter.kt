@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.settings_storage

import com.mercandalli.android.apps.files.file_storage_stats.FileStorageStatsManager
import com.mercandalli.android.apps.files.screen.ScreenManager
import com.mercandalli.android.apps.files.theme.Theme
import com.mercandalli.android.apps.files.theme.ThemeManager
import com.mercandalli.sdk.files.api.FileSizeUtils

class SettingsStoragePresenter(
    private val screen: SettingsStorageContract.Screen,
    private val themeManager: ThemeManager,
    private val fileStorageStatsManager: FileStorageStatsManager,
    private val screenManager: ScreenManager
) : SettingsStorageContract.UserAction {

    private val themeListener = createThemeListener()

    override fun onAttached() {
        themeManager.registerThemeListener(themeListener)
        updateTheme()
        updateStorage()
    }

    override fun onDetached() {
        themeManager.unregisterThemeListener(themeListener)
    }

    override fun onStorageLocalRowClicked() {
        screenManager.startSystemSettingsStorage()
    }

    private fun updateStorage() {
        val freeMemory = fileStorageStatsManager.getFreeMemory()
        val busyMemory = fileStorageStatsManager.getBusyMemory()
        val totalMemory = fileStorageStatsManager.getTotalMemory()
        val busyPercent = ((busyMemory.toFloat() * 100f) / totalMemory).toInt()
        val freeMemoryString = FileSizeUtils.humanReadableByteCount(freeMemory)
        val busyMemoryString = FileSizeUtils.humanReadableByteCount(busyMemory)
        val totalMemoryString = FileSizeUtils.humanReadableByteCount(totalMemory)
        screen.setLocalSubLabelText("$busyPercent% used - $freeMemoryString free")
        screen.setLocalBusy("$busyMemoryString busy")
        screen.setLocalTotal("$totalMemoryString total")
        screen.setProgress(busyMemory.toFloat() / totalMemory.toFloat())
    }

    private fun updateTheme(theme: Theme = themeManager.getTheme()) {
        screen.setTextPrimaryColorRes(theme.textPrimaryColorRes)
        screen.setTextSecondaryColorRes(theme.textSecondaryColorRes)
        screen.setSectionColor(theme.cardBackgroundColorRes)
    }

    private fun createThemeListener() = object : ThemeManager.ThemeListener {
        override fun onThemeChanged() {
            updateTheme()
        }
    }
}
