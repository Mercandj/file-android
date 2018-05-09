package com.mercandalli.android.apps.files.note

interface NoteManager {

    fun getNote(): String

    fun setNote(text: String)
}