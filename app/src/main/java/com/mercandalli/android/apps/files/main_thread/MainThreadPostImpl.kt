@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.main_thread

import android.os.Handler

/**
 * Simple [MainThreadPost] implementation.
 */
internal class MainThreadPostImpl(

    /**
     * The main (UI) [Thread] used to be sure that the callbacks are on the main [Thread].
     */
    private val mainThread: Thread,

    /**
     * An [Handler] used to be sure that the callbacks are on the main [Thread].
     */
    private val mainThreadHandler: Handler
) : MainThreadPost {

    override fun isOnMainThread() = Thread.currentThread() === mainThread

    override fun post(runnable: Runnable) {
        mainThreadHandler.post(runnable)
    }
}
