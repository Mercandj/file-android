@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.file_details

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mercandalli.android.apps.files.R
import com.mercandalli.android.apps.files.activity.ActivityExtension.bind
import com.mercandalli.android.apps.files.file_provider.FileProvider
import com.mercandalli.android.apps.files.main.ApplicationGraph
import java.lang.IllegalStateException

class FileDetailsActivity :
    AppCompatActivity(),
    FileDetailsContract.Screen {

    private val path: TextView by bind(R.id.activity_file_details_path)
    private val name: TextView by bind(R.id.activity_file_details_name)
    private val size: TextView by bind(R.id.activity_file_details_size)
    private val share: ImageView by bind(R.id.activity_file_details_share)
    private val userAction = createUserAction()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_details)

        val extras = intent!!.extras!!
        val path = extras.getString(EXTRA_PATH)!!
        val fileProviderString = extras.getString(EXTRA_FILE_PROVIDER)!!
        val fileProvider = convertStringToFileProvider(fileProviderString)
        when (fileProvider) {
            FileProvider.Local -> {
                val fileManager = ApplicationGraph.getFileManager()
                val fileChildrenManager = ApplicationGraph.getFileChildrenManager()
                val fileShareManager = ApplicationGraph.getFileShareManager()
                val fileSizeManager = ApplicationGraph.getFileSizeManager()
                userAction.onSetFileManagers(
                    fileManager,
                    fileChildrenManager,
                    fileShareManager,
                    fileSizeManager
                )
            }
            FileProvider.Online -> {
                val fileManager = ApplicationGraph.getFileOnlineManager()
                val fileChildrenManager = ApplicationGraph.getFileOnlineChildrenManager()
                val fileShareManager = ApplicationGraph.getFileOnlineShareManager()
                val fileSizeManager = ApplicationGraph.getFileOnlineSizeManager()
                ApplicationGraph.getFileShareManager()
                userAction.onSetFileManagers(
                    fileManager,
                    fileChildrenManager,
                    fileShareManager,
                    fileSizeManager
                )
            }
        }
        share.setOnClickListener { userAction.onSharedClicked() }
        userAction.onCreate(path)
    }

    override fun onDestroy() {
        userAction.onDestroy()
        super.onDestroy()
    }

    override fun setPathText(text: String) {
        path.text = text
    }

    override fun setNameText(text: String) {
        name.text = text
    }

    override fun setSizeText(text: String) {
        size.text = text
    }

    override fun showShareButton() {
        share.visibility = View.VISIBLE
    }

    override fun hideShareButton() {
        share.visibility = View.GONE
    }

    private fun createUserAction(): FileDetailsContract.UserAction {
        val fileManager = ApplicationGraph.getFileManager()
        val fileChildrenManager = ApplicationGraph.getFileChildrenManager()
        val fileShareManager = ApplicationGraph.getFileShareManager()
        val fileSizeManager = ApplicationGraph.getFileSizeManager()
        return FileDetailsPresenter(
            this,
            fileManager,
            fileChildrenManager,
            fileShareManager,
            fileSizeManager
        )
    }

    companion object {

        private const val EXTRA_PATH = "EXTRA_PATH"
        private const val EXTRA_FILE_PROVIDER = "EXTRA_FILE_PROVIDER"

        @JvmStatic
        fun start(
            context: Context,
            path: String,
            fileProvider: FileProvider
        ) {
            val intent = Intent(context, FileDetailsActivity::class.java)
            if (context !is Activity) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            intent.putExtra(EXTRA_PATH, path)
            val fileProviderString = convertFileProviderToString(fileProvider)
            intent.putExtra(EXTRA_FILE_PROVIDER, fileProviderString)
            context.startActivity(intent)
        }

        private fun convertFileProviderToString(fileProvider: FileProvider) = when (fileProvider) {
            FileProvider.Local -> "local"
            FileProvider.Online -> "online"
        }

        private fun convertStringToFileProvider(fileProviderString: String) = when (fileProviderString) {
            "local" -> FileProvider.Local
            "online" -> FileProvider.Online
            else -> throw IllegalStateException("Wrong fileProviderString: $fileProviderString")
        }
    }
}
