@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.file_list_row

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import androidx.core.content.ContextCompat
import androidx.cardview.widget.CardView
import androidx.appcompat.widget.PopupMenu
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
import com.mercandalli.sdk.files.api.FileDeleteManager
import com.mercandalli.sdk.files.api.FileCopyCutManager
import com.mercandalli.sdk.files.api.FileRenameManager
import com.mercandalli.sdk.files.api.FileSizeManager

class FileListRow @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), FileListRowContract.Screen {

    private val view = View.inflate(context, R.layout.view_file_list_row, this)
    private val card: CardView = view.findViewById(R.id.view_file_list_row_card)
    private val icon: ImageView = view.findViewById(R.id.view_file_list_row_icon)
    private val title: TextView = view.findViewById(R.id.view_file_list_row_title)
    private val subtitle: TextView = view.findViewById(R.id.view_file_list_row_subtitle)
    private val overflow: View = view.findViewById(R.id.view_file_list_row_overflow)
    private val sound: View = view.findViewById(R.id.view_file_list_row_sound)
    private val userAction = createUserAction()

    private var fileClickListener: FileClickListener? = null
    private var fileLongClickListener: FileLongClickListener? = null

    init {
        card.setOnClickListener { userAction.onRowClicked() }
        setOnLongClickListener {
            userAction.onRowLongClicked()
            return@setOnLongClickListener true
        }
        overflow.setOnClickListener {
            userAction.onOverflowClicked()
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

    override fun setSubtitle(subtitle: String) {
        this.subtitle.text = subtitle
    }

    override fun setSoundIconVisibility(visible: Boolean) {
        sound.visibility = if (visible) VISIBLE else INVISIBLE
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

    override fun notifyRowClicked(file: File) {
        fileClickListener?.onFileClicked(file)
    }

    override fun notifyRowLongClicked(file: File) {
        fileLongClickListener?.onFileLongClicked(file)
    }

    override fun showOverflowPopupMenu() {
        showOverflowPopupMenu(overflow)
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

    override fun setTitleTextColorRes(colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        title.setTextColor(color)
    }

    override fun setSubtitleTextColorRes(colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        subtitle.setTextColor(color)
    }

    override fun setCardBackgroundColorRes(colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        card.setCardBackgroundColor(color)
    }

    fun setFile(file: File, selectedPath: String?) {
        userAction.onFileChanged(file, selectedPath)
    }

    fun setFileClickListener(listener: FileClickListener?) {
        fileClickListener = listener
    }

    fun setFileManagers(
        fileCopyCutManager: FileCopyCutManager,
        fileDeleteManager: FileDeleteManager,
        fileRenameManager: FileRenameManager,
        fileSizeManager: FileSizeManager
    ) {
        userAction.onSetFileManagers(
            fileCopyCutManager,
            fileDeleteManager,
            fileRenameManager,
            fileSizeManager
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
                R.id.menu_file_row_details -> userAction.onDetailsClicked()
            }
            false
        }
        popupMenu.show()
    }

    private fun createUserAction() = if (isInEditMode) {
        object : FileListRowContract.UserAction {
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
            override fun onDetailsClicked() {}
            override fun onOverflowClicked() {}
            override fun onSetFileManagers(
                fileCopyCutManager: FileCopyCutManager,
                fileDeleteManager: FileDeleteManager,
                fileRenameManager: FileRenameManager,
                fileSizeManager: FileSizeManager
            ) {
            }
        }
    } else {
        val fileCopyCutManager = ApplicationGraph.getFileCopyCutManager()
        val fileDeleteManager = ApplicationGraph.getFileDeleteManager()
        val fileRenameManager = ApplicationGraph.getFileRenameManager()
        val fileSizeManager = ApplicationGraph.getFileSizeManager()
        val audioManager = ApplicationGraph.getAudioManager()
        val themeManager = ApplicationGraph.getThemeManager()
        val toastManager = ApplicationGraph.getToastManager()
        val screenManager = ApplicationGraph.getScreenManager()
        val fileString = context.getString(R.string.file_list_row_file)
        val directoryString = context.getString(R.string.file_list_row_directory)
        FileListRowPresenter(
            this,
            fileCopyCutManager,
            fileDeleteManager,
            fileRenameManager,
            fileSizeManager,
            audioManager,
            screenManager,
            themeManager,
            toastManager,
            fileString,
            directoryString
        )
    }

    interface FileClickListener {
        fun onFileClicked(file: File)
    }

    interface FileLongClickListener {
        fun onFileLongClicked(file: File)
    }
}
