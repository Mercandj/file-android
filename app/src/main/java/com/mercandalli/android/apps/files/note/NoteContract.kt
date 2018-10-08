package com.mercandalli.android.apps.files.note

import androidx.annotation.ColorRes

interface NoteContract {

    interface UserAction {

        fun onAttached()

        fun onDetached()

        fun onTextChanged(text: String)

        fun onShareClicked()

        fun onDeleteClicked()

        fun onDeleteConfirmedClicked()
    }

    interface Screen {

        fun setNote(note: String)

        fun showDeleteConfirmation()

        fun setTextColorRes(@ColorRes colorRes: Int)

        fun setTextHintColorRes(@ColorRes colorRes: Int)

        fun setCardBackgroundColorRes(@ColorRes colorRes: Int)
    }
}