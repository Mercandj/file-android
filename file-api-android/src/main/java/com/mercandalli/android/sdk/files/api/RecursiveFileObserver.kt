package com.mercandalli.android.sdk.files.api

import java.io.File
import java.util.ArrayList
import java.util.Stack

import android.os.FileObserver

class RecursiveFileObserver private constructor(
        private val path: String,
        private val mask: Int,
        private var block: (path: String?) -> Unit) : FileObserver(path, mask) {

    private var observers: ArrayList<SingleFileObserver>? = null

    constructor(path: String, block: (path: String?) -> Unit) :
            this(path, FileObserver.ALL_EVENTS, block)

    override fun startWatching() {
        if (observers != null) return
        observers = ArrayList()
        val stack = Stack<String>()
        stack.push(path)

        while (!stack.empty()) {
            val parent = stack.pop()
            observers!!.add(SingleFileObserver(parent, mask))
            val path = File(parent)
            val files = path.listFiles() ?: continue
            for (file in files) {
                if (file.isDirectory && file.name != "." && file.name != "..") {
                    stack.push(file.path)
                }
            }
        }
        for (i in observers!!.indices) {
            observers!![i].startWatching()
        }
    }

    override fun stopWatching() {
        if (observers == null) return

        for (i in observers!!.indices) {
            observers!![i].stopWatching()
        }

        observers!!.clear()
        observers = null
    }

    override fun onEvent(event: Int, path: String?) {
        block(path)
    }

    private inner class SingleFileObserver internal constructor(
            private val mPath: String,
            mask: Int
    ) : FileObserver(mPath, mask) {

        override fun onEvent(event: Int, path: String?) {
            val newPath = "$mPath/$path"
            this@RecursiveFileObserver.onEvent(event, newPath)
        }

    }

    companion object {

        var CHANGES_ONLY = FileObserver.CLOSE_WRITE or FileObserver.MOVE_SELF or FileObserver.MOVED_FROM
    }
}