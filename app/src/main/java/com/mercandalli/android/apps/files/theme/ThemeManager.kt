package com.mercandalli.android.apps.files.theme

interface ThemeManager {

    fun getTheme(): Theme

    fun setDarkEnable(enable: Boolean)

    fun isDarkEnable(): Boolean

    fun registerThemeListener(listener: OnCurrentThemeChangeListener)

    fun unregisterThemeListener(listener: OnCurrentThemeChangeListener)

    interface OnCurrentThemeChangeListener {
        fun onCurrentThemeChanged()
    }
}