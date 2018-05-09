package com.mercandalli.android.apps.files.note

import android.content.Context

class NoteModule(
        private val context: Context
) {

    fun provideNoteManager(): NoteManager {
        val sharedPreferences = context.getSharedPreferences(
                NoteManagerSharedPreferences.PREFERENCE_NAME,
                Context.MODE_PRIVATE)
        return NoteManagerSharedPreferences(sharedPreferences)
    }
}