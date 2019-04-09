package com.mercandalli.android.sdk.files.api.internal

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import androidx.annotation.RequiresApi
import com.mercandalli.android.sdk.files.api.PermissionManager
import com.mercandalli.android.sdk.files.api.PermissionRequestAddOn
import java.io.Closeable

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
        try {
            val childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(
                externalStorageUri,
                DocumentsContract.getTreeDocumentId(externalStorageUri)
            )
            val cursor = contentResolver.query(
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
            while (cursor.moveToNext()) {
                length++
            }
            closeSilently(cursor)
            return length > 2
        } catch (e: SecurityException) {
            return false
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
