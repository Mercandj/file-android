package com.mercandalli.android.apps.files.file_detail

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.FrameLayout
import com.mercandalli.android.apps.files.R
import com.mercandalli.sdk.files.api.File

class FileDetailView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), FileDetailContract.Screen {

    init {
        setBackgroundColor(ContextCompat.getColor(context, R.color.file_detail_background))
    }

    fun setFile(file: File?) {

    }
}