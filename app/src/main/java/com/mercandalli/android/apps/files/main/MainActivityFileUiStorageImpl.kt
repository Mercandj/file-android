package com.mercandalli.android.apps.files.main

import android.content.SharedPreferences

class MainActivityFileUiStorageImpl(
    private val sharedPreferences: SharedPreferences
) : MainActivityFileUiStorage {

    private var currentFileUi = MainActivityFileUiStorage.SECTION_FILE_LIST

    init {
        currentFileUi = sharedPreferences.getInt("currentFileUi", currentFileUi)
    }

    @MainActivityFileUiStorage.Companion.Section
    override fun getCurrentFileUi(): Int {
        return currentFileUi
    }

    override fun setCurrentFileUi(@MainActivityFileUiStorage.Companion.Section section: Int) {
        if (currentFileUi == section) {
            return
        }
        currentFileUi = section
        sharedPreferences.edit().putInt("currentFileUi", section).apply()
    }

    companion object {
        @JvmStatic
        val PREFERENCE_NAME = "MainActivityFileUiStorage"
    }
}
