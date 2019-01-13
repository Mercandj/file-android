package com.mercandalli.android.apps.files.main

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter

import com.mercandalli.android.apps.files.R
import kotlin.properties.Delegates

/**
 * A CardPresenter is used to generate Views and bind Objects to them on demand.
 * It contains an ImageCardView.
 */
class MainCardPresenter : Presenter() {

    private var defaultDirectoryCardImage: Drawable? = null
    private var defaultFileCardImage: Drawable? = null
    private var selectedBackgroundColor: Int by Delegates.notNull()
    private var defaultBackgroundColor: Int by Delegates.notNull()

    override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {
        defaultBackgroundColor = ContextCompat.getColor(parent.context, R.color.main_card_background)
        selectedBackgroundColor = ContextCompat.getColor(parent.context, R.color.selected_background)
        defaultDirectoryCardImage = ContextCompat.getDrawable(parent.context, R.drawable.card_directory)
        defaultFileCardImage = ContextCompat.getDrawable(parent.context, R.drawable.card_file)

        val cardView = object : ImageCardView(parent.context) {
            override fun setSelected(selected: Boolean) {
                updateCardBackgroundColor(this, selected)
                super.setSelected(selected)
            }
        }

        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true
        updateCardBackgroundColor(cardView, false)
        return Presenter.ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
        val mainFileViewModel = item as MainFileViewModel
        val cardView = viewHolder.view as ImageCardView
        cardView.titleText = mainFileViewModel.title
        cardView.contentText = mainFileViewModel.path
        cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT)

        val drawable = if (mainFileViewModel.directory) {
            defaultDirectoryCardImage
        } else {
            defaultFileCardImage
        }
        cardView.mainImageView.setImageDrawable(drawable)
        /*
        Glide.with(viewHolder.view.context)
            .load(drawable)
            .centerCrop()
            .error(drawable)
            .into(cardView.mainImageView)
            */
    }

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {
        val cardView = viewHolder.view as ImageCardView
        // Remove references to images so that the garbage collector can free up memory
        cardView.badgeImage = null
        cardView.mainImage = null
    }

    private fun updateCardBackgroundColor(view: ImageCardView, selected: Boolean) {
        val color = if (selected) selectedBackgroundColor else defaultBackgroundColor
        // Both background colors should be set because the view's background is temporarily visible
        // during animations.
        view.setBackgroundColor(color)
        view.setInfoAreaBackgroundColor(color)
    }

    companion object {
        private const val CARD_WIDTH = 313
        private const val CARD_HEIGHT = 176
    }
}
