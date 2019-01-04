@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.file_details

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mercandalli.android.apps.files.R
import com.mercandalli.android.apps.files.activity.ActivityExtension.bind

class FileDetailsActivity :
    AppCompatActivity(),
    FileDetailsContract.Screen {

    private val path: TextView by bind(R.id.activity_file_details_path)
    private val userAction = createUserAction()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_details)

        val path = intent!!.extras!!.getString(EXTRA_PATH)!!
        userAction.onCreate(path)
    }

    override fun setPathText(text: String) {
        path.text = text
    }

    private fun createUserAction(): FileDetailsContract.UserAction {
        return FileDetailsPresenter(
            this
        )
    }

    companion object {

        private const val EXTRA_PATH = "EXTRA_PATH"

        @JvmStatic
        fun start(
            context: Context,
            path: String
        ) {
            val intent = Intent(context, FileDetailsActivity::class.java)
            if (context !is Activity) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            intent.putExtra(EXTRA_PATH, path)
            context.startActivity(intent)
        }
    }
}
