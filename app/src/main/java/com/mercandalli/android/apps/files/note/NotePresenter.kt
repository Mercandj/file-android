package com.mercandalli.android.apps.files.note

class NotePresenter(
        private val screen: NoteContract.Screen,
        private val noteManager: NoteManager
) : NoteContract.UserAction {

    init {
        val note = noteManager.getNote()
        screen.setNote(note)
    }

    override fun onTextChanged(text: String) {
        noteManager.setNote(text)
    }

    override fun onShareClicked() {
        noteManager.share()
    }

    override fun onDeleteClicked() {
        screen.showDeleteConfirmation()
    }

    override fun onDeleteConfirmedClicked() {
        noteManager.delete()
        val note = noteManager.getNote()
        screen.setNote(note)
    }
}