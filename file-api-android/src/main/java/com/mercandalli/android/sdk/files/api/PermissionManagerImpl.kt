package com.mercandalli.android.sdk.files.api

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

class PermissionManagerImpl(
        private val context: Context,
        private val permissionRequestAddOn: PermissionRequestAddOn
) : PermissionManager {

    override fun shouldRequestStoragePermission(): Boolean {
        val hasPermission = checkStoragePermission(context)
        if (!hasPermission) {
            permissionRequestAddOn.requestStoragePermission()
        }
        return !hasPermission
    }

    companion object {

        /**
         * Check a permission.
         *
         * @param context The current [Context].
         * @return True if all the permissions are [PackageManager.PERMISSION_GRANTED].
         */
        @JvmStatic
        fun checkStoragePermission(context: Context): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val checkSelfPermission = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
            return true
        }
    }
}