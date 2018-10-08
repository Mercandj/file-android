package com.mercandalli.android.apps.files.settings

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.mercandalli.android.apps.files.settings_about.SettingsAboutView
import com.mercandalli.android.apps.files.settings_developer.SettingsDeveloperView
import com.mercandalli.android.apps.files.settings_theme.SettingsThemeView

class SettingsAdapter : ListDelegationAdapter<List<Any>>() {

    init {
        delegatesManager.addDelegate(SettingsThemeAdapterDelegate() as AdapterDelegate<List<Any>>)
        delegatesManager.addDelegate(SettingsDeveloperAdapterDelegate() as AdapterDelegate<List<Any>>)
        delegatesManager.addDelegate(SettingsAboutAdapterDelegate() as AdapterDelegate<List<Any>>)
        populate(listOf(
                SettingsTheme(),
                SettingsDeveloper(),
                SettingsAbout()
        ))
    }

    private fun populate(list: List<Any>) {
        setItems(list)
        notifyDataSetChanged()
    }

    //region SettingsTheme
    class SettingsTheme

    private class SettingsThemeAdapterDelegate : AbsListItemAdapterDelegate<Any, Any, SettingsThemeViewHolder>() {

        override fun isForViewType(o: Any, list: List<Any>, i: Int) = o is SettingsTheme

        override fun onCreateViewHolder(viewGroup: ViewGroup): SettingsThemeViewHolder {
            val view = SettingsThemeView(viewGroup.context)
            view.layoutParams = createDefaultRecyclerViewLayoutParam()
            return SettingsThemeViewHolder(view)
        }

        override fun onBindViewHolder(model: Any, titleViewHolder: SettingsThemeViewHolder, list: List<Any>) {}
    }

    private class SettingsThemeViewHolder(
            view: View
    ) : RecyclerView.ViewHolder(view)
    //endregion SettingsTheme

    //region SettingsDeveloper
    class SettingsDeveloper

    private class SettingsDeveloperAdapterDelegate : AbsListItemAdapterDelegate<Any, Any, SettingsDeveloperViewHolder>() {

        override fun isForViewType(o: Any, list: List<Any>, i: Int) = o is SettingsDeveloper

        override fun onCreateViewHolder(viewGroup: ViewGroup): SettingsDeveloperViewHolder {
            val view = SettingsDeveloperView(viewGroup.context)
            view.layoutParams = createDefaultRecyclerViewLayoutParam()
            return SettingsDeveloperViewHolder(view)
        }

        override fun onBindViewHolder(model: Any, titleViewHolder: SettingsDeveloperViewHolder, list: List<Any>) {}
    }

    private class SettingsDeveloperViewHolder(view: View) : RecyclerView.ViewHolder(view)
    //endregion SettingsDeveloper

    //region SettingsAbout
    class SettingsAbout

    private class SettingsAboutAdapterDelegate : AbsListItemAdapterDelegate<Any, Any, SettingsAboutViewHolder>() {

        override fun isForViewType(o: Any, list: List<Any>, i: Int) = o is SettingsAbout

        override fun onCreateViewHolder(viewGroup: ViewGroup): SettingsAboutViewHolder {
            val view = SettingsAboutView(viewGroup.context)
            view.layoutParams = createDefaultRecyclerViewLayoutParam()
            return SettingsAboutViewHolder(view)
        }

        override fun onBindViewHolder(model: Any, titleViewHolder: SettingsAboutViewHolder, list: List<Any>) {}
    }

    private class SettingsAboutViewHolder(view: View) : RecyclerView.ViewHolder(view)
    //endregion SettingsAbout

    companion object {

        private fun createDefaultRecyclerViewLayoutParam() = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
        )
    }
}