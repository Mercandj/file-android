@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.search

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.mercandalli.android.apps.files.R

class SearchActivity :
    Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
    }

    companion object {

        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, SearchActivity::class.java)
            if (context !is Activity) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            context.startActivity(intent)
        }
    }
}
