package com.mercandalli.android.sdk.files.api

interface PermissionManager {

    fun shouldRequestStoragePermission(): Boolean
}
