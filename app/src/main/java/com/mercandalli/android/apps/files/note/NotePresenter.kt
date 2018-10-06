package com.mercandalli.android.apps.files.note

import com.mercandalli.android.apps.files.theme.ThemeManager

class NotePresenter(
        private val screen: NoteContract.Screen,
        private val noteManager: NoteManager,
        private val themeManager: ThemeManager
) : NoteContract.UserAction {

    init {
        val note = noteManager.getNote()
        screen.setNote(note)
    }

    private val themeListener = createThemeListener()

    override fun onAttached() {
        themeManager.registerThemeListener(themeListener)
        syncWithCurrentTheme()
    }

    override fun onDetached() {
        themeManager.unregisterThemeListener(themeListener)
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

    private fun syncWithCurrentTheme() {
        val theme = themeManager.getTheme()
        screen.setTextColorRes(theme.textPrimaryColorRes)
        screen.setTextHintColorRes(theme.textSecondaryColorRes)
        screen.setCardBackgroundColorRes(theme.cardBackgroundColorRes)
    }

    private fun createThemeListener() = object : ThemeManager.ThemeListener {
        override fun onThemeChanged() {
            syncWithCurrentTheme()
        }
    }
}