package com.mercandalli.android.apps.files.settings

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
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

    private val versionTitle: TextView
    private val versionSubtitle: TextView
    private val themeCheckBox: CheckBox
    private val versionCard: CardView
    private val themeCard: CardView
    private val themeLabel: TextView
    private val themeSublabel: TextView
    private val userAction: SettingsContract.UserAction

    init {
        View.inflate(context, R.layout.view_settings, this)
        versionTitle = findViewById(R.id.view_settings_version_title)
        versionSubtitle = findViewById(R.id.view_settings_version_subtitle)
        themeCheckBox = findViewById(R.id.view_settings_theme)
        versionCard = findViewById(R.id.view_settings_version_section)
        themeCard = findViewById(R.id.view_settings_theme_section)
        themeLabel = findViewById(R.id.view_settings_theme_label)
        themeSublabel = findViewById(R.id.view_settings_theme_sublabel)
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
        versionTitle.text = context.getString(R.string.view_settings_version, versionName)
    }

    override fun setThemeCheckboxChecked(checked: Boolean) {
        themeCheckBox.isChecked = checked
    }

    override fun setCardBackgroundColorRes(cardBackgroundColorRes: Int) {
        val cardBackgroundColor = ContextCompat.getColor(context, cardBackgroundColorRes)
        versionCard.setCardBackgroundColor(cardBackgroundColor)
        themeCard.setCardBackgroundColor(cardBackgroundColor)
    }

    override fun setTitlesTextColorRes(titlesColorRes: Int) {
        val titlesColor = ContextCompat.getColor(context, titlesColorRes)
        versionTitle.setTextColor(titlesColor)
        themeLabel.setTextColor(titlesColor)
    }

    override fun setSubtitlesTextColorRes(subtitlesColorRes: Int) {
        val subtitlesColor = ContextCompat.getColor(context, subtitlesColorRes)
        versionSubtitle.setTextColor(subtitlesColor)
        themeSublabel.setTextColor(subtitlesColor)
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