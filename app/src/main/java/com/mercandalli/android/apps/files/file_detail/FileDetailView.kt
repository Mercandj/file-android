package com.mercandalli.android.apps.files.file_detail

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import com.mercandalli.android.apps.files.R
import com.mercandalli.android.apps.files.common.DialogUtils
import com.mercandalli.android.apps.files.main.ApplicationGraph
import com.mercandalli.sdk.files.api.File

class FileDetailView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr), FileDetailContract.Screen {

    private val userAction: FileDetailContract.UserAction
    private val title: TextView
    private val path: TextView
    private val length: TextView
    private val lastModified: TextView
    private val playPause: TextView
    private val next: View
    private val previous: View
    private val delete: View

    init {
        View.inflate(context, R.layout.view_file_detail, this)
        setBackgroundColor(ContextCompat.getColor(context, R.color.file_detail_background))
        title = findViewById(R.id.view_file_detail_title)
        path = findViewById(R.id.view_file_detail_path)
        length = findViewById(R.id.view_file_detail_length)
        lastModified = findViewById(R.id.view_file_detail_last_modified)
        playPause = findViewById(R.id.view_file_detail_play_pause)
        next = findViewById(R.id.view_file_detail_play_next)
        previous = findViewById(R.id.view_file_detail_play_previous)
        delete = findViewById(R.id.view_file_detail_delete)
        userAction = createUserAction()

        playPause.setOnClickListener {
            userAction.onPlayPauseClicked()
        }
        next.setOnClickListener {
            userAction.onNextClicked()
        }
        previous.setOnClickListener {
            userAction.onPreviousClicked()
        }
        delete.setOnClickListener {
            userAction.onDeleteClicked()
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

    override fun setPath(path: String) {
        this.path.text = context.getString(R.string.view_file_detail_path, path)
    }

    override fun setLength(length: String) {
        this.length.text = context.getString(R.string.view_file_detail_size, length)
    }

    override fun setLastModified(lastModifiedDateString: String) {
        this.lastModified.text = context.getString(
                R.string.view_file_detail_last_modified, lastModifiedDateString)
    }

    override fun showPlayPauseButton() {
        playPause.visibility = VISIBLE
    }

    override fun hidePlayPauseButton() {
        playPause.visibility = GONE
    }

    override fun showNextButton() {
        next.visibility = VISIBLE
    }

    override fun hideNextButton() {
        next.visibility = GONE
    }

    override fun showPreviousButton() {
        previous.visibility = VISIBLE
    }

    override fun hidePreviousButton() {
        previous.visibility = GONE
    }

    override fun setPlayPauseButtonText(stringRes: Int) {
        playPause.setText(stringRes)
    }

    override fun showDeleteConfirmation(fileName: String) {
        DialogUtils.alert(context, "Delete file?",
                "Do you want to delete: $fileName", "Yes",
                { userAction.onDeleteConfirmedClicked() }, "No", {}
        )
    }

    fun setFile(file: File?) {
        userAction.onFileChanged(file)
    }

    private fun createUserAction(): FileDetailContract.UserAction {
        if (isInEditMode) {
            return object : FileDetailContract.UserAction {
                override fun onAttached() {}
                override fun onDetached() {}
                override fun onFileChanged(file: File?) {}
                override fun onPlayPauseClicked() {}
                override fun onNextClicked() {}
                override fun onPreviousClicked() {}
                override fun onDeleteClicked() {}
                override fun onDeleteConfirmedClicked() {}
            }
        }
        val audioManager = ApplicationGraph.getAudioManager()
        val audioQueueManager = ApplicationGraph.getAudioQueueManager()
        val fileDeleteManager = ApplicationGraph.getFileDeleteManager()
        return FileDetailPresenter(
                this,
                audioManager,
                audioQueueManager,
                fileDeleteManager,
                R.string.view_file_detail_play,
                R.string.view_file_detail_pause
        )
    }
}