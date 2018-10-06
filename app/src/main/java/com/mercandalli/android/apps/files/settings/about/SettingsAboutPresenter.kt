package com.mercandalli.android.apps.files.settings.about

import com.google.android.material.snackbar.Snackbar
import com.mercandalli.android.apps.files.dialog.DialogManager
import com.mercandalli.android.apps.files.settings.SettingsManager
import com.mercandalli.android.apps.files.theme.ThemeManager
import com.mercandalli.android.apps.files.version.VersionManager
import com.mercandalli.android.apps.files.R
import com.mercandalli.android.apps.files.hash.HashManager

class SettingsAboutPresenter(
        private val screen: SettingsAboutContract.Screen,
        versionManager: VersionManager,
        private val themeManager: ThemeManager,
        private val dialogManager: DialogManager,
        private val settingsManager: SettingsManager,
        private val hashManager: HashManager,
        private val addOn: AddOn
) : SettingsAboutContract.UserAction {

    private val themeListener = createThemeListener()
    private val dialogListener = createDialogListener()
    private val versionClickTimestampsMs = ArrayList<Long>()

    init {
        val versionName = versionManager.getVersionName()
        screen.showVersionName(versionName)
    }

    override fun onAttached() {
        themeManager.registerThemeListener(themeListener)
        syncWithCurrentTheme()
    }

    override fun onDetached() {
        themeManager.unregisterThemeListener(themeListener)
    }

    override fun onRateClicked() {
        screen.openUrl(PLAY_STORE_URL_FILESPACE)
    }

    override fun onTeamAppsClicked() {
        screen.openUrl(PLAY_STORE_URL_TEAM_MERCAN)
    }

    override fun onVersionClicked() {
        val currentTimeMillis = addOn.getCurrentTimeMillis()
        versionClickTimestampsMs.add(currentTimeMillis)
        if (!isEnoughVersionClick(versionClickTimestampsMs, currentTimeMillis, 5, 1000)) {
            return
        }
        val appDeveloperEnabled = settingsManager.isDeveloperMode()
        if (appDeveloperEnabled) {
            setIsAppDeveloperEnabled(false)
            return
        }
        dialogManager.registerListener(dialogListener)
        dialogManager.alert(
                DIALOG_ID_VERSION_NAME,
                R.string.view_settings_developer_activation_message_title,
                R.string.view_settings_developer_activation_message,
                R.string.view_settings_developer_activation_message_positive,
                R.string.view_settings_developer_activation_message_negative
        )
    }

    private fun syncWithCurrentTheme() {
        val theme = themeManager.getTheme()
        screen.setCardBackgroundColorRes(theme.cardBackgroundColorRes)
        screen.setTitlesTextColorRes(theme.textPrimaryColorRes)
        screen.setSubtitlesTextColorRes(theme.textSecondaryColorRes)
    }

    private fun consumeDialogActionPositiveClicked(dialogAction: DialogManager.DialogAction?) {
        if (dialogAction == null) {
            return
        }
        when (dialogAction.dialogId) {
            DIALOG_ID_VERSION_NAME -> {
                dialogManager.prompt(
                        DIALOG_ID_PROMPT_PASS,
                        R.string.view_settings_developer_activation_message_title,
                        R.string.view_settings_developer_activation_password,
                        R.string.view_settings_developer_activation_ok,
                        R.string.view_settings_developer_activation_cancel
                )
            }
            DIALOG_ID_PROMPT_PASS -> {
                val isAppDeveloperModeEnabled = hashManager.sha256(dialogAction.userInput, 32) ==
                        "1753549de2d885325195f6ab9e3f86174f7f2626ccd3d4eccae82398b48de19d"
                setIsAppDeveloperEnabled(isAppDeveloperModeEnabled)
            }
        }
    }

    private fun setIsAppDeveloperEnabled(isAppDeveloperModeEnabled: Boolean) {
        settingsManager.setDeveloperMode(isAppDeveloperModeEnabled)
        screen.showSnackbar(
                if (isAppDeveloperModeEnabled) R.string.view_settings_developer_mode_enabled
                else R.string.view_settings_developer_mode_disabled,
                Snackbar.LENGTH_SHORT
        )
        versionClickTimestampsMs.clear()
    }

    private fun isEnoughVersionClick(
            timestamps: List<Long>,
            currentTimestamp: Long,
            nbClick: Int,
            duration: Long
    ): Boolean {
        if (timestamps.size < nbClick) {
            return false
        }
        return currentTimestamp < timestamps[timestamps.size - nbClick] + duration
    }

    private fun createThemeListener() = object : ThemeManager.ThemeListener {
        override fun onThemeChanged() {
            syncWithCurrentTheme()
        }
    }

    private fun createDialogListener() = object : DialogManager.Listener {
        override fun onDialogPositiveClicked(dialogAction: DialogManager.DialogAction): Boolean {
            consumeDialogActionPositiveClicked(dialogAction)
            return true
        }

        override fun onDialogNegativeClicked(dialogAction: DialogManager.DialogAction) {

        }
    }

    companion object {
        private const val PLAY_STORE_URL_FILESPACE = "https://play.google.com/store/apps/details?id=com.mercandalli.android.apps.files"
        private const val PLAY_STORE_URL_TEAM_MERCAN = "https://play.google.com/store/apps/dev?id=8371778130997780965"

        private const val DIALOG_ID_VERSION_NAME = "SettingsAboutPresenter.DIALOG_ID_VERSION_NAME"
        private const val DIALOG_ID_PROMPT_PASS = "SettingsAboutPresenter.DIALOG_ID_PROMPT_PASS"
    }

    interface AddOn {

        fun getCurrentTimeMillis(): Long
    }
}