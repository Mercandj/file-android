package com.mercandalli.android.sdk.files.api

class FileModule {

    fun provideFileManager(): FileManager {
        return FileManagerAndroid()
    }

}