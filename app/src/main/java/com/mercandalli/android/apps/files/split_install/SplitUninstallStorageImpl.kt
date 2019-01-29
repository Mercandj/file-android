@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.split_install

import android.content.SharedPreferences

class SplitUninstallStorageImpl(
    private val sharedPreferences: SharedPreferences
) : SplitUninstallStorage {

    private val names = HashSet<String>()

    init {
        val loadedNames = sharedPreferences.getStringSet(KEY, HashSet<String>())!!
        names.addAll(loadedNames)
    }

    override fun isUninstallTrigger(name: String): Boolean {
        return names.contains(name)
    }

    override fun setUninstallTrigger(name: String, trigger: Boolean) {
        if (trigger) {
            names.add(name)
        } else {
            names.remove(name)
        }
        sharedPreferences.edit().putStringSet(KEY, names).apply()
    }

    companion object {
        @JvmStatic
        val PREFERENCE_NAME = "SplitUninstallStorage"
        private val KEY = "uninstall-trigger"
    }
}
