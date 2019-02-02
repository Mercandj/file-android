@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.settings_developer

import android.widget.ScrollView
import androidx.annotation.StringRes
import com.mercandalli.android.apps.files.developer.DeveloperManager
import com.mercandalli.android.apps.files.theme.ThemeManager
import com.mercandalli.android.apps.files.theme.Theme
import com.mercandalli.sdk.files.api.online.FileOnlineLoginManager
import com.mercandalli.android.apps.files.R
import com.mercandalli.android.apps.files.dialog.DialogManager

class SettingsDeveloperPresenter(
    private val screen: SettingsDeveloperContract.Screen,
    private val themeManager: ThemeManager,
    private val developerManager: DeveloperManager,
    private val fileOnlineLoginManager: FileOnlineLoginManager,
    private val dialogManager: DialogManager,
    private val addOn: AddOn
) : SettingsDeveloperContract.UserAction {

    private val themeListener = createThemeListener()
    private val dialogListener = createDialogListener()
    private val appDeveloperListener = createAppDeveloperListener()
    private val loginListener = createLoginListener()

    override fun onAttached() {
        themeManager.registerThemeListener(themeListener)
        dialogManager.registerListener(dialogListener)
        developerManager.registerDeveloperModeListener(appDeveloperListener)
        fileOnlineLoginManager.registerLoginListener(loginListener)
        updateTheme()
        syncDeveloperSection()
    }

    override fun onDetached() {
        themeManager.unregisterThemeListener(themeListener)
        dialogManager.unregisterListener(dialogListener)
        developerManager.unregisterDeveloperModeListener(appDeveloperListener)
        fileOnlineLoginManager.unregisterLoginListener(loginListener)
    }

    override fun onOnlineLoginRowClicked() {
        dialogManager.prompt(
            DIALOG_ID_ONLINE_LOGIN,
            R.string.view_settings_developer_online_login_label,
            R.string.view_settings_developer_online_login_label,
            R.string.ok,
            R.string.cancel
        )
    }

    override fun onOnlinePasswordRowClicked() {
        dialogManager.prompt(
            DIALOG_ID_ONLINE_PASSWORD,
            R.string.view_settings_developer_online_password_label,
            R.string.view_settings_developer_online_password_label,
            R.string.ok,
            R.string.cancel
        )
    }

    override fun onActivationRowClicked() {
        developerManager.setDeveloperMode(false)
        syncDeveloperSection()
    }

    override fun onDeveloperActivationCheckChanged(checked: Boolean) {
        developerManager.setDeveloperMode(checked)
        syncDeveloperSection()
    }

    private fun syncDeveloperSection(isAppDeveloperModeEnabled: Boolean = developerManager.isDeveloperMode()) {
        val visibility = if (isAppDeveloperModeEnabled) ScrollView.VISIBLE else ScrollView.GONE
        screen.setDeveloperSectionLabelVisibility(visibility)
        screen.setDeveloperSectionVisibility(visibility)
        screen.setDeveloperActivationChecked(isAppDeveloperModeEnabled)

        val login = fileOnlineLoginManager.getLogin()
        val onlineLoginSubLabel = if (login == null) {
            addOn.getString(R.string.view_settings_developer_online_sublabel_not_logged)
        } else {
            addOn.getString(R.string.view_settings_developer_online_sublabel, login)
        }
        screen.setOnlineLoginSubLabelText(onlineLoginSubLabel)

        val logged = fileOnlineLoginManager.isLogged()
        val onlinePasswordSubLabel = if (logged) {
            addOn.getString(R.string.view_settings_developer_online_passowrd_sublabel_logged)
        } else {
            addOn.getString(R.string.view_settings_developer_online_passowrd_sublabel_not_logged)
        }
        screen.setOnlinePasswordSubLabelText(onlinePasswordSubLabel)

        if (!isAppDeveloperModeEnabled) {
            return
        }
        syncDeveloperRemoteCountry()
    }

    private fun syncDeveloperRemoteCountry() {
    }

    private fun updateTheme(theme: Theme = themeManager.getTheme()) {
        screen.setTextPrimaryColorRes(theme.textPrimaryColorRes)
        screen.setTextSecondaryColorRes(theme.textSecondaryColorRes)
        screen.setSectionColor(theme.cardBackgroundColorRes)
    }

    private fun createThemeListener() = object : ThemeManager.ThemeListener {
        override fun onThemeChanged() {
            updateTheme()
        }
    }

    private fun createAppDeveloperListener() = object : DeveloperManager.DeveloperModeListener {
        override fun onDeveloperModeChanged() {
            syncDeveloperSection()
        }
    }

    private fun createLoginListener() = object : FileOnlineLoginManager.LoginListener{
        override fun onOnlineLogChanged() {
            syncDeveloperSection()
        }
    }

    private fun createDialogListener() = object : DialogManager.Listener {

        override fun onDialogPositiveClicked(dialogAction: DialogManager.DialogAction): Boolean {
            return when (dialogAction.dialogId) {
                DIALOG_ID_ONLINE_LOGIN -> {
                    fileOnlineLoginManager.setLogin(dialogAction.userInput)
                    true
                }
                DIALOG_ID_ONLINE_PASSWORD -> {
                    fileOnlineLoginManager.setPassword(dialogAction.userInput)
                    true
                }
                else -> false
            }
        }

        override fun onDialogNegativeClicked(dialogAction: DialogManager.DialogAction) {
        }
    }

    companion object {
        private const val DIALOG_ID_ONLINE_LOGIN = "SettingsDeveloperPresenter.DIALOG_ID_ONLINE_LOGIN"
        private const val DIALOG_ID_ONLINE_PASSWORD = "SettingsDeveloperPresenter.DIALOG_ID_ONLINE_PASSWORD"
    }

    interface AddOn {

        fun getString(@StringRes stringRes: Int): String

        fun getString(@StringRes stringRes: Int, value: Int): String

        fun getString(@StringRes stringRes: Int, value: String): String
    }
}
