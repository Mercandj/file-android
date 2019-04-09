package com.mercandalli.android.apps.files.permission

interface PermissionContract {

    interface UserAction {

        fun onPermissionAllowClicked()
    }

    interface Screen {

        fun requestStoragePermission()

        fun requestScopedStoragePermission()
    }
}
