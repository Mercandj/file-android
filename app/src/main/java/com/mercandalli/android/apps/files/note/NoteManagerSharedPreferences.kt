package com.mercandalli.android.apps.files.note

import android.content.SharedPreferences

class NoteManagerSharedPreferences(
        private val sharedPreferences: SharedPreferences
) : NoteManager {

    private var note: String

    init {
        note = sharedPreferences.getString("note", "")
    }

    override fun getNote(): String {
        return note
    }

    override fun setNote(text: String) {
        note = text
        sharedPreferences.edit().putString("note", note).apply()
    }

    companion object {
        @JvmStatic
        val PREFERENCE_NAME = "NoteManager"
    }
}