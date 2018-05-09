package com.mercandalli.android.apps.files.settings

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.mercandalli.android.apps.files.R

class SettingsView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), SettingsContract.Screen {

    init {
        View.inflate(context, R.layout.view_settings, this)
    }
}