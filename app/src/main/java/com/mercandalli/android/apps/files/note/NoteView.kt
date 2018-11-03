package com.mercandalli.android.apps.files.note

import android.content.Context
import androidx.core.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import com.mercandalli.android.apps.files.R
import com.mercandalli.android.apps.files.common.DialogUtils
import com.mercandalli.android.apps.files.main.ApplicationGraph

class NoteView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr),
    NoteContract.Screen {

    private val view = View.inflate(context, R.layout.view_note, this)
    private val editText: EditText = view.findViewById(R.id.view_note_input)
    private val userAction = createUserAction()

    init {
        editText.addTextChangedListener(createTextWatcher())
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        userAction.onAttached()
    }

    override fun onDetachedFromWindow() {
        userAction.onDetached()
        super.onDetachedFromWindow()
    }

    override fun setNote(note: String) {
        editText.setText(note)
    }

    override fun showDeleteConfirmation() {
        DialogUtils.alert(
            context,
            "Delete note?",
            "Do you want note", "Yes",
            object : DialogUtils.OnDialogUtilsListener {
                override fun onDialogUtilsCalledBack() {
                    userAction.onDeleteConfirmedClicked()
                }
            },
            "No",
            null
        )
    }

    override fun setTextColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        editText.setTextColor(color)
    }

    override fun setTextHintColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        editText.setHintTextColor(color)
    }

    override fun setCardBackgroundColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        editText.setBackgroundColor(color)
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
                override fun onAttached() {}
                override fun onDetached() {}
                override fun onTextChanged(text: String) {}
                override fun onShareClicked() {}
                override fun onDeleteClicked() {}
                override fun onDeleteConfirmedClicked() {}
            }
        }
        val noteManager = ApplicationGraph.getNoteManager()
        val themeManager = ApplicationGraph.getThemeManager()
        return NotePresenter(
            this,
            noteManager,
            themeManager
        )
    }

    private fun createTextWatcher() = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            userAction.onTextChanged(s!!.toString())
        }
    }
}
