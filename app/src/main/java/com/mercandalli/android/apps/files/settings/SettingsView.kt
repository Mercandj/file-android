package com.mercandalli.android.apps.files.settings

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.mercandalli.android.apps.files.R

class SettingsView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), SettingsContract.Screen {

    init {
        View.inflate(context, R.layout.view_settings, this)
        val userAction = createUserAction()
        findViewById<View>(R.id.view_settings_rate).setOnClickListener {
            userAction.onRateClicked()
        }
        findViewById<View>(R.id.view_settings_team_apps).setOnClickListener {
            userAction.onTeamAppsClicked()
        }
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

    private fun createUserAction(): SettingsContract.UserAction {
        if (isInEditMode) {
            return object : SettingsContract.UserAction {
                override fun onRateClicked() {}
                override fun onTeamAppsClicked() {}
            }
        }
        return SettingsPresenter(this)
    }
}