package com.mercandalli.android.sdk.files.api.internal

import com.mercandalli.sdk.files.api.FileShareManager

internal class FileShareManagerAndroid(
    private val addOn: AddOn
) : FileShareManager {

    override fun share(path: String) {
        val mime = extractMime(path)
        addOn.startActivity(path, mime)
    }

    override fun isShareSupported(path: String): Boolean {
        val ioFile = java.io.File(path)
        if (!ioFile.exists()) {
            return false
        }
        if (ioFile.isDirectory) {
            return false
        }
        return true
    }

    interface AddOn {
        fun startActivity(path: String, mime: String)
    }

    companion object {

        private const val MIME_AUDIO = "audio/*"
        private const val MIME_IMAGE = "image/*"
        private const val MIME_TEXT = "text/*"
        private const val MIME_HTML = "text/html"
        private const val MIME_VIDEO = "video/*"
        private const val MIME_APK = "application/vnd.android.package-archive"
        private const val MIME_PDF = "application/pdf"
        private const val MIME_WORD = "application/msword"

        private fun extractMime(path: String): String {
            for (typeModelENUM in FileOpenManagerAndroid.FileTypeModelENUM.values()) {
                for (ext in typeModelENUM.type.value) {
                    if (path.endsWith(".$ext")) {
                        when (typeModelENUM) {
                            FileOpenManagerAndroid.FileTypeModelENUM.APK -> return MIME_APK
                            FileOpenManagerAndroid.FileTypeModelENUM.TEXT -> return MIME_TEXT
                            FileOpenManagerAndroid.FileTypeModelENUM.IMAGE -> return MIME_IMAGE
                            FileOpenManagerAndroid.FileTypeModelENUM.AUDIO -> return MIME_AUDIO
                            FileOpenManagerAndroid.FileTypeModelENUM.VIDEO -> return MIME_VIDEO
                            FileOpenManagerAndroid.FileTypeModelENUM.WORD -> return MIME_WORD
                            FileOpenManagerAndroid.FileTypeModelENUM.OPEN_DOCUMENT -> return MIME_TEXT
                            FileOpenManagerAndroid.FileTypeModelENUM.VCF -> return MIME_TEXT
                            FileOpenManagerAndroid.FileTypeModelENUM.PDF -> return MIME_PDF
                            FileOpenManagerAndroid.FileTypeModelENUM.SOURCE -> return MIME_TEXT
                            FileOpenManagerAndroid.FileTypeModelENUM.SOURCE_JAVA -> return MIME_TEXT
                            FileOpenManagerAndroid.FileTypeModelENUM.SOURCE_HTML -> return MIME_HTML
                            FileOpenManagerAndroid.FileTypeModelENUM.LOG -> return MIME_TEXT
                            else -> return ""
                        }
                    }
                }
            }
            return ""
        }
    }
}
