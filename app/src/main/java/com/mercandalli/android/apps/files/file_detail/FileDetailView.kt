package com.mercandalli.android.apps.files.file_detail

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import com.mercandalli.android.apps.files.R
import com.mercandalli.android.apps.files.main.ApplicationGraph
import com.mercandalli.sdk.files.api.File

class FileDetailView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr), FileDetailContract.Screen {

    private val userAction: FileDetailContract.UserAction
    private val title: TextView
    private val path: TextView
    private val length: TextView
    private val playPause: TextView

    init {
        View.inflate(context, R.layout.view_file_detail, this)
        setBackgroundColor(ContextCompat.getColor(context, R.color.file_detail_background))
        title = findViewById(R.id.view_file_detail_title)
        path = findViewById(R.id.view_file_detail_path)
        length = findViewById(R.id.view_file_detail_length)
        playPause = findViewById(R.id.view_file_detail_play_pause)
        userAction = createUserAction()

        playPause.setOnClickListener {
            userAction.onPlayPauseClicked()
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
        this.path.text = path
    }

    override fun setLength(length: String) {
        this.length.text = length
    }

    override fun showPlayPauseButton() {
        playPause.visibility = VISIBLE
    }

    override fun hidePlayPauseButton() {
        playPause.visibility = GONE
    }

    override fun setPlayPauseButtonText(stringRes: Int) {
        playPause.setText(stringRes)
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
            }
        }
        val audioManager = ApplicationGraph.getAudioManager()
        return FileDetailPresenter(
                this,
                audioManager,
                R.string.view_file_detail_play,
                R.string.view_file_detail_pause
        )
    }
}