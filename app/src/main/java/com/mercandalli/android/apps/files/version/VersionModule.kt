package com.mercandalli.android.apps.files.version

import android.content.Context

class VersionModule(
        private val context: Context
) {

    fun provideVersionManager(): VersionManager {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        val addOn = object : VersionManagerImpl.AddOn {
            override fun getVersionName(): String {
                return packageInfo.versionName
            }
        }
        return VersionManagerImpl(
                addOn
        )
    }
}