package com.mercandalli.android.apps.files.main

import android.content.SharedPreferences

internal class MainActivitySectionStorageImpl(
    private val sharedPreferences: SharedPreferences
) : MainActivitySectionStorage {

    private var section = MainActivitySectionStorage.Companion.Section.FILE

    init {
        val sectionId = sharedPreferences.getInt("section", section.value)
        section = MainActivitySectionStorage.Companion.getSection(sectionId)
    }

    override fun putSection(section: MainActivitySectionStorage.Companion.Section) {
        if (this.section == section) {
            return
        }
        this.section = section
        sharedPreferences.edit().putInt("section", section.value).apply()
    }

    override fun getSection(): MainActivitySectionStorage.Companion.Section {
        return section
    }

    companion object {
        @JvmStatic
        val PREFERENCE_NAME = "MainActivitySectionStorage"
    }
}
