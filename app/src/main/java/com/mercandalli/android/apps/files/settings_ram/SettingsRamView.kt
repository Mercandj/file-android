package com.mercandalli.android.apps.files.settings_ram

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.mercandalli.android.apps.files.R
import com.mercandalli.android.apps.files.main.ApplicationGraph

class SettingsRamView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val view = LayoutInflater.from(context).inflate(R.layout.view_settings_ram, this)
    private val storageSection: CardView = view.findViewById(R.id.view_settings_ram_section)
    private val storageLocalRow: View = view.findViewById(R.id.view_settings_ram_local_row)
    private val localLabel: TextView = view.findViewById(R.id.view_settings_ram_local_label)
    private val localSubLabel: TextView = view.findViewById(R.id.view_settings_ram_local_sublabel)
    private val busy: TextView = view.findViewById(R.id.view_settings_ram_local_busy)
    private val total: TextView = view.findViewById(R.id.view_settings_ram_local_total)
    private val progress: ProgressBar = view.findViewById(R.id.view_settings_ram_local_progress)

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

    private fun createScreen() = object : SettingsRamContract.Screen {

        override fun setTextPrimaryColorRes(colorRes: Int) {
            val color = ContextCompat.getColor(context, colorRes)
            localLabel.setTextColor(color)
        }

        override fun setTextSecondaryColorRes(colorRes: Int) {
            val color = ContextCompat.getColor(context, colorRes)
            localSubLabel.setTextColor(color)
            busy.setTextColor(color)
            total.setTextColor(color)
        }

        override fun setSectionColor(colorRes: Int) {
            val color = ContextCompat.getColor(context, colorRes)
            storageSection.setCardBackgroundColor(color)
        }

        override fun setLocalSubLabelText(text: String) {
            localSubLabel.text = text
        }

        override fun setProgress(percent: Float) {
            progress.max = 1_000
            progress.progress = (percent * 1_000).toInt()
        }

        override fun setLocalBusy(text: String) {
            busy.text = text
        }

        override fun setLocalTotal(text: String) {
            total.text = text
        }
    }

    private fun createUserAction(): SettingsRamContract.UserAction = if (isInEditMode) {
        object : SettingsRamContract.UserAction {
            override fun onAttached() {}
            override fun onDetached() {}
            override fun onStorageLocalRowClicked() {}
        }
    } else {
        val screen = createScreen()
        val ramStatsManager = ApplicationGraph.getRamStatsManager()
        val screenManager = ApplicationGraph.getScreenManager()
        val themeManager = ApplicationGraph.getThemeManager()
        SettingsRamPresenter(
            screen,
            ramStatsManager,
            screenManager,
            themeManager
        )
    }
}
