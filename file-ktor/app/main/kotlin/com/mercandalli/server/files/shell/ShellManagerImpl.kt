package com.mercandalli.server.files.shell

import com.mercandalli.server.files.log.LogManager
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.concurrent.Executors
import java.util.function.Consumer

class ShellManagerImpl(
        private val logManager: LogManager
) : ShellManager {

    override fun execute(command: String, block: (result: String) -> Unit) {
        logManager.d(TAG, "Input: $command")
        val process: Process = Runtime.getRuntime().exec(command)
        val streamGobbler = StreamGobbler(process.inputStream, Consumer {
            logManager.d(TAG, "Output: $it")
            block(it)
        })
        Executors.newSingleThreadExecutor().submit(streamGobbler)
        val exitCode = process.waitFor()
        assert(exitCode == 0)
    }

    override fun isWindows(): Boolean {
        return System.getProperty("os.name").toLowerCase().startsWith("windows")
    }

    private class StreamGobbler(
            private val inputStream: InputStream,
            private val consumer: Consumer<String>) : Runnable {

        override fun run() {
            BufferedReader(InputStreamReader(inputStream)).lines().forEach(consumer)
            consumer.accept("")
        }
    }

    companion object {
        private const val TAG = "ShellManager"
    }
}