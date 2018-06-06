package com.mercandalli.android.apps.files.theme

interface ThemeManager {

    val theme: Theme

    fun setDarkEnable(enable: Boolean)

    fun isDarkEnable(): Boolean

    fun registerThemeListener(listener: OnCurrentThemeChangeListener)

    fun unregisterThemeListener(listener: OnCurrentThemeChangeListener)

    interface OnCurrentThemeChangeListener {
        fun onCurrentThemeChanged()
    }
}