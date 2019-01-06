@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.settings_theme

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

class SettingsThemeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr),
    SettingsThemeContract.Screen {

    private val view = LayoutInflater.from(context).inflate(R.layout.view_settings_theme, this)

    private val themeRow: View = view.findViewById(R.id.view_settings_theme_row)
    private val themeSection: CardView = view.findViewById(R.id.view_settings_theme_section)
    private val themeSectionLabel: TextView = view.findViewById(R.id.view_settings_theme_section_label)
    private val themeLabel: TextView = view.findViewById(R.id.view_settings_theme_label)
    private val themeSubLabel: TextView = view.findViewById(R.id.view_settings_theme_sublabel)
    private val themeCheckBox: CheckBox = view.findViewById(R.id.view_settings_theme)

    private val userAction = createUserAction()

    init {
        orientation = LinearLayout.VERTICAL
        themeRow.setOnClickListener {
            val isChecked = !themeCheckBox.isChecked
            themeCheckBox.isChecked = isChecked
            userAction.onDarkThemeCheckBoxCheckedChanged(isChecked)
        }
        themeCheckBox.setOnCheckedChangeListener { _, isChecked ->
            userAction.onDarkThemeCheckBoxCheckedChanged(isChecked)
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

    override fun setDarkThemeCheckBox(checked: Boolean) {
        themeCheckBox.isChecked = checked
    }

    override fun setSectionColor(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        themeSection.setCardBackgroundColor(color)
    }

    override fun setTextPrimaryColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        themeLabel.setTextColor(color)
        themeCheckBox.setTextColor(color)
    }

    override fun setTextSecondaryColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        themeSectionLabel.setTextColor(color)
        themeSubLabel.setTextColor(color)
    }

    private fun createUserAction(): SettingsThemeContract.UserAction = if (isInEditMode) {
        object : SettingsThemeContract.UserAction {
            override fun onAttached() {}
            override fun onDetached() {}
            override fun onDarkThemeCheckBoxCheckedChanged(isChecked: Boolean) {}
        }
    } else {
        val themeManager = ApplicationGraph.getThemeManager()
        SettingsThemePresenter(
            this,
            themeManager
        )
    }
}
