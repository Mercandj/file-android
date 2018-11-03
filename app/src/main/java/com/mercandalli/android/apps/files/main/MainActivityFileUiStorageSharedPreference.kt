package com.mercandalli.android.apps.files.main

import android.content.SharedPreferences

class MainActivityFileUiStorageSharedPreference(
    private val sharedPreference: SharedPreferences
) : MainActivityFileUiStorage {

    private var currentFileUi = MainActivityFileUiStorage.SECTION_FILE_LIST

    init {
        currentFileUi = sharedPreference.getInt("currentFileUi", currentFileUi)
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
        sharedPreference.edit().putInt("currentFileUi", section).apply()
    }

    companion object {
        @JvmStatic
        val PREFERENCE_NAME = "MainActivityFileUiStorage"
    }
}
