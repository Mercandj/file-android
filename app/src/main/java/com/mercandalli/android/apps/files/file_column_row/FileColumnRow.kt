@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.file_column_row

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.appcompat.widget.PopupMenu
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import com.mercandalli.android.apps.files.R
import com.mercandalli.android.apps.files.common.DialogUtils
import com.mercandalli.android.apps.files.main.ApplicationGraph
import com.mercandalli.sdk.files.api.File

class FileColumnRow @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), FileColumnRowContract.Screen {

    private val view = View.inflate(context, R.layout.view_file_column_row, this)
    private val icon: ImageView = view.findViewById(R.id.view_file_column_row_icon)
    private val title: TextView = view.findViewById(R.id.view_file_column_row_title)
    private val arrayRight: ImageView = view.findViewById(R.id.view_file_column_row_arrow_right)
    private val userAction = createUserAction()

    private var fileClickListener: FileClickListener? = null
    private var fileLongClickListener: FileLongClickListener? = null

    init {
        foreground = getSelectableItemBackground(context)
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
            val selectedTitleColor = ContextCompat.getColor(
                context, R.color.view_file_row_selected_title)
            title.setTextColor(selectedTitleColor)
            icon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
            arrayRight.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
            title.isSelected = true
        } else {
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
        DialogUtils.alert(
            context,
            "Delete file?",
            "Do you want to delete: $fileName",
            "Yes",
            object : DialogUtils.OnDialogUtilsListener {
                override fun onDialogUtilsCalledBack() {
                    userAction.onDeleteConfirmedClicked()
                }
            },
            "No",
            null
        )
    }

    override fun showRenamePrompt(fileName: String) {
        DialogUtils.prompt(context, "Rename file?",
            "Enter a new name for: $fileName",
            "Rename",
            object : DialogUtils.OnDialogUtilsStringListener {
                override fun onDialogUtilsStringCalledBack(text: String) {
                    userAction.onRenameConfirmedClicked(text)
                }
            },
            "Dismiss",
            null,
            fileName,
            "File name",
            null
        )
    }

    override fun setTextColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        title.setTextColor(color)
    }

    override fun setBackgroundColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        setBackgroundColor(color)
    }

    fun setFile(file: File, selectedPath: String?) {
        userAction.onFileChanged(file, selectedPath)
    }

    fun setFileClickListener(listener: FileClickListener?) {
        fileClickListener = listener
    }

    private fun createUserAction() = if (isInEditMode) {
        object : FileColumnRowContract.UserAction {
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
    } else {
        val audioManager = ApplicationGraph.getAudioManager()
        val fileDeleteManager = ApplicationGraph.getFileDeleteManager()
        val fileCopyCutManager = ApplicationGraph.getFileCopyCutManager()
        val fileRenameManager = ApplicationGraph.getFileRenameManager()
        val themeManager = ApplicationGraph.getThemeManager()
        val toastManager = ApplicationGraph.getToastManager()
        FileColumnRowPresenter(
            this,
            audioManager,
            fileDeleteManager,
            fileCopyCutManager,
            fileRenameManager,
            themeManager,
            toastManager,
            R.drawable.ic_play_arrow_black_24dp,
            R.drawable.ic_volume_up_black_24dp,
            R.color.view_file_row_selected_title,
            R.color.view_file_row_selected_background
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
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_file_row_copy -> userAction.onCopyClicked()
                R.id.menu_file_row_cut -> userAction.onCutClicked()
                R.id.menu_file_row_delete -> userAction.onDeleteClicked()
                R.id.menu_file_row_rename -> userAction.onRenameClicked()
            }
            false
        }
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
