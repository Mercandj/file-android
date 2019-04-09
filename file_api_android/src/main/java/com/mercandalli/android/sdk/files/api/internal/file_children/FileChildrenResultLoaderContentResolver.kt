package com.mercandalli.android.sdk.files.api.internal.file_children

import android.content.ContentResolver
import android.net.Uri
import android.provider.DocumentsContract
import androidx.annotation.RequiresApi
import com.mercandalli.sdk.files.api.File
import com.mercandalli.sdk.files.api.FileChildrenResult
import java.io.Closeable

@RequiresApi(21)
internal class FileChildrenResultLoaderContentResolver(
    private val contentResolver: ContentResolver
) : FileChildrenResultLoader {

    override fun loadFileChildrenSync(
        parentPath: String
    ): FileChildrenResult {
        val parentUri = Uri.parse(parentPath)
        val childrenUri = if (
            parentPath == "content://com.android.externalstorage.documents/tree/primary%3A"
        ) {
            DocumentsContract.buildChildDocumentsUriUsingTree(
                parentUri,
                DocumentsContract.getTreeDocumentId(parentUri)
            )
        } else {
            Uri.parse(
                "$parentPath/children"
            )
        }
        val childCursor = contentResolver.query(
            childrenUri,
            arrayOf(
                DocumentsContract.Document.COLUMN_DOCUMENT_ID,
                DocumentsContract.Document.COLUMN_DISPLAY_NAME,
                DocumentsContract.Document.COLUMN_MIME_TYPE,
                DocumentsContract.Document.COLUMN_SIZE,
                DocumentsContract.Document.COLUMN_LAST_MODIFIED
            ),
            null,
            null,
            null
        )
        try {
            val files = ArrayList<File>()
            while (childCursor!!.moveToNext()) {
                val documentId = childCursor.getString(0)
                val name = childCursor.getString(1)
                val mimeType = childCursor.getString(2)
                val size = childCursor.getLong(3)
                val lastModified = childCursor.getLong(4)
                val directory = mimeType == DocumentsContract.Document.MIME_TYPE_DIR
                val path = DocumentsContract.buildDocumentUriUsingTree(
                    parentUri,
                    documentId
                )
                val id = path.toString()
                val file = File.create(
                    id,
                    path.toString(),
                    parentPath,
                    directory,
                    name,
                    size,
                    lastModified
                )
                files.add(file)
            }
            return FileChildrenResult.createLoaded(
                parentPath,
                files
            )
        } finally {
            closeSilently(childCursor)
        }
    }

    companion object {

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
