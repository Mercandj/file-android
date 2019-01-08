@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.selection_dialog

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.mercandalli.android.apps.files.R

class SelectionDialogActivity :
    Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection_dialog)
    }

    companion object {

        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, SelectionDialogActivity::class.java)
            if (context !is Activity) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            context.startActivity(intent)
        }
    }
}
