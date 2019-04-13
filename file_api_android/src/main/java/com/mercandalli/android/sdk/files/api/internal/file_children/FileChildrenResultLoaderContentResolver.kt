package com.mercandalli.android.sdk.files.api.internal.file_children

import android.content.ContentResolver
import android.net.Uri
import android.provider.DocumentsContract
import androidx.annotation.RequiresApi
import com.mercandalli.sdk.files.api.File
import com.mercandalli.sdk.files.api.FileChildrenResult

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
        childCursor.use { cursor ->
            val files = ArrayList<File>()
            while (cursor!!.moveToNext()) {
                val documentId = cursor.getString(0)
                val name = cursor.getString(1)
                val mimeType = cursor.getString(2)
                val size = cursor.getLong(3)
                val lastModified = cursor.getLong(4)
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
        }
    }
}
