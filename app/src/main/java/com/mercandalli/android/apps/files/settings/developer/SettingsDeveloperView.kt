package com.mercandalli.android.apps.files.settings.developer

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.mercandalli.android.apps.files.main.ApplicationGraph
import com.mercandalli.android.apps.files.R

class SettingsDeveloperView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr),
        SettingsDeveloperContract.Screen {

    private val view = LayoutInflater.from(context).inflate(R.layout.view_settings_developer, this)

    private val developerSectionLabel: View = view.findViewById(R.id.view_settings_developer_label)
    private val developerSection: CardView = view.findViewById(R.id.view_settings_developer_section)
    private val developerActivation: CheckBox = view.findViewById(R.id.view_settings_developer_activation)
    private val developerActivationLabel: TextView = view.findViewById(R.id.view_settings_developer_activation_label)
    private val developerActivationSubLabel: TextView = view.findViewById(R.id.view_settings_developer_activation_sublabel)
    private val developerRemoteCountryLabel: TextView = view.findViewById(R.id.view_settings_developer_online_label)
    private val developerRemoteCountrySubLabel: TextView = view.findViewById(R.id.view_settings_developer_online_sublabel)

    private val userAction = createUserAction()

    init {
        orientation = LinearLayout.VERTICAL

        findViewById<View>(R.id.view_settings_developer_online_row).setOnClickListener {
            userAction.onVideoStartedCountClicked()
        }
        findViewById<View>(R.id.view_settings_developer_activation_row).setOnClickListener {
            userAction.onActivationRowClicked()
        }
        developerActivation.setOnCheckedChangeListener { _, isChecked ->
            userAction.onDeveloperActivationCheckChanged(isChecked)
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

    override fun setTextPrimaryColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        developerActivationLabel.setTextColor(color)
        developerRemoteCountryLabel.setTextColor(color)
    }

    override fun setTextSecondaryColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        developerActivationSubLabel.setTextColor(color)
        developerRemoteCountrySubLabel.setTextColor(color)
    }

    override fun setSectionColor(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        developerSection.setCardBackgroundColor(color)
    }

    override fun setDeveloperSectionLabelVisibility(visibility: Int) {
        developerSectionLabel.visibility = visibility
    }

    override fun setDeveloperSectionVisibility(visibility: Int) {
        developerSection.visibility = visibility
    }

    override fun setDeveloperActivationChecked(checked: Boolean) {
        developerActivation.isChecked = checked
    }

    override fun setDeveloperRemoteCountrySubLabelText(text: String) {
        developerRemoteCountrySubLabel.text = text
    }

    private fun createUserAction(): SettingsDeveloperContract.UserAction = if (isInEditMode) {
        object : SettingsDeveloperContract.UserAction {
            override fun onAttached() {}
            override fun onDetached() {}
            override fun onVideoStartedCountClicked() {}
            override fun onActivationRowClicked() {}
            override fun onDeveloperActivationCheckChanged(checked: Boolean) {}
        }
    } else {
        val themeManager = ApplicationGraph.getThemeManager()
        val settingsManager = ApplicationGraph.getSettingsManager()
        val addOn = object : SettingsDeveloperPresenter.AddOn {
            override fun getString(stringRes: Int) = resources.getString(stringRes)
            override fun getString(stringRes: Int, value: Int) = resources.getString(stringRes, value)
            override fun getString(stringRes: Int, value: String) = resources.getString(stringRes, value)
        }
        SettingsDeveloperPresenter(
                this,
                themeManager,
                settingsManager,
                addOn
        )
    }
}