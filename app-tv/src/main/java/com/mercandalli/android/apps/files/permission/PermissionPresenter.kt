package com.mercandalli.android.apps.files.permission

class PermissionPresenter(
    private val screen: PermissionContract.Screen
) : PermissionContract.UserAction {

    override fun onPermissionAllowClicked() {
        screen.requestStoragePermission()
    }
}
