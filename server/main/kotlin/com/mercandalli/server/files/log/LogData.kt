package com.mercandalli.server.files.log

import java.lang.StringBuilder

data class LogData(
        val type: Type,
        val tags: List<String>,
        val dayString: String,
        val dateString: String,
        val dateLong: Long,
        val contentString: Map<String, String>,
        val contentLong: Map<String, Long>,
        val contentBoolean: Map<String, Boolean>
) {

    enum class Type {
        Description,
        Error
    }

    fun toTerminalInput(): String {
        val tagColor = if (type == Type.Error) ANSI_RED_BOLD else ANSI_CYAN
        val stringBuilder = StringBuilder("$tagColor$dayString$ANSI_RESET ")
        for (tag in tags) {
            stringBuilder.append('[').append(tag).append(']')
        }
        stringBuilder.append(' ')
        for (key in contentString.keys) {
            val value = contentString[key]
            stringBuilder
                    .append(ANSI_PURPLE)
                    .append(key)
                    .append(ANSI_RESET)
                    .append(':')
                    .append(value)
                    .append(' ')
        }
        return stringBuilder.toString()
    }

    @Suppress("unused")
    companion object {

        private const val ANSI_RESET = "\u001B[0m"
        private const val ANSI_BLACK = "\u001B[30m"
        private const val ANSI_RED = "\u001B[31m"
        private const val ANSI_GREEN = "\u001B[32m"
        private const val ANSI_YELLOW = "\u001B[33m"
        private const val ANSI_BLUE = "\u001B[34m"
        private const val ANSI_PURPLE = "\u001B[35m"
        private const val ANSI_CYAN = "\u001B[36m"
        private const val ANSI_WHITE = "\u001B[37m"
        private const val ANSI_RED_BOLD = "\u001B[1;31m"
    }
}
