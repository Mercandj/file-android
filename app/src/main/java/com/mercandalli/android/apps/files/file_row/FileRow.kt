package com.mercandalli.android.apps.files.file_row

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.mercandalli.android.sdk.files.api.File

class FileRow @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), FileRowContract.Screen {

    override fun showFile(file: File) {

    }
}