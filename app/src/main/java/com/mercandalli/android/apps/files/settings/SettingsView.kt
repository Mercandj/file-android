package com.mercandalli.android.apps.files.settings

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.CheckBox
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import com.mercandalli.android.apps.files.R
import com.mercandalli.android.apps.files.main.ApplicationGraph

class SettingsView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr), SettingsContract.Screen {

    private val version: TextView
    private val themeCheckBox: CheckBox
    private val userAction: SettingsContract.UserAction

    init {
        View.inflate(context, R.layout.view_settings, this)
        version = findViewById(R.id.view_settings_version_name)
        themeCheckBox = findViewById(R.id.view_settings_theme)
        userAction = createUserAction()
        findViewById<View>(R.id.view_settings_rate).setOnClickListener {
            userAction.onRateClicked()
        }
        findViewById<View>(R.id.view_settings_team_apps).setOnClickListener {
            userAction.onTeamAppsClicked()
        }
        findViewById<View>(R.id.view_settings_theme_row).setOnClickListener {
            userAction.onThemeRowClicked(!themeCheckBox.isChecked)
        }
        themeCheckBox.setOnCheckedChangeListener { _, isChecked ->
            userAction.onThemeCheckboxCheckedChange(isChecked)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        userAction.onAttached()
    }

    override fun onDetachedFromWindow() {
        userAction.onDetached()
        super.onDetachedFromWindow()
    }

    override fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        try {
            context.startActivity(intent)
        } catch (exception: ActivityNotFoundException) {
            Toast.makeText(context, R.string.oops_something_went_wrong, Toast.LENGTH_SHORT).show()
        }
    }

    override fun showVersionName(versionName: String) {
        version.text = context.getString(R.string.view_settings_version, versionName)
    }

    override fun setThemeCheckboxChecked(checked: Boolean) {
        themeCheckBox.isChecked = checked
    }

    private fun createUserAction(): SettingsContract.UserAction {
        if (isInEditMode) {
            return object : SettingsContract.UserAction {
                override fun onAttached() {}
                override fun onDetached() {}
                override fun onRateClicked() {}
                override fun onTeamAppsClicked() {}
                override fun onThemeRowClicked(checked: Boolean) {}
                override fun onThemeCheckboxCheckedChange(checked: Boolean) {}
            }
        }
        val versionManager = ApplicationGraph.getVersionManager()
        val themeManager = ApplicationGraph.getThemeManager()
        return SettingsPresenter(
                this,
                versionManager,
                themeManager
        )
    }
}