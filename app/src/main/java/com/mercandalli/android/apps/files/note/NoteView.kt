package com.mercandalli.android.apps.files.note

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import com.mercandalli.android.apps.files.R
import com.mercandalli.android.apps.files.main.ApplicationGraph

class NoteView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), NoteContract.Screen {

    private val userAction: NoteContract.UserAction
    private val editText: EditText

    init {
        View.inflate(context, R.layout.view_note, this)
        editText = findViewById(R.id.view_note_input)
        userAction = createUserAction()
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                userAction.onTextChanged(s!!.toString())
            }
        })
    }

    override fun setNote(note: String) {
        editText.setText(note)
    }

    fun onShareClicked() {
        userAction.onShareClicked()
    }

    fun onDeleteClicked() {
        userAction.onDeleteClicked()
    }

    private fun createUserAction(): NoteContract.UserAction {
        if (isInEditMode) {
            return object : NoteContract.UserAction {
                override fun onShareClicked() {}
                override fun onDeleteClicked() {}
                override fun onTextChanged(text: String) {}
            }
        }
        val noteManager = ApplicationGraph.getNoteManager()
        return NotePresenter(
                this,
                noteManager
        )
    }
}