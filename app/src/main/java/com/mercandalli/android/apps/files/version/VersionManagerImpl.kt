package com.mercandalli.android.apps.files.version

class VersionManagerImpl(
    private val addOn: AddOn
) : VersionManager {

    override fun getVersionName() = addOn.getVersionName()

    interface AddOn {
        fun getVersionName(): String
    }
}
