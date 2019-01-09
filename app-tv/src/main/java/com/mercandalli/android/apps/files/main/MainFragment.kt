package com.mercandalli.android.apps.files.main

import java.util.Timer
import java.util.TimerTask

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.leanback.app.BackgroundManager
import androidx.leanback.app.BrowseFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.leanback.widget.Row
import androidx.leanback.widget.RowPresenter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.OnItemViewSelectedListener

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.mercandalli.android.apps.files.selection_dialog.SelectionDialogActivity
import com.mercandalli.android.apps.files.R
import com.mercandalli.sdk.files.api.File

/**
 * Loads a grid of cards with movies to browse.
 */
class MainFragment : BrowseFragment(),
    MainFragmentContract.Screen {

    private val userAction = createUserAction()
    private val handler = Handler()
    private lateinit var backgroundManager: BackgroundManager
    private var defaultBackground: Drawable? = null
    private lateinit var metrics: DisplayMetrics
    private var backgroundTimer: Timer? = null
    private var backgroundUri: String? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        prepareBackgroundManager()
        setupUIElements()
        loadRows(
            listOf(
                MainFileRowViewModel(
                    "Local Files",
                    listOf(
                        MainFileViewModel.create(
                            File.create(java.io.File("/"))
                        )
                    )
                )
            )
        )
        setupEventListeners()

        userAction.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        backgroundTimer?.cancel()
        userAction.onDestroy()
    }

    override fun showFiles(mainFileRowViewModels: List<MainFileRowViewModel>) {
        loadRows(mainFileRowViewModels)
    }

    private fun prepareBackgroundManager() {
        backgroundManager = BackgroundManager.getInstance(activity)
        backgroundManager.attach(activity.window)
        defaultBackground = ContextCompat.getDrawable(activity, R.drawable.default_background)
        metrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metrics)
    }

    private fun setupUIElements() {
        title = getString(R.string.browse_title)
        headersState = BrowseFragment.HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
        brandColor = ContextCompat.getColor(activity, R.color.fastlane_background)
        searchAffordanceColor = ContextCompat.getColor(activity, R.color.search_opaque)
        view!!.setBackgroundColor(Color.BLACK)
    }

    private fun loadRows(mainFileRowViewModels: List<MainFileRowViewModel> = ArrayList()) {
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        val cardPresenter = MainCardPresenter()
        for ((i, mainFileRow) in mainFileRowViewModels.withIndex()) {
            val listRowAdapter = ArrayObjectAdapter(cardPresenter)
            for (file in mainFileRow.files) {
                listRowAdapter.add(file)
            }
            val header = HeaderItem(i.toLong(), mainFileRow.title)
            val listRow = ListRow(header, listRowAdapter)
            rowsAdapter.add(listRow)
        }

        val gridHeader = HeaderItem(NUM_ROWS.toLong(), "Settings")

        val gridPresenter = GridItemPresenter()
        val gridRowAdapter = ArrayObjectAdapter(gridPresenter)
        gridRowAdapter.add(resources.getString(R.string.personal_settings))
        rowsAdapter.add(ListRow(gridHeader, gridRowAdapter))

        adapter = rowsAdapter
    }

    private fun setupEventListeners() {
        setOnSearchClickedListener {
            SelectionDialogActivity.start(activity)
        }

        onItemViewClickedListener = OnItemViewClickedListener { _,
                                                                item,
                                                                _,
                                                                _ ->
            run {
                val fileViewModel = item as MainFileViewModel
                userAction.onFileClicked(fileViewModel)
            }
        }
        onItemViewSelectedListener = ItemViewSelectedListener()
    }

    private inner class ItemViewSelectedListener : OnItemViewSelectedListener {
        override fun onItemSelected(
            itemViewHolder: Presenter.ViewHolder?,
            item: Any?,
            rowViewHolder: RowPresenter.ViewHolder,
            row: Row
        ) {
            if (item is MainFileViewModel) {
                backgroundUri = item.backgroundImageUrl
                startBackgroundTimer()
            }
        }
    }

    private fun updateBackground(uri: String?) {
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        Glide.with(activity)
            .load(uri)
            .centerCrop()
            .error(defaultBackground)
            .into<SimpleTarget<GlideDrawable>>(
                object : SimpleTarget<GlideDrawable>(width, height) {
                    override fun onResourceReady(
                        resource: GlideDrawable,
                        glideAnimation: GlideAnimation<in GlideDrawable>
                    ) {
                        backgroundManager.drawable = resource
                    }
                })
        backgroundTimer?.cancel()
    }

    private fun startBackgroundTimer() {
        backgroundTimer?.cancel()
        backgroundTimer = Timer()
        backgroundTimer?.schedule(UpdateBackgroundTask(), BACKGROUND_UPDATE_DELAY.toLong())
    }

    private inner class UpdateBackgroundTask : TimerTask() {

        override fun run() {
            handler.post { updateBackground(backgroundUri) }
        }
    }

    private inner class GridItemPresenter : Presenter() {
        override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {
            val view = TextView(parent.context)
            view.layoutParams = ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT)
            view.isFocusable = true
            view.isFocusableInTouchMode = true
            view.setBackgroundColor(ContextCompat.getColor(activity, R.color.main_card_background))
            view.setTextColor(Color.WHITE)
            view.gravity = Gravity.CENTER
            return Presenter.ViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
            (viewHolder.view as TextView).text = item as String
        }

        override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {}
    }

    private fun createUserAction(): MainFragmentContract.UserAction {
        val fileChildrenManager = ApplicationGraph.getFileChildrenManager()
        val fileOpenManager = ApplicationGraph.getFileOpenManager()
        val fileSortManager = ApplicationGraph.getFileSortManager()
        val currentPath = Environment.getExternalStorageDirectory().absolutePath
        return MainFragmentPresenter(
            this,
            fileChildrenManager,
            fileOpenManager,
            fileSortManager,
            currentPath
        )
    }

    companion object {
        private const val BACKGROUND_UPDATE_DELAY = 300
        private const val GRID_ITEM_WIDTH = 200
        private const val GRID_ITEM_HEIGHT = 200
        private const val NUM_ROWS = 1
    }
}
