package com.mercandalli.android.apps.files.settings_about

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import com.mercandalli.android.apps.files.R
import com.mercandalli.android.apps.files.main.ApplicationGraph

class SettingsAboutView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr), SettingsAboutContract.Screen {

    private val view = View.inflate(context, R.layout.view_settings_about, this)
    private val versionTitle: TextView = view.findViewById(R.id.view_settings_version_title)
    private val versionSubtitle: TextView = view.findViewById(R.id.view_settings_version_subtitle)
    private val versionCard: androidx.cardview.widget.CardView = view.findViewById(R.id.view_settings_version_section)
    private val userAction = createUserAction()

    init {
        findViewById<View>(R.id.view_settings_rate).setOnClickListener {
            userAction.onRateClicked()
        }
        findViewById<View>(R.id.view_settings_team_apps).setOnClickListener {
            userAction.onTeamAppsClicked()
        }
        versionTitle.setOnClickListener {
            userAction.onVersionClicked()
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

    override fun setCardBackgroundColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        versionCard.setCardBackgroundColor(color)
    }

    override fun setTitlesTextColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        versionTitle.setTextColor(color)
    }

    override fun setSubtitlesTextColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        versionSubtitle.setTextColor(color)
    }

    override fun showSnackbar(@StringRes messageStringRes: Int, duration: Int) {
        val activity = context as Activity
        val view = activity.window.decorView.findViewById<View>(android.R.id.content)
        Snackbar.make(view, messageStringRes, duration).show()
    }

    private fun createUserAction() = if (isInEditMode) {
        object : SettingsAboutContract.UserAction {
            override fun onAttached() {}
            override fun onDetached() {}
            override fun onRateClicked() {}
            override fun onTeamAppsClicked() {}
            override fun onVersionClicked() {}
        }
    } else {
        val versionManager = ApplicationGraph.getVersionManager()
        val themeManager = ApplicationGraph.getThemeManager()
        val dialogManager = ApplicationGraph.getDialogManager()
        val developerManager = ApplicationGraph.getDeveloperManager()
        val hashManager = ApplicationGraph.getHashManager()
        val addOn = object : SettingsAboutPresenter.AddOn {
            override fun getCurrentTimeMillis() = System.currentTimeMillis()
        }
        SettingsAboutPresenter(
                this,
                versionManager,
                themeManager,
                dialogManager,
                developerManager,
                hashManager,
                addOn
        )
    }
}