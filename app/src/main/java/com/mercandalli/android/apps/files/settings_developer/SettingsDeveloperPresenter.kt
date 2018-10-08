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
    private val appDeveloperListener = createAppDeveloperListener()

    override fun onAttached() {
        themeManager.registerThemeListener(themeListener)
        developerManager.registerDeveloperModeListener(appDeveloperListener)
        updateTheme()
        syncDeveloperSection()
    }

    override fun onDetached() {
        themeManager.unregisterThemeListener(themeListener)
    }

    override fun onOnlineRowClicked() {

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
        val onlineSubLabel = if (login == null) {
            addOn.getString(R.string.view_settings_developer_online_sublabel_not_logged)
        } else {
            addOn.getString(R.string.view_settings_developer_online_sublabel, login)
        }
        screen.setOnlineSubLabelText(onlineSubLabel)

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

    interface AddOn {

        fun getString(@StringRes stringRes: Int): String

        fun getString(@StringRes stringRes: Int, value: Int): String

        fun getString(@StringRes stringRes: Int, value: String): String
    }
}