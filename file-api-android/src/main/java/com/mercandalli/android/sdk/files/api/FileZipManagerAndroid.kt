package com.mercandalli.android.sdk.files.api

import com.mercandalli.sdk.files.api.FileExtension
import com.mercandalli.sdk.files.api.FileZipManager
import com.mercandalli.sdk.files.api.MediaScanner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.zip.ZipInputStream

class FileZipManagerAndroid(
        private val mediaScanner: MediaScanner
) : FileZipManager {

    private val listeners = ArrayList<FileZipManager.FileZipListener>()

    override fun isZip(path: String) = FileExtension.ZIP.isCompliant(path)

    override fun unzip(
            path: String,
            outputPath: String
    ) {
        GlobalScope.launch(Dispatchers.Default) {
            val zipInput = java.io.File(path)
            val output = java.io.File(outputPath)
            unzipSync(zipInput, output)
            mediaScanner.refresh(output.parentFile.absolutePath)
            GlobalScope.launch(Dispatchers.Main) {
                for (listener in listeners) {
                    listener.onUnzipEnded(path, outputPath)
                }
            }
        }
    }


    override fun registerFileZipListener(listener: FileZipManager.FileZipListener) {
        if (listeners.contains(listener)) {
            return
        }
        listeners.add(listener)
    }

    override fun unregisterFileZipListener(listener: FileZipManager.FileZipListener) {
        listeners.remove(listener)
    }

    companion object {

        @Throws(IOException::class)
        private fun unzipSync(zipFile: java.io.File, targetDirectory: java.io.File) {
            val zipInputStream = ZipInputStream(
                    java.io.BufferedInputStream(
                            java.io.FileInputStream(
                                    zipFile
                            )
                    )
            )
            zipInputStream.use { zis ->
                var count: Int
                val buffer = ByteArray(8192)
                var zipEntry = zis.nextEntry
                while (zipEntry != null) {
                    val file = java.io.File(targetDirectory, zipEntry.name)
                    val dir = if (zipEntry.isDirectory) file else file.parentFile
                    if (!dir.isDirectory && !dir.mkdirs())
                        throw java.io.FileNotFoundException(
                                "Failed to ensure directory: " + dir.absolutePath
                        )
                    if (zipEntry.isDirectory) {
                        zipEntry = zis.nextEntry
                        continue
                    }
                    val fileOutputStream = java.io.FileOutputStream(file)
                    fileOutputStream.use {
                        count = zis.read(buffer)
                        while (count != -1) {
                            it.write(buffer, 0, count)
                            count = zis.read(buffer)
                        }
                    }
                    val time = zipEntry.time
                    if (time > 0) {
                        file.setLastModified(time)
                    }
                    zipEntry = zis.nextEntry
                }
            }
        }
    }
}