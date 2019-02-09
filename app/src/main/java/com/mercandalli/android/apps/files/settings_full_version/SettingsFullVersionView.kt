@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.settings_full_version

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.mercandalli.android.apps.files.main.ApplicationGraph
import com.mercandalli.android.apps.files.R

class SettingsFullVersionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr),
    SettingsFullVersionContract.Screen {

    private val view = LayoutInflater.from(context).inflate(R.layout.view_settings_full_version, this)

    private val row: View = view.findViewById(R.id.view_settings_full_version_row)
    private val section: CardView = view.findViewById(R.id.view_settings_full_version_section)
    private val sectionLabel: TextView = view.findViewById(R.id.view_settings_full_version_section_label)
    private val label: TextView = view.findViewById(R.id.view_settings_full_version_label)
    private val subLabel: TextView = view.findViewById(R.id.view_settings_full_version_sublabel)
    private val promotionGradient: View = view.findViewById(R.id.view_settings_full_version_promotion_gradient)

    private val userAction = createUserAction()

    init {
        orientation = LinearLayout.VERTICAL
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        userAction.onAttached()
    }

    override fun onDetachedFromWindow() {
        userAction.onDetached()
        super.onDetachedFromWindow()
    }

    override fun setSectionColor(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        section.setCardBackgroundColor(color)
    }

    override fun setTextPrimaryColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        label.setTextColor(color)
    }

    override fun setTextSecondaryColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        sectionLabel.setTextColor(color)
        subLabel.setTextColor(color)
    }

    override fun setPromotionGradient(dark: Boolean) {
        if (dark) {
            promotionGradient.setBackgroundResource(R.drawable.settings_store_gradient_dark_list)
        } else {
            promotionGradient.setBackgroundResource(R.drawable.settings_store_gradient_light_list)
        }
        val animationDrawable = promotionGradient.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(0)
        animationDrawable.setExitFadeDuration(2_000)
        animationDrawable.start()
    }

    private fun createUserAction(): SettingsFullVersionContract.UserAction = if (isInEditMode) {
        object : SettingsFullVersionContract.UserAction {
            override fun onAttached() {}
            override fun onDetached() {}
        }
    } else {
        val themeManager = ApplicationGraph.getThemeManager()
        SettingsFullVersionPresenter(
            this,
            themeManager
        )
    }
}
