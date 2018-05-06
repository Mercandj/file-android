package com.mercandalli.android.apps.files.file_row

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.mercandalli.android.apps.files.R
import com.mercandalli.sdk.files.api.File

class FileRow @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), FileRowContract.Screen {

    private val userAction = FileRowPresenter(this)
    private val icon: ImageView
    private val title: TextView
    private val arrayRight: ImageView
    private var fileClickListener: FileClickListener? = null

    init {
        View.inflate(context, R.layout.view_file_row, this)
        icon = findViewById(R.id.view_file_row_icon)
        title = findViewById(R.id.view_file_row_title)
        arrayRight = findViewById(R.id.view_file_row_arrow_right)
        foreground = getSelectableItemBackground(context)
        setOnClickListener { userAction.onRowClicked() }
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
            icon.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        } else {
            icon.setImageResource(R.drawable.ic_insert_drive_file_black_24dp)
            icon.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN)
        }
    }

    override fun setRowSelected(selected: Boolean) {
        if (selected) {
            val selectedBackgroundColor = ContextCompat.getColor(
                    context, R.color.view_file_row_selected_background)
            setBackgroundColor(selectedBackgroundColor)
            val selectedTitleColor = ContextCompat.getColor(
                    context, R.color.view_file_row_selected_title)
            title.setTextColor(selectedTitleColor)
            icon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
            arrayRight.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
        } else {
            val backgroundColor = ContextCompat.getColor(
                    context, R.color.view_file_row_background)
            setBackgroundColor(backgroundColor)
            val titleColor = ContextCompat.getColor(context, R.color.view_file_row_title)
            title.setTextColor(titleColor)
            arrayRight.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN)
        }
    }

    override fun notifyRowClicked(file: File) {
        fileClickListener?.onFileClicked(file)
    }

    fun setFile(file: File, selectedPath: String?) {
        userAction.onFileChanged(file, selectedPath)
    }

    fun setFileClickListener(listener: FileClickListener?) {
        fileClickListener = listener
    }

    companion object {
        fun getSelectableItemBackground(context: Context): Drawable? {
            val attrs = intArrayOf(android.R.attr.selectableItemBackground /* index 0 */)
            val ta = context.obtainStyledAttributes(attrs)
            val drawableFromTheme = ta.getDrawable(0 /* index */)
            ta.recycle()
            return drawableFromTheme
        }
    }

    interface FileClickListener {
        fun onFileClicked(file: File)
    }
}