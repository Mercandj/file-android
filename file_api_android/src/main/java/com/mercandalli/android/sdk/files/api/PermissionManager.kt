package com.mercandalli.android.sdk.files.api

interface PermissionManager {

    fun hasStoragePermission(): Boolean

    fun requestStoragePermissionIfRequired(): Boolean
}
