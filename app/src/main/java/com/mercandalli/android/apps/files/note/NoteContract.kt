package com.mercandalli.android.apps.files.note

interface NoteContract {

    interface UserAction {

        fun onTextChanged(text: String)

        fun onShareClicked()

        fun onDeleteClicked()
    }

    interface Screen {

        fun setNote(note: String)
    }
}