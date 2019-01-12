@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.bottom_bar

import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Parcelable
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import com.mercandalli.android.apps.files.R
import com.mercandalli.android.apps.files.main.ApplicationGraph

class BottomBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr),
    BottomBarContract.Screen {

    private val view = View.inflate(context, R.layout.view_bottom_bar, this)
    private val sectionFileIcon: ImageView = view.findViewById(R.id.view_bottom_bar_section_file_icon)
    private val sectionFileText: TextView = view.findViewById(R.id.view_bottom_bar_section_file_text)
    private val sectionOnline: View = view.findViewById(R.id.view_bottom_bar_online_section)
    private val sectionOnlineIcon: ImageView = view.findViewById(R.id.view_bottom_bar_section_online_icon)
    private val sectionOnlineText: TextView = view.findViewById(R.id.view_bottom_bar_section_online_text)
    private val sectionNoteIcon: ImageView = view.findViewById(R.id.view_bottom_bar_section_note_icon)
    private val sectionNoteText: TextView = view.findViewById(R.id.view_bottom_bar_section_note_text)
    private val sectionSettingsIcon: ImageView = view.findViewById(R.id.view_bottom_bar_section_settings_icon)
    private val sectionSettingsText: TextView = view.findViewById(R.id.view_bottom_bar_section_settings_text)
    private val userAction = createUserAction()

    private var clickListener: OnBottomBarClickListener? = null

    init {
        findViewById<View>(R.id.view_bottom_bar_file_section).setOnClickListener {
            userAction.onFileClicked()
        }
        sectionOnline.setOnClickListener {
            userAction.onOnlineClicked()
        }
        findViewById<View>(R.id.view_bottom_bar_note_section).setOnClickListener {
            userAction.onNoteClicked()
        }
        findViewById<View>(R.id.view_bottom_bar_settings_section).setOnClickListener {
            userAction.onSettingsClicked()
        }
        val horizontalEdgePadding = resources.getDimensionPixelSize(R.dimen.default_space)
        setPadding(horizontalEdgePadding, 0, horizontalEdgePadding, 0)
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

    override fun notifyListenerOnlineClicked() {
        clickListener?.onOnlineSectionClicked()
    }

    override fun notifyListenerNoteClicked() {
        clickListener?.onNoteSectionClicked()
    }

    override fun notifyListenerSettingsClicked() {
        clickListener?.onSettingsSectionClicked()
    }

    override fun showOnlineSection() {
        sectionOnline.visibility = VISIBLE
    }

    override fun hideOnlineSection() {
        sectionOnline.visibility = GONE
    }

    override fun setFileIconColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        sectionFileIcon.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    }

    override fun setOnlineIconColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        sectionOnlineIcon.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    }

    override fun setNoteIconColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        sectionNoteIcon.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    }

    override fun setSettingsIconColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        sectionSettingsIcon.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    }

    override fun setSectionFileTextColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        sectionFileText.setTextColor(color)
    }

    override fun setSectionOnlineTextColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        sectionOnlineText.setTextColor(color)
    }

    override fun setSectionNoteTextColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        sectionNoteText.setTextColor(color)
    }

    override fun setSectionSettingsTextColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        sectionSettingsText.setTextColor(color)
    }

    fun setOnBottomBarClickListener(listener: OnBottomBarClickListener?) {
        clickListener = listener
    }

    fun selectFile() {
        userAction.onSelectFile()
    }

    fun selectOnline() {
        userAction.onSelectOnline()
    }

    fun selectNote() {
        userAction.onSelectNote()
    }

    fun selectSettings() {
        userAction.onSelectSettings()
    }

    private fun createUserAction() = if (isInEditMode) {
        object : BottomBarContract.UserAction {
            override fun onAttached() {}
            override fun onDetached() {}
            override fun onSaveInstanceState(saveState: Bundle) {}
            override fun onRestoreInstanceState(state: Bundle) {}
            override fun onFileClicked() {}
            override fun onOnlineClicked() {}
            override fun onNoteClicked() {}
            override fun onSettingsClicked() {}
            override fun onSelectFile() {}
            override fun onSelectOnline() {}
            override fun onSelectNote() {}
            override fun onSelectSettings() {}
        }
    } else {
        val themeManager = ApplicationGraph.getThemeManager()
        val developerManager = ApplicationGraph.getDeveloperManager()
        val fileOnlineLoginManager = ApplicationGraph.getFileOnlineLoginManager()
        val selectedColorRes = R.color.bottom_bar_selected
        val notSelectedColorRes = R.color.bottom_bar_not_selected
        BottomBarPresenter(
            this,
            themeManager,
            developerManager,
            fileOnlineLoginManager,
            selectedColorRes,
            notSelectedColorRes
        )
    }

    interface OnBottomBarClickListener {

        fun onFileSectionClicked()

        fun onOnlineSectionClicked()

        fun onNoteSectionClicked()

        fun onSettingsSectionClicked()
    }
}
