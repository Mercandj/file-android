package com.mercandalli.android.apps.files.main

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import com.mercandalli.android.apps.files.R
import com.mercandalli.android.apps.files.activity.ActivityExtension.bind
import com.mercandalli.android.apps.files.bottom_bar.BottomBar
import com.mercandalli.android.apps.files.common.DialogUtils
import com.mercandalli.android.apps.files.file_column_horizontal_lists.FileColumnHorizontalLists
import com.mercandalli.android.apps.files.file_list.FileListView
import com.mercandalli.android.apps.files.file_online.FileOnlineView
import com.mercandalli.android.apps.files.file_online_upload.FileOnlineUploadActivity
import com.mercandalli.android.apps.files.note.NoteView
import com.mercandalli.android.apps.files.settings.SettingsView
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur

class MainActivity : AppCompatActivity(),
    MainActivityContract.Screen {

    private val fileListViewSelectedFileListener = createFileListViewSelectedFileListener()
    private val fileList: FileListView by bind(R.id.activity_main_file_list)
    private val fileColumnHorizontalLists: FileColumnHorizontalLists by bind(R.id.activity_main_file_horizontal_lists)
    private val onlineLazy = lazy {
        val view = findViewById<ViewStub>(R.id.activity_main_file_online_view_stub)
        val fileOnlineView = view.inflate() as FileOnlineView
        fileOnlineView.setFileListViewSelectedFileListener(fileListViewSelectedFileListener)
        fileOnlineView
    }
    private val online by onlineLazy
    private val note: NoteView by bind(R.id.activity_main_note)
    private val settings: SettingsView by bind(R.id.activity_main_settings)
    private val bottomBar: BottomBar by bind(R.id.activity_main_bottom_bar)
    private val toolbarDelete: View by bind(R.id.activity_main_toolbar_delete)
    private val toolbarShare: View by bind(R.id.activity_main_toolbar_share)
    private val toolbarAdd: View by bind(R.id.activity_main_toolbar_add)
    private val toolbarUpload: View by bind(R.id.activity_main_toolbar_upload)
    private val toolbarFilePaste: View by bind(R.id.activity_main_toolbar_file_paste)
    private val toolbarFileList: View by bind(R.id.activity_main_toolbar_file_list)
    private val toolbarFileColumn: View by bind(R.id.activity_main_toolbar_file_column)
    private val bottomBarBlurView: BlurView by bind(R.id.activity_main_bottom_bar_container)
    private val userAction by lazy { createUserAction() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userAction.onCreate()
        userAction.onRestoreInstanceState(savedInstanceState)
        bottomBar.setOnBottomBarClickListener(createOnBottomBarClickListener())
        toolbarDelete.setOnClickListener { userAction.onToolbarDeleteClicked() }
        toolbarShare.setOnClickListener { userAction.onToolbarShareClicked() }
        toolbarAdd.setOnClickListener { userAction.onToolbarAddClicked() }
        toolbarUpload.setOnClickListener { userAction.onToolbarUploadClicked() }
        toolbarFileColumn.setOnClickListener { userAction.onToolbarFileColumnClicked() }
        toolbarFileList.setOnClickListener { userAction.onToolbarFileListClicked() }
        toolbarFilePaste.setOnClickListener { userAction.onToolbarFilePasteClicked() }
        fileList.setFileListViewSelectedFileListener(fileListViewSelectedFileListener)
        val fileHorizontalListsSelectedFileListener = createFileHorizontalListsSelectedFileListener()
        fileColumnHorizontalLists.setFileHorizontalListsSelectedFileListener(fileHorizontalListsSelectedFileListener)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val decorView = window.decorView
            bottomBarBlurView.setupWith(decorView.findViewById<View>(android.R.id.content) as ViewGroup)
                .setFrameClearDrawable(decorView.background)
                .setBlurAlgorithm(RenderScriptBlur(this))
                .setBlurRadius(2f)
                .setHasFixedTransformationMatrix(true)
        }
        fileList.getCurrentPath()
    }

    override fun onDestroy() {
        userAction.onDestroy()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        userAction.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        fileColumnHorizontalLists.onResume()
    }

    override fun showFileListView() {
        fileList.visibility = View.VISIBLE
    }

    override fun hideFileListView() {
        fileList.visibility = View.GONE
    }

    override fun showFileColumnView() {
        fileColumnHorizontalLists.visibility = View.VISIBLE
    }

    override fun hideFileColumnView() {
        fileColumnHorizontalLists.visibility = View.GONE
    }

    override fun showOnlineView() {
        online.visibility = View.VISIBLE
    }

    override fun hideOnlineView() {
        if (!onlineLazy.isInitialized()) {
            return
        }
        online.visibility = View.GONE
    }

    override fun showNoteView() {
        note.visibility = View.VISIBLE
    }

    override fun hideNoteView() {
        note.visibility = View.GONE
    }

    override fun showSettingsView() {
        settings.visibility = View.VISIBLE
    }

    override fun hideSettingsView() {
        settings.visibility = View.GONE
    }

    override fun showToolbarDelete() {
        toolbarDelete.visibility = View.VISIBLE
    }

    override fun hideToolbarDelete() {
        toolbarDelete.visibility = View.GONE
    }

    override fun showToolbarShare() {
        toolbarShare.visibility = View.VISIBLE
    }

    override fun hideToolbarShare() {
        toolbarShare.visibility = View.GONE
    }

    override fun showToolbarAdd() {
        toolbarAdd.visibility = View.VISIBLE
    }

    override fun hideToolbarAdd() {
        toolbarAdd.visibility = View.GONE
    }

    override fun showToolbarUpload() {
        toolbarUpload.visibility = View.VISIBLE
    }

    override fun hideToolbarUpload() {
        toolbarUpload.visibility = View.GONE
    }

    override fun showToolbarFileColumn() {
        toolbarFileColumn.visibility = View.VISIBLE
    }

    override fun hideToolbarFileColumn() {
        toolbarFileColumn.visibility = View.GONE
    }

    override fun showToolbarFileList() {
        toolbarFileList.visibility = View.VISIBLE
    }

    override fun hideToolbarFileList() {
        toolbarFileList.visibility = View.GONE
    }

    override fun showToolbarFilePaste() {
        toolbarFilePaste.visibility = View.VISIBLE
    }

    override fun hideToolbarFilePaste() {
        toolbarFilePaste.visibility = View.GONE
    }

    override fun selectBottomBarFile() {
        bottomBar.selectFile()
    }

    override fun selectBottomBarOnline() {
        bottomBar.selectOnline()
    }

    override fun selectBottomBarNote() {
        bottomBar.selectNote()
    }

    override fun selectBottomBarSettings() {
        bottomBar.selectSettings()
    }

    override fun deleteNote() {
        note.onDeleteClicked()
    }

    override fun shareNote() {
        note.onShareClicked()
    }

    override fun showFileCreationSelection() {
        val menuAlert = AlertDialog.Builder(this)
        val menuList = arrayOf<String>(getString(R.string.file_model_local_new_folder_file))
        menuAlert.setTitle(getString(R.string.file_model_local_new_title))
        menuAlert.setItems(menuList) { _, item ->
            when (item) {
                0 -> DialogUtils.prompt(
                    this,
                    getString(R.string.file_model_local_new_folder_file),
                    getString(R.string.file_model_local_new_folder_file_description),
                    getString(R.string.ok),
                    object : DialogUtils.OnDialogUtilsStringListener {
                        override fun onDialogUtilsStringCalledBack(text: String) {
                            userAction.onFileCreationConfirmed(text)
                        }
                    },
                    getString(android.R.string.cancel),
                    null,
                    null)
                else -> throw IllegalStateException("Unsupported item: $item")
            }
        }
        menuAlert.create().show()
    }

    override fun showFileUploadSelection() {
        FileOnlineUploadActivity.start(this)
    }

    override fun setWindowBackgroundColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(this, colorRes)
        window.setBackgroundDrawable(ColorDrawable(color))
    }

    override fun setBottomBarBlurOverlayColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(this, colorRes)
        bottomBarBlurView.setOverlayColor(color)
    }

    override fun setPasteIconVisibility(visible: Boolean) {
        toolbarFilePaste.visibility = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    override fun getFileListCurrentPath() = fileList.getCurrentPath()

    override fun getFileOnlineCurrentPath() = online.getCurrentPath()

    private fun createOnBottomBarClickListener() = object : BottomBar.OnBottomBarClickListener {
        override fun onFileSectionClicked() {
            userAction.onFileSectionClicked()
        }

        override fun onOnlineSectionClicked() {
            userAction.onOnlineSectionClicked()
        }

        override fun onNoteSectionClicked() {
            userAction.onNoteSectionClicked()
        }

        override fun onSettingsSectionClicked() {
            userAction.onSettingsSectionClicked()
        }
    }

    private fun createFileHorizontalListsSelectedFileListener() = object : FileColumnHorizontalLists.FileHorizontalListsSelectedFileListener {
        override fun onSelectedFilePathChanged(path: String?) {
            userAction.onSelectedFilePathChanged(path)
        }
    }

    private fun createFileListViewSelectedFileListener() = object : FileListView.FileListViewSelectedFileListener {
        override fun onSelectedFilePathChanged(path: String?) {
            userAction.onSelectedFilePathChanged(path)
        }
    }

    private fun createUserAction(): MainActivityContract.UserAction {
        val fileCreatorManager = ApplicationGraph.getFileCreatorManager()
        val fileOnlineCreatorManager = ApplicationGraph.getFileOnlineCreatorManager()
        val fileCopyCutManager = ApplicationGraph.getFileCopyCutManager()
        val fileOnlineCopyCutManager = ApplicationGraph.getFileOnlineCopyCutManager()
        val themeManager = ApplicationGraph.getThemeManager()
        val fileUiStorageSharedPreferences = getSharedPreferences(
            MainActivityFileUiStorageImpl.PREFERENCE_NAME,
            Context.MODE_PRIVATE
        )
        val mainActivityFileUiStorage = MainActivityFileUiStorageImpl(
            fileUiStorageSharedPreferences
        )
        val sectionStorageSharedPreferences = getSharedPreferences(
            MainActivitySectionStorageImpl.PREFERENCE_NAME,
            Context.MODE_PRIVATE
        )
        val mainActivitySectionStorage = MainActivitySectionStorageImpl(
            sectionStorageSharedPreferences
        )
        return MainActivityPresenter(
            this,
            fileCreatorManager,
            fileOnlineCreatorManager,
            fileCopyCutManager,
            fileOnlineCopyCutManager,
            themeManager,
            mainActivityFileUiStorage,
            mainActivitySectionStorage,
            Environment.getExternalStorageDirectory().absolutePath,
            "/"
        )
    }
}
