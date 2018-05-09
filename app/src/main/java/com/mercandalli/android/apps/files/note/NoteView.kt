package com.mercandalli.android.apps.files.note

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.mercandalli.android.apps.files.R

class NoteView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), NoteContract.Screen {

    init {
        View.inflate(context, R.layout.view_note, this)

    }
}