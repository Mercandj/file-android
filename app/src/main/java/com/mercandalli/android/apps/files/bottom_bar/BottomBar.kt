package com.mercandalli.android.apps.files.bottom_bar

import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.mercandalli.android.apps.files.R
import com.mercandalli.android.apps.files.main.ApplicationGraph

class BottomBar @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), BottomBarContract.Screen {

    private val userAction: BottomBarContract.UserAction
    private val sectionFileIcon: ImageView
    private val sectionFileText: TextView
    private val sectionNoteIcon: ImageView
    private val sectionNoteText: TextView
    private val sectionSettingsIcon: ImageView
    private val sectionSettingsText: TextView
    private var clickListener: OnBottomBarClickListener? = null

    init {
        View.inflate(context, R.layout.view_bottom_bar, this)
        sectionFileIcon = findViewById(R.id.view_bottom_bar_section_file_icon)
        sectionFileText = findViewById(R.id.view_bottom_bar_section_file_text)
        sectionNoteIcon = findViewById(R.id.view_bottom_bar_section_note_icon)
        sectionNoteText = findViewById(R.id.view_bottom_bar_section_note_text)
        sectionSettingsIcon = findViewById(R.id.view_bottom_bar_section_settings_icon)
        sectionSettingsText = findViewById(R.id.view_bottom_bar_section_settings_text)
        userAction = createUserAction()
        findViewById<View>(R.id.view_bottom_bar_file_section).setOnClickListener {
            userAction.onFileClicked()
        }
        findViewById<View>(R.id.view_bottom_bar_note_section).setOnClickListener {
            userAction.onNoteClicked()
        }
        findViewById<View>(R.id.view_bottom_bar_settings_section).setOnClickListener {
            userAction.onSettingsClicked()
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

    override fun onSaveInstanceState(): Parcelable {
        val savedInstance = super.onSaveInstanceState()
        val saveState = Bundle()
        saveState.putParcelable("BUNDLE_KEY_SAVED_INSTANCE", savedInstance)
        userAction.onSaveInstanceState(saveState)
        return saveState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            val savedInstance = state.getParcelable<Parcelable>("BUNDLE_KEY_SAVED_INSTANCE")
            super.onRestoreInstanceState(savedInstance)
            userAction.onRestoreInstanceState(state)
        } else {
            super.onRestoreInstanceState(View.BaseSavedState.EMPTY_STATE)
        }
    }

    override fun notifyListenerFileClicked() {
        clickListener?.onFileSectionClicked()
    }

    override fun notifyListenerNoteClicked() {
        clickListener?.onNoteSectionClicked()
    }

    override fun notifyListenerSettingsClicked() {
        clickListener?.onSettingsSectionClicked()
    }

    override fun setFileIconColor(color: Int) {
        sectionFileIcon.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    }

    override fun setNoteIconColor(color: Int) {
        sectionNoteIcon.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    }

    override fun setSettingsIconColor(color: Int) {
        sectionSettingsIcon.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    }

    override fun setSectionFileTextColorRes(textColorRes: Int) {
        sectionFileText.setTextColor(ContextCompat.getColor(context, textColorRes))
    }

    override fun setSectionNoteTextColorRes(textColorRes: Int) {
        sectionNoteText.setTextColor(ContextCompat.getColor(context, textColorRes))
    }

    override fun setSectionSettingsTextColorRes(textColorRes: Int) {
        sectionSettingsText.setTextColor(ContextCompat.getColor(context, textColorRes))
    }

    fun setOnBottomBarClickListener(listener: OnBottomBarClickListener?) {
        clickListener = listener
    }

    private fun createUserAction(): BottomBarContract.UserAction {
        if (isInEditMode) {
            return object : BottomBarContract.UserAction {
                override fun onAttached() {}
                override fun onDetached() {}
                override fun onSaveInstanceState(saveState: Bundle) {}
                override fun onRestoreInstanceState(state: Bundle) {}
                override fun onFileClicked() {}
                override fun onNoteClicked() {}
                override fun onSettingsClicked() {}
            }
        }
        val themeManager = ApplicationGraph.getThemeManager()
        return BottomBarPresenter(
                this,
                themeManager,
                ContextCompat.getColor(context, R.color.bottom_bar_selected),
                ContextCompat.getColor(context, R.color.bottom_bar_not_selected)
        )
    }

    interface OnBottomBarClickListener {

        fun onFileSectionClicked()

        fun onNoteSectionClicked()

        fun onSettingsSectionClicked()
    }
}