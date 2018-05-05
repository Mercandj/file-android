package com.mercandalli.android.apps.files.file_row

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.mercandalli.android.apps.files.R
import com.mercandalli.sdk.files.api.File

class FileRow @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), FileRowContract.Screen {

    private val userAction = FileRowPresenter(this)
    private val title: TextView

    init {
        View.inflate(context, R.layout.view_file_row, this)
        title = findViewById(R.id.view_file_row_title)
    }

    override fun setTitle(title: String) {
        this.title.text = title
    }

    fun setFile(file: File) {
        userAction.onFileChanged(file)
    }
}