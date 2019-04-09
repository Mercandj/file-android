package com.mercandalli.android.sdk.files.api.internal

import android.content.Context
import android.os.Build
import com.mercandalli.android.sdk.files.api.FileScopedStorageManager
import com.mercandalli.android.sdk.files.api.PermissionManager
import com.mercandalli.android.sdk.files.api.PermissionRequestAddOn

internal class PermissionModule(
    private val context: Context,
    private val fileScopedStorageManager: FileScopedStorageManager,
    private val permissionRequestAddOn: PermissionRequestAddOn
) {

    fun createPermissionManager(): PermissionManager {
        val permissionManagerNonScoped = PermissionManagerNonScopedImpl(
            context,
            permissionRequestAddOn
        )
        if (
            fileScopedStorageManager.isScopedStorage() &&
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        ) {
            return PermissionManagerScopedImpl(
                context,
                permissionManagerNonScoped,
                permissionRequestAddOn
            )
        }
        return permissionManagerNonScoped
    }
}
