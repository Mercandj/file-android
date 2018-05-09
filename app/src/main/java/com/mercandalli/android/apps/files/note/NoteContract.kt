package com.mercandalli.android.apps.files.note

interface NoteContract {

    interface UserAction {

        fun onTextChanged(text: String)
    }

    interface Screen {

        fun setNote(note: String)
    }
}