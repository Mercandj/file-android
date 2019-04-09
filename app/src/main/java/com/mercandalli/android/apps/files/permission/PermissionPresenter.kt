package com.mercandalli.android.apps.files.permission

import com.mercandalli.android.sdk.files.api.FileScopedStorageManager

class PermissionPresenter(
    private val screen: PermissionContract.Screen,
    private val fileScopedStorageManager: FileScopedStorageManager
) : PermissionContract.UserAction {

    override fun onPermissionAllowClicked() {
        if (fileScopedStorageManager.isScopedStorage()) {
            screen.requestScopedStoragePermission()
        } else {
            screen.requestStoragePermission()
        }
    }
}
