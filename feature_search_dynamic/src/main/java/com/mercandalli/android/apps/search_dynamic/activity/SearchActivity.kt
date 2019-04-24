@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.search_dynamic.activity

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import com.google.android.play.core.splitcompat.SplitCompat
import com.mercandalli.android.apps.files.activity.ActivityExtension.bind
import com.mercandalli.android.apps.files.keyboard.KeyboardUtils
import com.mercandalli.android.apps.search_dynamic.R
import com.mercandalli.android.apps.search_dynamic.SearchGraph
import com.mercandalli.android.apps.search_dynamic.list.SearchListView
import com.mercandalli.sdk.files.api.File

class SearchActivity : Activity(),
    SearchActivityContract.Screen {

    private val back: View by bind(R.id.activity_search_toolbar_back)
    private val input: EditText by bind(R.id.activity_search_input)
    private val searchIcon: View by bind(R.id.activity_search_toolbar_search_icon)
    private val searchListView: SearchListView by bind(R.id.activity_search_list)

    private val userAction = createUserAction()
    private val inputTextWatcher = createInputTextWatcher()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SplitCompat.install(this)
        setContentView(R.layout.activity_search)
        back.setOnClickListener {
            userAction.onBackClicked()
        }
        input.setOnEditorActionListener(createOnEditorActionListener())
        input.addTextChangedListener(inputTextWatcher)
        input.setOnFocusChangeListener { _, hasFocus ->
            userAction.onInputFocusChanged(hasFocus)
        }
        searchIcon.setOnClickListener {
            userAction.onSearchIconClicked(input.text.toString())
        }
        userAction.onCreate()
    }

    override fun onDestroy() {
        userAction.onDestroy()
        super.onDestroy()
    }

    override fun populate(list: List<File>) {
        searchListView.populate(list)
    }

    override fun showKeyboard() {
        input.requestFocus()
        KeyboardUtils.showSoftInput(input)
    }

    override fun hideKeyBoard() {
        KeyboardUtils.hideSoftInput(input)
    }

    override fun quit() {
        finish()
    }

    private fun onEditorAction(actionId: Int) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            userAction.onSearchPerformed(input.text.toString())
        }
    }

    private fun createOnEditorActionListener() = TextView.OnEditorActionListener { _, actionId, _ ->
        this@SearchActivity.onEditorAction(actionId)
        true
    }

    private fun createInputTextWatcher() = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            userAction.onInputChanged(s)
        }

        override fun afterTextChanged(s: Editable) {
        }
    }

    private fun createUserAction(): SearchActivityContract.UserAction {
        val fileSearchManager = SearchGraph.getFileSearchManager()
        return SearchActivityPresenter(
            this,
            fileSearchManager
        )
    }
}
