package com.mercandalli.android.apps.files.permission

interface PermissionContract {

    interface UserAction {

        fun onPermissionAllowClicked()

        fun onPermissionSystemGranted()
    }

    interface Screen {

        fun requestStoragePermission()

        fun quit()
    }
}
