package com.mercandalli.android.apps.files.settings_android_q

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.mercandalli.android.apps.files.R
import com.mercandalli.android.apps.files.android_q.AndroidQActivity
import com.mercandalli.android.apps.files.main.ApplicationGraph

class SettingsAndroidQView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val view = LayoutInflater.from(context).inflate(R.layout.view_settings_android_q, this)
    private val storageSection: CardView = view.findViewById(R.id.view_settings_android_q_section)
    private val storageLocalRow: View = view.findViewById(R.id.view_settings_android_q_local_row)
    private val localLabel: TextView = view.findViewById(R.id.view_settings_android_q_local_label)
    private val localSubLabel: TextView = view.findViewById(R.id.view_settings_android_q_local_sublabel)

    private val userAction = createUserAction()

    init {
        storageLocalRow.setOnClickListener {
            userAction.onRowClicked()
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

    private fun createScreen() = object : SettingsAndroidQContract.Screen {

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

        override fun startAndroidQActivity() {
            AndroidQActivity.start(context)
        }
    }

    private fun createUserAction(): SettingsAndroidQContract.UserAction = if (isInEditMode) {
        object : SettingsAndroidQContract.UserAction {
            override fun onAttached() {}
            override fun onDetached() {}
            override fun onRowClicked() {}
        }
    } else {
        val screen = createScreen()
        val screenManager = ApplicationGraph.getScreenManager()
        val themeManager = ApplicationGraph.getThemeManager()
        SettingsAndroidQPresenter(
            screen,
            screenManager,
            themeManager
        )
    }
}
