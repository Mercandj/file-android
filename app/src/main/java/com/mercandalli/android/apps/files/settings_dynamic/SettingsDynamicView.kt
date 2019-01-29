@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.settings_dynamic

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.mercandalli.android.apps.files.R
import com.mercandalli.android.apps.files.main.ApplicationGraph
import com.mercandalli.android.apps.files.split_install.SplitFeature
import com.mercandalli.android.apps.files.split_install.SplitInstallManager

class SettingsDynamicView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr),
    SettingsDynamicContract.Screen {

    private val view = LayoutInflater.from(context).inflate(R.layout.view_settings_dynamic_feature, this)
    private val dynamicSection: CardView = view.findViewById(R.id.view_settings_dynamic_feature_section)
    private val label: TextView = view.findViewById(R.id.view_settings_dynamic_feature_search_label)
    private val sublabel: TextView = view.findViewById(R.id.view_settings_dynamic_feature_search_sublabel)
    private val uninstall: Button = view.findViewById(R.id.view_settings_dynamic_feature_search_uninstall)
    private val splitInstallManager by lazy {
        ApplicationGraph.getSplitInstallManager()
    }
    private val listener = createSplitInstallStateUpdatedListener()
    private val userAction = createUserAction()

    init {
        orientation = LinearLayout.VERTICAL
        uninstall.setOnClickListener {
            splitInstallManager.deferredUninstall(SplitFeature.Search)
            syncView()
        }
        syncView()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        userAction.onAttached()
        splitInstallManager.registerListener(SplitFeature.Search, listener)
    }

    override fun onDetachedFromWindow() {
        userAction.onDetached()
        splitInstallManager.unregisterListener(SplitFeature.Search, listener)
        super.onDetachedFromWindow()
    }

    override fun setTextPrimaryColorRes(colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        label.setTextColor(color)
    }

    override fun setTextSecondaryColorRes(colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        sublabel.setTextColor(color)
    }

    override fun setSectionColor(colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        dynamicSection.setCardBackgroundColor(color)
    }

    private fun syncView() {
        val installed = splitInstallManager.isInstalled(SplitFeature.Search)
        if (installed) {
            uninstall.visibility = View.VISIBLE
            val uninstallTriggered = splitInstallManager.isUninstallTriggered(SplitFeature.Search)
            val buttonText = if (uninstallTriggered) {
                "Pending search uninstall"
            } else {
                "Defer search uninstall"
            }
            uninstall.text = buttonText
        } else {
            uninstall.visibility = View.GONE
        }
    }

    private fun createSplitInstallStateUpdatedListener() = object : SplitInstallManager.SplitInstallListener {
        override fun onUninstalled(splitFeature: SplitFeature) {
            syncView()
        }

        override fun onDownloading(splitFeature: SplitFeature) {
            syncView()
        }

        override fun onInstalled(splitFeature: SplitFeature) {
            syncView()
        }

        override fun onFailed(splitFeature: SplitFeature) {
            syncView()
        }
    }

    private fun createUserAction(): SettingsDynamicContract.UserAction {
        if (isInEditMode) {
            return object : SettingsDynamicContract.UserAction {
                override fun onAttached() {}
                override fun onDetached() {}
            }
        }
        val splitInstallManager = ApplicationGraph.getSplitInstallManager()
        val themeManager = ApplicationGraph.getThemeManager()
        return SettingsDynamicPresenter(
            this,
            splitInstallManager,
            themeManager
        )
    }
}
