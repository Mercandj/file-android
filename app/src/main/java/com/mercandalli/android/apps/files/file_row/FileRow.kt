package com.mercandalli.android.apps.files.file_row

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.PopupMenu
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.mercandalli.android.apps.files.R
import com.mercandalli.android.apps.files.common.DialogUtils
import com.mercandalli.android.apps.files.main.ApplicationGraph
import com.mercandalli.sdk.files.api.File

class FileRow @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), FileRowContract.Screen {

    private val userAction: FileRowContract.UserAction
    private val icon: ImageView
    private val title: TextView
    private val arrayRight: ImageView
    private var fileClickListener: FileClickListener? = null
    private var fileLongClickListener: FileLongClickListener? = null

    init {
        View.inflate(context, R.layout.view_file_row, this)
        icon = findViewById(R.id.view_file_row_icon)
        title = findViewById(R.id.view_file_row_title)
        arrayRight = findViewById(R.id.view_file_row_arrow_right)
        foreground = getSelectableItemBackground(context)
        userAction = createUserAction()

        setOnClickListener { userAction.onRowClicked() }
        setOnLongClickListener {
            userAction.onRowLongClicked()
            return@setOnLongClickListener true
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        userAction.onAttached()
    }

    override fun onDetachedFromWindow() {
        userAction.onDetached()
        super.onDetachedFromWindow()
    }

    override fun setTitle(title: String) {
        this.title.text = title
    }

    override fun setRightIconVisibility(visible: Boolean) {
        arrayRight.visibility = if (visible) VISIBLE else GONE
    }

    override fun setRightIconDrawableRes(drawableRightIconDirectoryDrawableRes: Int) {
        arrayRight.setImageResource(drawableRightIconDirectoryDrawableRes)
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
            title.isSelected = true
        } else {
            val backgroundColor = ContextCompat.getColor(
                    context, R.color.view_file_row_background)
            setBackgroundColor(backgroundColor)
            val titleColor = ContextCompat.getColor(context, R.color.view_file_row_title)
            title.setTextColor(titleColor)
            arrayRight.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN)
            title.isSelected = false
        }
    }

    override fun notifyRowClicked(file: File) {
        fileClickListener?.onFileClicked(file)
    }

    override fun notifyRowLongClicked(file: File) {
        fileLongClickListener?.onFileLongClicked(file)
    }

    override fun showOverflowPopupMenu() {
        showOverflowPopupMenu(this)
    }

    override fun showDeleteConfirmation(fileName: String) {
        DialogUtils.alert(context, "Delete file?",
                "Do you want to delete: $fileName", "Yes",
                { userAction.onDeleteConfirmedClicked() }, "No", {}
        )
    }

    override fun showRenamePrompt(fileName: String) {
        DialogUtils.prompt(context, "Rename file?",
                "Enter a new name for: $fileName", "Rename",
                { userAction.onRenameConfirmedClicked(it) }, "Dismiss", {},
                fileName, "File name", {}
        )
    }

    fun setFile(file: File, selectedPath: String?) {
        userAction.onFileChanged(file, selectedPath)
    }

    fun setFileClickListener(listener: FileClickListener?) {
        fileClickListener = listener
    }

    private fun createUserAction(): FileRowContract.UserAction {
        if (isInEditMode) {
            return object : FileRowContract.UserAction {
                override fun onAttached() {}
                override fun onDetached() {}
                override fun onFileChanged(file: File, selectedPath: String?) {}
                override fun onRowClicked() {}
                override fun onRowLongClicked() {}
                override fun onCopyClicked() {}
                override fun onCutClicked() {}
                override fun onDeleteClicked() {}
                override fun onDeleteConfirmedClicked() {}
                override fun onRenameClicked() {}
                override fun onRenameConfirmedClicked(fileName: String) {}
            }
        }
        val fileDeleteManager = ApplicationGraph.getFileDeleteManager()
        val fileCopyCutManager = ApplicationGraph.getFileCopyCutManager()
        val fileRenameManager = ApplicationGraph.getFileRenameManager()
        val audioManager = ApplicationGraph.getAudioManager()
        return FileRowPresenter(
                this,
                fileDeleteManager,
                fileCopyCutManager,
                fileRenameManager,
                audioManager,
                R.drawable.ic_play_arrow_black_24dp,
                R.drawable.ic_volume_up_black_24dp
        )
    }

    /**
     * Show a popup menu which allows the user to perform action.
     *
     * @param view : The [View] on which the popup menu should be anchored.
     */
    private fun showOverflowPopupMenu(view: View) {
        val popupMenu = PopupMenu(context, view, Gravity.END)
        popupMenu.menuInflater.inflate(R.menu.menu_file_row, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener({
            when (it.itemId) {
                R.id.menu_file_row_copy -> userAction.onCopyClicked()
                R.id.menu_file_row_cut -> userAction.onCutClicked()
                R.id.menu_file_row_delete -> userAction.onDeleteClicked()
                R.id.menu_file_row_rename -> userAction.onRenameClicked()
            }
            false
        })
        popupMenu.show()
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

    interface FileLongClickListener {
        fun onFileLongClicked(file: File)
    }
}