package com.mercandalli.android.apps.files.file_row

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.mercandalli.android.apps.files.R
import com.mercandalli.android.apps.files.main.ApplicationGraph.Companion.init
import com.mercandalli.sdk.files.api.File

class FileRow @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), FileRowContract.Screen {

    private val userAction = FileRowPresenter(this)
    private val icon: ImageView
    private val title: TextView
    private val arrayRight: View

    init {
        View.inflate(context, R.layout.view_file_row, this)
        icon = findViewById(R.id.view_file_row_icon)
        title = findViewById(R.id.view_file_row_title)
        arrayRight = findViewById(R.id.view_file_row_arrow_right)
    }

    override fun setTitle(title: String) {
        this.title.text = title
    }

    override fun setArrowRightVisibility(visible: Boolean) {
        arrayRight.visibility = if (visible) VISIBLE else GONE
    }

    override fun setIcon(directory: Boolean) {
        if (directory) {
            icon.setImageResource(R.drawable.ic_folder_black_24dp)
            val color = ContextCompat.getColor(context, R.color.color_primary)
            icon.setColorFilter(
                    color,
                    android.graphics.PorterDuff.Mode.SRC_IN)
        } else {
            icon.setImageResource(R.drawable.ic_insert_drive_file_black_24dp)
            icon.setColorFilter(
                    Color.GRAY,
                    android.graphics.PorterDuff.Mode.SRC_IN)

        }
    }

    fun setFile(file: File) {
        userAction.onFileChanged(file)
    }
}