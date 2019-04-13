package com.mercandalli.android.sdk.files.api.internal

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.DocumentsContract
import androidx.annotation.RequiresApi
import com.mercandalli.android.sdk.files.api.PermissionManager
import com.mercandalli.android.sdk.files.api.PermissionRequestAddOn

@RequiresApi(21)
class PermissionManagerScopedImpl(
    private val context: Context,
    private val permissionManagerNonScoped: PermissionManager,
    private val permissionRequestAddOn: PermissionRequestAddOn
) : PermissionManager {

    private val contentResolver by lazy { context.contentResolver }
    private val externalStorageUri by lazy { Uri.parse("content://com.android.externalstorage.documents/tree/primary%3A") }

    override fun hasStoragePermission(): Boolean {
        if (permissionManagerNonScoped.hasStoragePermission()) {
            return true
        }
        return hasPermission()
    }

    override fun requestStoragePermissionIfRequired(): Boolean {
        if (!hasStoragePermission()) {
            permissionRequestAddOn.requestStoragePermission()
            return true
        }
        return false
    }

    private fun hasPermission(): Boolean {
        var cursor: Cursor? = null
        try {
            val childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(
                externalStorageUri,
                DocumentsContract.getTreeDocumentId(externalStorageUri)
            )
            cursor = contentResolver.query(
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
            var length = 0
            while (cursor?.moveToNext() == true) {
                length++
            }
            return length > 2
        } catch (e: SecurityException) {
            return false
        } finally {
            cursor?.close()
        }
    }
}
