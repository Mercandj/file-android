@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.settings_storage

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.mercandalli.android.apps.files.R
import com.mercandalli.android.apps.files.main.ApplicationGraph

class SettingsStorageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr),
    SettingsStorageContract.Screen {

    private val view = LayoutInflater.from(context).inflate(R.layout.view_settings_storage, this)
    private val storageSection: CardView = view.findViewById(R.id.view_settings_storage_section)
    private val storageLocalRow: View = view.findViewById(R.id.view_settings_storage_local_row)
    private val localLabel: TextView = view.findViewById(R.id.view_settings_storage_local_label)
    private val localSubLabel: TextView = view.findViewById(R.id.view_settings_storage_local_sublabel)

    private val userAction = createUserAction()

    init {
        storageLocalRow.setOnClickListener {
            userAction.onStorageLocalRowClicked()
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

    override fun setTextPrimaryColorRes(colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        localLabel.setTextColor(color)
    }

    override fun setTextSecondaryColorRes(colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        localSubLabel.setTextColor(color)
    }

    override fun setSectionColor(colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        storageSection.setCardBackgroundColor(color)
    }

    override fun setLocalSubLabelText(text: String) {
        localSubLabel.text = text
    }

    private fun createUserAction(): SettingsStorageContract.UserAction = if (isInEditMode) {
        object : SettingsStorageContract.UserAction {
            override fun onAttached() {}
            override fun onDetached() {}
            override fun onStorageLocalRowClicked() {}
        }
    } else {
        val themeManager = ApplicationGraph.getThemeManager()
        val fileStorageStatsManager = ApplicationGraph.getFileStorageStatsManager()
        val screenManager = ApplicationGraph.getScreenManager()
        SettingsStoragePresenter(
            this,
            themeManager,
            fileStorageStatsManager,
            screenManager
        )
    }
}
