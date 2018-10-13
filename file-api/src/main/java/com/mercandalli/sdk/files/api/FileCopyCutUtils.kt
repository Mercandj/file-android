package com.mercandalli.sdk.files.api

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object FileCopyCutUtils {

    fun copyJavaFileSync(pathInput: String, pathDirectoryOutput: String) {
        try {
            val dir = File(pathDirectoryOutput)
            if (!dir.exists()) {
                dir.mkdirs()
            }
            val inputFile = java.io.File(pathInput)

            var outputUrl = if (pathDirectoryOutput.endsWith("/")) {
                pathDirectoryOutput + inputFile.name
            } else {
                pathDirectoryOutput + File.separator + inputFile.name
            }
            while (File(outputUrl).exists()) {
                outputUrl = if (pathDirectoryOutput.endsWith("/")) {
                    pathDirectoryOutput + inputFile.name
                } else {
                    pathDirectoryOutput + File.separator + inputFile.name
                }
            }

            if (inputFile.isDirectory) {
                val copy = File(outputUrl)
                copy.mkdirs()
                val children = inputFile.listFiles()
                for (child in children) {
                    copyJavaFileSync(child.absolutePath, copy.absolutePath + File.separator)
                }
            } else {
                val inputStream = FileInputStream(pathInput)
                val outputStream = FileOutputStream(outputUrl)

                val buffer = ByteArray(1024)
                var read: Int = inputStream.read(buffer)
                while (read != -1) {
                    outputStream.write(buffer, 0, read)
                    read = inputStream.read(buffer)
                }
                inputStream.close()
                outputStream.flush()
                outputStream.close()
            }
        } catch (e: Exception) {

        }
    }

    fun cutJavaFileSync(pathInput: String, pathDirectoryOutput: String) {
        val ioFileInput = java.io.File(pathInput)
        val ioFileOutputDirectory = java.io.File(pathDirectoryOutput)
        val outputPath = ioFileOutputDirectory.absolutePath + File.separator + ioFileInput.name
        val ioFileOutput = java.io.File(outputPath)
        ioFileInput.renameTo(ioFileOutput)
    }
}