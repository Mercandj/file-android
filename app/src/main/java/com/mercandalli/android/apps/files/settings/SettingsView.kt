package com.mercandalli.android.apps.files.settings

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.mercandalli.android.apps.files.R
import com.mercandalli.android.apps.files.main.ApplicationGraph

class SettingsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val view = LayoutInflater.from(context).inflate(R.layout.view_settings, this)
    private val recyclerView: RecyclerView = view.findViewById(R.id.view_settings_recycler_view)
    private val adapter = SettingsAdapter()
    private val userAction = createUserAction()

    init {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        userAction.onAttached()
    }

    override fun onDetachedFromWindow() {
        userAction.onDetached()
        super.onDetachedFromWindow()
    }

    private fun createScreen() = object : SettingsViewContract.Screen {
        override fun populate(list: List<Any>) {
            adapter.populate(list)
        }
    }

    private fun createUserAction(): SettingsViewContract.UserAction {
        if (isInEditMode) {
            return object : SettingsViewContract.UserAction {
                override fun onAttached() {}
                override fun onDetached() {}
            }
        }
        val screen = createScreen()
        val developerManager = ApplicationGraph.getDeveloperManager()
        val productManager = ApplicationGraph.getProductManager()
        val remoteConfig = ApplicationGraph.getRemoteConfig()
        return SettingsViewPresenter(
            screen,
            developerManager,
            productManager,
            remoteConfig
        )
    }
}
