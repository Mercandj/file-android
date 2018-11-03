package com.mercandalli.sdk.files.api

interface FileSortManager {

    fun sort(files: List<File>): List<File>

    fun sortIoFiles(ioFiles: List<java.io.File>): List<java.io.File>

    fun sortIoFiles(ioFiles: Array<out java.io.File>): Array<out java.io.File>
}
