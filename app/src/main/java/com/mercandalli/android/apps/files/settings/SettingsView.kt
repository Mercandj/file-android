package com.mercandalli.android.apps.files.settings

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
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

    private val view = View.inflate(context, R.layout.view_settings, this)
    private val versionTitle: TextView = view.findViewById(R.id.view_settings_version_title)
    private val versionSubtitle: TextView = view.findViewById(R.id.view_settings_version_subtitle)
    private val themeCheckBox: CheckBox = view.findViewById(R.id.view_settings_theme)
    private val versionCard: CardView = view.findViewById(R.id.view_settings_version_section)
    private val themeCard: CardView = view.findViewById(R.id.view_settings_theme_section)
    private val themeLabel: TextView = view.findViewById(R.id.view_settings_theme_label)
    private val themeSubLabel: TextView = view.findViewById(R.id.view_settings_theme_sublabel)
    private val userAction = createUserAction()

    init {
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
        versionTitle.text = context.getString(R.string.view_settings_version, versionName)
    }

    override fun setThemeCheckboxChecked(checked: Boolean) {
        themeCheckBox.isChecked = checked
    }

    override fun setCardBackgroundColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        versionCard.setCardBackgroundColor(color)
        themeCard.setCardBackgroundColor(color)
    }

    override fun setTitlesTextColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        versionTitle.setTextColor(color)
        themeLabel.setTextColor(color)
    }

    override fun setSubtitlesTextColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        versionSubtitle.setTextColor(color)
        themeSubLabel.setTextColor(color)
    }

    private fun createUserAction() = if (isInEditMode) {
        object : SettingsContract.UserAction {
            override fun onAttached() {}
            override fun onDetached() {}
            override fun onRateClicked() {}
            override fun onTeamAppsClicked() {}
            override fun onThemeRowClicked(checked: Boolean) {}
            override fun onThemeCheckboxCheckedChange(checked: Boolean) {}
        }
    } else {
        val versionManager = ApplicationGraph.getVersionManager()
        val themeManager = ApplicationGraph.getThemeManager()
        SettingsPresenter(
                this,
                versionManager,
                themeManager
        )
    }
}