package com.mercandalli.android.apps.files.note

import android.content.SharedPreferences

class NoteManagerSharedPreferences(
        private val sharedPreferences: SharedPreferences,
        private val addOn: AddOn
) : NoteManager {

    private var note: String

    init {
        val loadedNote = sharedPreferences.getString("note", "")
        note = loadedNote ?: ""
    }

    override fun getNote(): String {
        return note
    }

    override fun setNote(text: String) {
        note = text
        sharedPreferences.edit().putString("note", note).apply()
    }

    override fun delete() {
        note = ""
        sharedPreferences.edit().putString("note", "").apply()
    }

    override fun share() {
        addOn.shareText(note)
    }

    interface AddOn {

        fun shareText(text: String)
    }

    companion object {

        @JvmStatic
        val PREFERENCE_NAME = "NoteManager"
    }
}