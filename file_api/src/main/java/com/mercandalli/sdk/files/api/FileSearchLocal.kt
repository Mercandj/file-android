package com.mercandalli.sdk.files.api

object FileSearchLocal {

    fun searchSync(
        query: String,
        path: String
    ): List<String> {
        val pathsResult = ArrayList<String>()
        val ioFile = java.io.File(path)
        if (!ioFile.isDirectory) {
            val contains = ioFile.name.toLowerCase().contains(query.toLowerCase())
            if (contains && !pathsResult.contains(path)) {
                pathsResult.add(path)
            }
            return pathsResult
        }
        val ioFiles = ioFile.listFiles() ?: return pathsResult
        for (ioFileLoop in ioFiles) {
            val contains = ioFileLoop.name.toLowerCase().contains(query.toLowerCase())
            if (contains && !pathsResult.contains(ioFileLoop.absolutePath)) {
                pathsResult.add(ioFileLoop.absolutePath)
            }
            pathsResult.addAll(searchSync(query, ioFileLoop.absolutePath))
        }
        return pathsResult
    }
}
