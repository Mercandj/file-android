package com.mercandalli.android.sdk.files.api

import com.mercandalli.sdk.files.api.FileOpenManager
import com.mercandalli.sdk.files.api.FileZipManager
import java.io.File

class FileOpenManagerAndroid(
    private val fileZipManager: FileZipManager,
    private val addOn: AddOn
) : FileOpenManager {

    override fun open(path: String, mime: String?) {
        if (fileZipManager.isZip(path)) {
            unzip(path)
            return
        }
        val mimeToUse = mime ?: extractMime(path)
        addOn.startActivity(path, mimeToUse)
    }

    private fun unzip(path: String) {
        val file = File(path)
        val parentPath = file.parentFile.absolutePath
        val outputPath = createNewFolderPath(
            parentPath,
            file.name.toLowerCase().replace(".zip", "")
        ).absolutePath
        fileZipManager.unzip(path, outputPath)
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
            for (typeModelENUM in FileTypeModelENUM.values()) {
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

        private fun createNewFolderPath(
            parentFolderPath: String,
            folderName: String,
            index: Int = 2
        ): File {
            val folderCandidate = java.io.File(parentFolderPath, "$folderName-$index")
            if (folderCandidate.exists()) {
                return createNewFolderPath(
                    parentFolderPath,
                    folderName,
                    index + 1
                )
            }
            return folderCandidate
        }
    }

    interface AddOn {
        fun startActivity(path: String, mime: String)
    }

    enum class FileTypeModelENUM constructor(val type: FileTypeModel) {

        APK(FileTypeModel("apk")),
        TEXT(FileTypeModel("txt", "csv", "rtf", "text", "json")),
        IMAGE(FileTypeModel("jpeg", "jpg", "png", "gif", "raw", "psd", "bmp", "tiff", "tif")),
        AUDIO(FileTypeModel("mp3", "wav", "m4a", "aiff", "wma", "caf", "flac", "m4p", "amr", "ogg")),
        VIDEO(FileTypeModel("m4v", "3gp", "wmv", "mp4", "mpeg", "mpg", "rm", "mov", "avi", "mkv", "flv", "ogg", "webm")),
        ARCHIVE(FileTypeModel("zip", "gzip", "rar", "tar", "tar.gz", "gz", "7z")),
        WORD(FileTypeModel("doc", "docx")),
        OPEN_DOCUMENT(FileTypeModel("odp")),
        POWERPOINT(FileTypeModel("ppt", "pptx")),
        EXCEL(FileTypeModel("xlsx")),
        VCF(FileTypeModel("vcf")),
        PDF(FileTypeModel("pdf")),
        N64(FileTypeModel("n64", "z64")),
        GB(FileTypeModel("gb")),
        GBC(FileTypeModel("gbc")),
        GBA(FileTypeModel("gba")),

        // Dev
        SOURCE(FileTypeModel("c", "cs", "cpp", "sql", "php", "html", "js", "css", "ec")),
        SOURCE_JAVA(FileTypeModel("java", "class")),
        SOURCE_HTML(FileTypeModel("html", "htm", "php", "xml")),
        NO_MEDIA(FileTypeModel("nomedia")),
        THREE_D(FileTypeModel("3ds", "obj", "max")),
        ISO(FileTypeModel("iso")),
        TMP(FileTypeModel("tmp")),
        INDEX(FileTypeModel("idx")),
        KEYSTORE(FileTypeModel("keystore", "jdk")),
        TRACE(FileTypeModel("trace")),
        DATABASE(FileTypeModel("db")),
        LOG(FileTypeModel("log")),

        DIRECTORY(FileTypeModel("dir", ""));
    }

    class FileTypeModel constructor(
        vararg val value: String
    )
}
