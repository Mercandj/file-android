package com.mercandalli.android.apps.files.bottom_bar

import android.content.Context
import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.mercandalli.android.apps.files.R

class BottomBar @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), BottomBarContract.Screen {

    private val userAction: BottomBarContract.UserAction
    private val sectionIconFile: ImageView
    private val sectionIconNote: ImageView
    private val sectionIconSettings: ImageView
    private var clickListener: OnBottomBarClickListener? = null

    init {
        View.inflate(context, R.layout.view_bottom_bar, this)
        sectionIconFile = findViewById(R.id.view_bottom_bar_section_icon_file)
        sectionIconNote = findViewById(R.id.view_bottom_bar_section_icon_note)
        sectionIconSettings = findViewById(R.id.view_bottom_bar_section_icon_settings)

        userAction = BottomBarPresenter(
                this,
                ContextCompat.getColor(context, R.color.bottom_bar_selected),
                ContextCompat.getColor(context, R.color.bottom_bar_not_selected)
        )

        findViewById<View>(R.id.view_bottom_bar_section_file).setOnClickListener {
            userAction.onFileClicked()
        }
        findViewById<View>(R.id.view_bottom_bar_section_note).setOnClickListener {
            userAction.onNoteClicked()
        }
        findViewById<View>(R.id.view_bottom_bar_section_settings).setOnClickListener {
            userAction.onSettingsClicked()
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
        sectionIconFile.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    }

    override fun setNoteIconColor(color: Int) {
        sectionIconNote.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    }

    override fun setSettingsIconColor(color: Int) {
        sectionIconSettings.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    }

    fun setOnBottomBarClickListener(listener: OnBottomBarClickListener?) {
        clickListener = listener
    }

    interface OnBottomBarClickListener {

        fun onFileSectionClicked()

        fun onNoteSectionClicked()

        fun onSettingsSectionClicked()
    }
}