@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.settings_store

import android.app.Activity
import android.content.Context
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

class SettingsStoreView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr),
    SettingsStoreContract.Screen {

    private val view = LayoutInflater.from(context).inflate(R.layout.view_settings_store, this)

    private val themeRow: View = view.findViewById(R.id.view_settings_store_row)
    private val themeSection: CardView = view.findViewById(R.id.view_settings_store_section)
    private val themeSectionLabel: TextView = view.findViewById(R.id.view_settings_store_section_label)
    private val themeLabel: TextView = view.findViewById(R.id.view_settings_store_label)
    private val themeSubLabel: TextView = view.findViewById(R.id.view_settings_store_sublabel)

    private val userAction = createUserAction()

    init {
        orientation = LinearLayout.VERTICAL
        themeRow.setOnClickListener {
            val activity = context as Activity
            userAction.onRowClicked(activity)
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

    override fun setSectionColor(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        themeSection.setCardBackgroundColor(color)
    }

    override fun setTextPrimaryColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        themeLabel.setTextColor(color)
    }

    override fun setTextSecondaryColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        themeSectionLabel.setTextColor(color)
        themeSubLabel.setTextColor(color)
    }

    private fun createUserAction(): SettingsStoreContract.UserAction = if (isInEditMode) {
        object : SettingsStoreContract.UserAction {
            override fun onAttached() {}
            override fun onDetached() {}
            override fun onRowClicked(activity: Activity) {}
        }
    } else {
        val productManager = ApplicationGraph.getProductManager()
        val themeManager = ApplicationGraph.getThemeManager()
        SettingsStorePresenter(
            this,
            productManager,
            themeManager
        )
    }
}
