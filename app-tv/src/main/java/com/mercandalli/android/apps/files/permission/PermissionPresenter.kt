package com.mercandalli.android.apps.files.permission

import com.mercandalli.sdk.files.api.FileChildrenManager

class PermissionPresenter(
    private val screen: PermissionContract.Screen,
    private val fileChildrenManager: FileChildrenManager,
    private val initialPath: String
) : PermissionContract.UserAction {

    override fun onPermissionAllowClicked() {
        screen.requestStoragePermission()
    }

    override fun onPermissionSystemGranted() {
        fileChildrenManager.loadFileChildren(initialPath, true)
        screen.quit()
    }
}
