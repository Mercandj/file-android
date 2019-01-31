package com.mercandalli.android.apps.files.update

interface UpdateManager {

    fun isFirstRunAfterUpdate(): Boolean

    fun isFirstRun(): Boolean
}
