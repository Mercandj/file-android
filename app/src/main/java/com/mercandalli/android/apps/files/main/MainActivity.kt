package com.mercandalli.android.apps.files.main

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import com.mercandalli.android.apps.files.R
import com.mercandalli.android.apps.files.bottom_bar.BottomBar
import com.mercandalli.android.apps.files.common.DialogUtils
import com.mercandalli.android.apps.files.file_column_horizontal_lists.FileColumnHorizontalLists
import com.mercandalli.android.apps.files.note.NoteView
import com.mercandalli.android.apps.files.settings.SettingsView
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur

class MainActivity : AppCompatActivity(), MainActivityContract.Screen {

    private lateinit var fileColumnHorizontalLists: FileColumnHorizontalLists
    private lateinit var note: NoteView
    private lateinit var settings: SettingsView
    private lateinit var bottomBar: BottomBar
    private lateinit var toolbarDelete: View
    private lateinit var toolbarShare: View
    private lateinit var toolbarAdd: View
    private lateinit var toolbarFileViewSwitcher: View
    private lateinit var bottomBarBlurView: BlurView
    private lateinit var userAction: MainActivityContract.UserAction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fileColumnHorizontalLists = findViewById(R.id.activity_main_file_horizontal_lists)
        note = findViewById(R.id.activity_main_note)
        settings = findViewById(R.id.activity_main_settings)
        bottomBar = findViewById(R.id.activity_main_bottom_bar)
        toolbarDelete = findViewById(R.id.activity_main_toolbar_delete)
        toolbarShare = findViewById(R.id.activity_main_toolbar_share)
        toolbarAdd = findViewById(R.id.activity_main_toolbar_add)
        toolbarFileViewSwitcher = findViewById(R.id.activity_main_toolbar_file_view_switcher)
        bottomBarBlurView = findViewById(R.id.activity_main_bottom_bar_container)
        window.setBackgroundDrawable(ColorDrawable(
                ContextCompat.getColor(this, R.color.window_background_light)))
        userAction = createUserAction()
        userAction.onRestoreInstanceState(savedInstanceState)
        bottomBar.setOnBottomBarClickListener(object : BottomBar.OnBottomBarClickListener {
            override fun onFileSectionClicked() {
                userAction.onFileSectionClicked()
            }

            override fun onNoteSectionClicked() {
                userAction.onNoteSectionClicked()
            }

            override fun onSettingsSectionClicked() {
                userAction.onSettingsSectionClicked()
            }
        })
        toolbarDelete.setOnClickListener {
            userAction.onToolbarDeleteClicked()
        }
        toolbarShare.setOnClickListener {
            userAction.onToolbarShareClicked()
        }
        toolbarAdd.setOnClickListener {
            userAction.onToolbarAddClicked()
        }
        toolbarFileViewSwitcher.setOnClickListener {
            userAction.onToolbarFileViewSwitcherClicked()
        }
        fileColumnHorizontalLists.setFileHorizontalListsSelectedFileListener(object : FileColumnHorizontalLists.FileHorizontalListsSelectedFileListener {
            override fun onSelectedFilePathChanged(path: String?) {
                userAction.onSelectedFilePathChanged(path)
            }
        })
        val decorView = window.decorView
        bottomBarBlurView.setupWith(decorView.findViewById<View>(android.R.id.content) as ViewGroup)
                .windowBackground(decorView.background)
                .blurAlgorithm(RenderScriptBlur(this))
                .blurRadius(2f)
                .setHasFixedTransformationMatrix(true)
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

    override fun showFileView() {
        fileColumnHorizontalLists.visibility = View.VISIBLE
    }

    override fun hideFileView() {
        fileColumnHorizontalLists.visibility = View.GONE
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

    override fun showToolbarFileViewSwitcher() {
        toolbarFileViewSwitcher.visibility = View.VISIBLE
    }

    override fun hideToolbarFileViewSwitcher() {
        toolbarFileViewSwitcher.visibility = View.GONE
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

    override fun setWindowBackgroundColorRes(windowBackgroundColorRes: Int) {
        window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(
                this,
                windowBackgroundColorRes)))
    }

    override fun setBottomBarBlurOverlayColorRes(bottomBarBlurOverlayRes: Int) {
        bottomBarBlurView.setOverlayColor(ContextCompat.getColor(
                this,
                bottomBarBlurOverlayRes))
    }

    private fun createUserAction(): MainActivityContract.UserAction {
        val fileCreatorManager = ApplicationGraph.getFileCreatorManager()
        val themeManager = ApplicationGraph.getThemeManager()
        return MainActivityPresenter(
                this,
                fileCreatorManager,
                themeManager
        )
    }
}
