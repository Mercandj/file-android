package com.mercandalli.android.apps.files.dialog

import android.content.Context
import android.view.ContextThemeWrapper
import com.mercandalli.android.apps.files.R

class DialogModule(
        private val context: Context
) {

    fun createDialogManager(): DialogManager {
        val context = ContextThemeWrapper(context, R.style.AppTheme)
        val addOn = object : DialogManagerImpl.AddOn {
            override fun startDialogActivity(dialogInput: DialogActivity.DialogInput) {
                DialogActivity.start(context, dialogInput)
            }

            override fun getString(stringRes: Int) = context.getString(stringRes)
        }
        return DialogManagerImpl(
                addOn
        )
    }
}