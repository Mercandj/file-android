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

    init {
        View.inflate(context, R.layout.view_file_detail, this)
        setBackgroundColor(ContextCompat.getColor(context, R.color.file_detail_background))
        title = findViewById(R.id.view_file_detail_title)
        path = findViewById(R.id.view_file_detail_path)
        userAction = createUserAction()
    }

    override fun setTitle(title: String) {
        this.title.text = title
    }

    override fun setPath(path: String) {
        this.path.text = path
    }

    fun setFile(file: File?) {
        userAction.onFileChanged(file)
    }

    private fun createUserAction(): FileDetailContract.UserAction {
        if (isInEditMode) {
            return object : FileDetailContract.UserAction {
                override fun onFileChanged(file: File?) {}
            }
        }
        val audioManager = ApplicationGraph.getAudioManager()
        return FileDetailPresenter(
                this,
                audioManager
        )
    }
}