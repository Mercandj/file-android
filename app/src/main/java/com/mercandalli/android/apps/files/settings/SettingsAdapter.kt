package com.mercandalli.android.apps.files.settings

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.mercandalli.android.apps.files.settings_about.SettingsAboutView
import com.mercandalli.android.apps.files.settings_developer.SettingsDeveloperView
import com.mercandalli.android.apps.files.settings_dynamic.SettingsDynamicView
import com.mercandalli.android.apps.files.settings_full_version.SettingsFullVersionView
import com.mercandalli.android.apps.files.settings_storage.SettingsStorageView
import com.mercandalli.android.apps.files.settings_store.SettingsStoreView
import com.mercandalli.android.apps.files.settings_theme.SettingsThemeView

class SettingsAdapter : ListDelegationAdapter<List<Any>>() {

    init {
        delegatesManager.addDelegate(SettingsThemeAdapterDelegate() as AdapterDelegate<List<Any>>)
        delegatesManager.addDelegate(SettingsStorageAdapterDelegate() as AdapterDelegate<List<Any>>)
        delegatesManager.addDelegate(SettingsDynamicAdapterDelegate() as AdapterDelegate<List<Any>>)
        delegatesManager.addDelegate(SettingsDeveloperAdapterDelegate() as AdapterDelegate<List<Any>>)
        delegatesManager.addDelegate(SettingsAboutAdapterDelegate() as AdapterDelegate<List<Any>>)
        delegatesManager.addDelegate(SettingsStoreAdapterDelegate() as AdapterDelegate<List<Any>>)
        delegatesManager.addDelegate(SettingsFullVersionAdapterDelegate() as AdapterDelegate<List<Any>>)
    }

    fun populate(list: List<Any>) {
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

    //region SettingsStorage
    class SettingsStorage

    private class SettingsStorageAdapterDelegate :
        AbsListItemAdapterDelegate<Any, Any, SettingsStorageViewHolder>() {

        override fun isForViewType(o: Any, list: List<Any>, i: Int) = o is SettingsStorage

        override fun onCreateViewHolder(viewGroup: ViewGroup): SettingsStorageViewHolder {
            val view = SettingsStorageView(viewGroup.context)
            view.layoutParams = createDefaultRecyclerViewLayoutParam()
            return SettingsStorageViewHolder(view)
        }

        override fun onBindViewHolder(model: Any, titleViewHolder: SettingsStorageViewHolder, list: List<Any>) {}
    }

    private class SettingsStorageViewHolder(view: View) : RecyclerView.ViewHolder(view)
    //endregion SettingsStorage

    //region SettingsStore
    class SettingsStore

    private class SettingsStoreAdapterDelegate : AbsListItemAdapterDelegate<Any, Any, SettingsStoreViewHolder>() {

        override fun isForViewType(o: Any, list: List<Any>, i: Int) = o is SettingsStore

        override fun onCreateViewHolder(viewGroup: ViewGroup): SettingsStoreViewHolder {
            val view = SettingsStoreView(viewGroup.context)
            view.layoutParams = createDefaultRecyclerViewLayoutParam()
            return SettingsStoreViewHolder(view)
        }

        override fun onBindViewHolder(model: Any, titleViewHolder: SettingsStoreViewHolder, list: List<Any>) {}
    }

    private class SettingsStoreViewHolder(view: View) : RecyclerView.ViewHolder(view)
    //endregion SettingsStore

    //region SettingsFullVersion
    class SettingsFullVersion

    private class SettingsFullVersionAdapterDelegate : AbsListItemAdapterDelegate<Any, Any, SettingsFullVersionViewHolder>() {

        override fun isForViewType(o: Any, list: List<Any>, i: Int) = o is SettingsFullVersion

        override fun onCreateViewHolder(viewGroup: ViewGroup): SettingsFullVersionViewHolder {
            val view = SettingsFullVersionView(viewGroup.context)
            view.layoutParams = createDefaultRecyclerViewLayoutParam()
            return SettingsFullVersionViewHolder(view)
        }

        override fun onBindViewHolder(model: Any, titleViewHolder: SettingsFullVersionViewHolder, list: List<Any>) {}
    }

    private class SettingsFullVersionViewHolder(view: View) : RecyclerView.ViewHolder(view)
    //endregion SettingsFullVersion

    //region SettingsDynamic
    class SettingsDynamic

    private class SettingsDynamicAdapterDelegate :
        AbsListItemAdapterDelegate<Any, Any, SettingsDynamicViewHolder>() {

        override fun isForViewType(o: Any, list: List<Any>, i: Int) = o is SettingsDynamic

        override fun onCreateViewHolder(viewGroup: ViewGroup): SettingsDynamicViewHolder {
            val view = SettingsDynamicView(viewGroup.context)
            view.layoutParams = createDefaultRecyclerViewLayoutParam()
            return SettingsDynamicViewHolder(view)
        }

        override fun onBindViewHolder(model: Any, titleViewHolder: SettingsDynamicViewHolder, list: List<Any>) {}
    }

    private class SettingsDynamicViewHolder(view: View) : RecyclerView.ViewHolder(view)
    //endregion SettingsDynamic

    //region SettingsDeveloper
    class SettingsDeveloper

    private class SettingsDeveloperAdapterDelegate :
        AbsListItemAdapterDelegate<Any, Any, SettingsDeveloperViewHolder>() {

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
