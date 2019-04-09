@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.android_q

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent.ACTION_OPEN_DOCUMENT_TREE
import android.util.Log
import android.provider.DocumentsContract
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.mercandalli.android.apps.files.R
import com.mercandalli.android.apps.files.activity.ActivityExtension.bind
import com.mercandalli.android.apps.files.main.ApplicationGraph
import java.io.Closeable

class AndroidQActivity : AppCompatActivity() {

    private val askPermission: View by bind(R.id.activity_main_q_ask_permission)
    private val refresh: View by bind(R.id.activity_main_q_refresh)
    private val files: TextView by bind(R.id.activity_main_q_files)
    private var text = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.mercandalli.android.apps.files.R.layout.activity_main_q)
        askPermission.setOnClickListener {
            val intent = Intent(ACTION_OPEN_DOCUMENT_TREE)
            startActivityForResult(intent, REQUEST_CODE_OPEN_DIRECTORY)
        }
        refresh.setOnClickListener {
            updateScreen()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                updateDirectoryEntries(
                    Uri.parse("content://com.android.externalstorage.documents/tree/primary%3A")
                )
            }
        }
        updateScreen()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_OPEN_DIRECTORY && resultCode == Activity.RESULT_OK) {
            Log.d("jm/debug", "data: $data")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                updateDirectoryEntries(data!!.data!!)
            }
        }
    }

    @RequiresApi(21)
    private fun updateDirectoryEntries(uri: Uri) {
        val docUri = DocumentsContract.buildDocumentUriUsingTree(
            uri,
            DocumentsContract.getTreeDocumentId(uri)
        )
        val childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(
            uri,
            DocumentsContract.getTreeDocumentId(uri)
        )

        val docCursor = contentResolver.query(
            docUri,
            arrayOf(
                DocumentsContract.Document.COLUMN_DISPLAY_NAME,
                DocumentsContract.Document.COLUMN_MIME_TYPE
            ),
            null,
            null,
            null
        )
        try {
            while (docCursor!!.moveToNext()) {
                Log.d(
                    "jm/debug",
                    "uri$uri, " +
                        "found doc =" + docCursor.getString(0) + ", " +
                        "mime=" + docCursor.getString(1))
            }
        } finally {
            closeSilently(docCursor)
        }

        val childCursor = contentResolver.query(
            childrenUri,
            arrayOf(
                DocumentsContract.Document.COLUMN_DOCUMENT_ID,
                DocumentsContract.Document.COLUMN_DISPLAY_NAME,
                DocumentsContract.Document.COLUMN_MIME_TYPE,
                DocumentsContract.Document.COLUMN_SIZE
            ),
            null,
            null,
            null
        )
        try {
            val directoryEntries = ArrayList<DirectoryEntry>()
            while (childCursor!!.moveToNext()) {
                Log.d(
                    "jm/debug",
                    "found child=" + childCursor.getString(0) + ", mime=" + childCursor
                        .getString(1))
                val entry = DirectoryEntry()
                entry.documentId = childCursor.getString(0)
                entry.fileName = childCursor.getString(1)
                entry.mimeType = childCursor.getString(2)
                entry.size = childCursor.getLong(3)
                entry.uri = DocumentsContract.buildDocumentUriUsingTree(
                    uri,
                    entry.documentId
                )
                directoryEntries.add(entry)
                text = directoryEntries.joinToString(",\n")
                updateScreen()
            }
        } finally {
            closeSilently(childCursor)
        }
    }

    private fun updateScreen() {
        val permissionManager = ApplicationGraph.getPermissionManager()
        val hasStoragePermission = permissionManager.hasStoragePermission()
        files.text = "hasStoragePermission: $hasStoragePermission\n\n$text"
    }

    data class DirectoryEntry(
        var documentId: String? = null,
        var fileName: String? = null,
        var mimeType: String? = null,
        var size: Long? = null,
        var uri: Uri? = null
    )

    companion object {

        private const val REQUEST_CODE_OPEN_DIRECTORY = 1

        fun start(context: Context) {
            val intent = Intent(context, AndroidQActivity::class.java)
            if (context !is Activity) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            context.startActivity(intent)
        }

        private fun closeSilently(vararg xs: Closeable?) {
            for (x in xs) {
                try {
                    x?.close()
                } catch (ignored: Throwable) {
                }
            }
        }
    }
}
