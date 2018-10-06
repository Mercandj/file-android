package com.mercandalli.android.apps.files.settings

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.mercandalli.android.apps.files.settings.about.SettingsAboutView
import com.mercandalli.android.apps.files.settings.theme.SettingsThemeView

class SettingsAdapter : ListDelegationAdapter<List<Any>>() {

    init {
        delegatesManager.addDelegate(SettingsThemeAdapterDelegate() as AdapterDelegate<List<Any>>)
        delegatesManager.addDelegate(SettingsAboutAdapterDelegate() as AdapterDelegate<List<Any>>)
        populate(listOf(
                SettingsTheme(),
                SettingsAbout()
        ))
    }

    private fun populate(list: List<Any>) {
        setItems(list)
        notifyDataSetChanged()
    }

    //region SettingsTheme
    class SettingsTheme

    private class SettingsThemeAdapterDelegate :
            AbsListItemAdapterDelegate<Any, Any, SettingsThemeViewHolder>() {

        override fun isForViewType(o: Any, list: List<Any>, i: Int): Boolean {
            return o is SettingsTheme
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup): SettingsThemeViewHolder {
            val context = viewGroup.context
            val view = SettingsThemeView(context)
            val layoutParams = RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT
            )
            view.layoutParams = layoutParams
            return SettingsThemeViewHolder(view)
        }

        override fun onBindViewHolder(
                model: Any, titleViewHolder: SettingsThemeViewHolder, list: List<Any>
        ) {
        }
    }

    private class SettingsThemeViewHolder(
            view: View
    ) : RecyclerView.ViewHolder(view)
    //endregion SettingsTheme

    //region SettingsAbout
    class SettingsAbout

    private class SettingsAboutAdapterDelegate :
            AbsListItemAdapterDelegate<Any, Any, SettingsAboutViewHolder>() {

        override fun isForViewType(o: Any, list: List<Any>, i: Int): Boolean {
            return o is SettingsAbout
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup): SettingsAboutViewHolder {
            val context = viewGroup.context
            val view = SettingsAboutView(context)
            val layoutParams = RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT
            )
            view.layoutParams = layoutParams
            return SettingsAboutViewHolder(view)
        }

        override fun onBindViewHolder(
                model: Any, titleViewHolder: SettingsAboutViewHolder, list: List<Any>
        ) {
        }
    }

    private class SettingsAboutViewHolder(
            view: View
    ) : RecyclerView.ViewHolder(view)
    //endregion SettingsAbout

}