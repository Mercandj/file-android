package com.mercandalli.android.apps.files.main

import java.lang.IllegalStateException

internal interface MainActivitySectionStorage {

    fun putSection(section: Section)

    fun getSection(): Section

    companion object {

        enum class Section(val value: Int) {
            UNDEFINED(100),
            FILE(200),
            ONLINE(300),
            NOTE(400),
            SETTINGS(500)
        }

        fun getSection(sectionId: Int) = when (sectionId) {
            100 -> Section.UNDEFINED
            200 -> Section.FILE
            300 -> Section.ONLINE
            400 -> Section.NOTE
            500 -> Section.SETTINGS
            else -> throw IllegalStateException("Wrong id: $sectionId")
        }
    }
}
