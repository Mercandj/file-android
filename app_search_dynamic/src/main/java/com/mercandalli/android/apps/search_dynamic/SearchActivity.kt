@file:Suppress("UnusedImport")

package com.mercandalli.android.apps.search_dynamic

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.google.android.play.core.splitcompat.SplitCompat
import com.mercandalli.android.apps.files.activity.ActivityExtension.bind
import com.mercandalli.android.apps.files.keyboard.KeyboardUtils

class SearchActivity : Activity(),
    SearchActivityContract.Screen {

    private val back: View by bind(R.id.activity_search_toolbar_back)
    private val input: EditText by bind(R.id.activity_search_input)
    private val recyclerView: RecyclerView by bind(R.id.activity_search_recycler_view)
    private val userAction = createUserAction()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SplitCompat.install(this)
        setContentView(R.layout.activity_search)
        back.setOnClickListener {
            userAction.onBackClicked()
        }
        userAction.onCreate()
    }

    override fun onDestroy() {
        userAction.onDestroy()
        super.onDestroy()
    }

    override fun showKeyboard() {
        input.requestFocus()
        KeyboardUtils.showSoftInput(input)
    }

    override fun quit() {
        finish()
    }

    private fun createUserAction(): SearchActivityContract.UserAction {
        val fileSearchManager = SearchGraph.getFileSearchManager()
        return SearchActivityPresenter(
            this,
            fileSearchManager
        )
    }
}
