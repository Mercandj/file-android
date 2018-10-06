package com.mercandalli.android.apps.files.theme

interface ThemeManager {

    fun getTheme(): Theme

    fun setDarkEnable(enable: Boolean)

    fun isDarkEnable(): Boolean

    fun registerThemeListener(listener: ThemeListener)

    fun unregisterThemeListener(listener: ThemeListener)

    interface ThemeListener {
        fun onThemeChanged()
    }
}