@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.main_thread

interface MainThreadPost {

    /**
     * Is the call on main [Thread].
     *
     * @return `true` is call on main [Thread].
     */
    fun isOnMainThread(): Boolean

    /**
     * Post on main [Thread].
     *
     * @param runnable [Runnable.run] on main [Thread].
     */
    fun post(runnable: Runnable)
}
