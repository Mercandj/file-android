package com.mercandalli.android.apps.files.file_online_upload

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mercandalli.android.apps.files.R
import com.mercandalli.android.apps.files.activity.ActivityExtension.bind
import com.mercandalli.android.apps.files.main.ApplicationGraph

class FileOnlineUploadActivity : AppCompatActivity(),
        FileOnlineUploadContract.Screen {

    private val upload: View by bind(R.id.activity_file_online_upload_upload)
    private val userAction = createUserAction()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_online_upload)
        upload.setOnClickListener {
            userAction.onUploadClicked()
        }
    }

    override fun quit() {
        finish()
    }

    private fun createUserAction(): FileOnlineUploadContract.UserAction {
        val fileOnlineUploadManager = ApplicationGraph.getFileOnlineUploadManager()
        return FileOnlineUploadPresenter(
                this,
                fileOnlineUploadManager
        )
    }

    companion object {

        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, FileOnlineUploadActivity::class.java)
            if (context !is Activity) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            context.startActivity(intent)
        }
    }
}