package com.mercandalli.android.apps.files.main

interface MainActivityFileUiStorage {

    fun getCurrentFileUi(): Int

    fun setCurrentFileUi(section: Int)

    companion object {
        const val SECTION_FILE_LIST = 1
        const val SECTION_FILE_COLUMN = 2
    }
}