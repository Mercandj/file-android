@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.split_install

import android.content.Context
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.mercandalli.android.apps.files.R

class SplitInstallModule(
    private val context: Context
) {

    fun createSplitInstallManager(): SplitInstallManager {
        val splitInstallManager = SplitInstallManagerFactory.create(context)
        val splitUninstallStorage = createSplitUninstallStorage()
        return SplitInstallManagerImpl(
            splitInstallManager,
            splitUninstallStorage,
            context.getString(R.string.module_app_search_dynamic)
        )
    }

    private fun createSplitUninstallStorage(): SplitUninstallStorage {
        val sharedPreferences = context.getSharedPreferences(
            SplitUninstallStorageImpl.PREFERENCE_NAME,
            Context.MODE_PRIVATE
        )
        return SplitUninstallStorageImpl(
            sharedPreferences
        )
    }
}
