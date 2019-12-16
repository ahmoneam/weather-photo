package com.ahmoneam.basecleanarchitecture.utils.widget

import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ahmoneam.basecleanarchitecture.base.data.model.Markable
import java.lang.ref.WeakReference

abstract class BaseSelectableListAdapter<T, VH : BaseSelectableViewHolder<Markable<T>>> :
    ListAdapter<Markable<T>, VH> {

    var multipleSelect: Boolean = false

    val selectedItems: List<Markable<T>>
        get() = currentList.filter { it.isSelected }

    constructor(diffCallback: DiffUtil.ItemCallback<Markable<T>>) : super(diffCallback)

    constructor(config: AsyncDifferConfig<Markable<T>>) : super(config)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val viewHolder = getViewHolder(parent, viewType)
        viewHolder.multipleSelect = multipleSelect
        viewHolder.adapter = WeakReference(this)
        return viewHolder
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    abstract fun getViewHolder(parent: ViewGroup, viewType: Int): VH

    fun clearAllSelectedItems() {
        currentList.forEach { it.isSelected = false }
        notifyDataSetChanged()
    }
}

abstract class BaseSelectableViewHolder<T : Markable<*>>(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    var multipleSelect: Boolean = false
    var adapter: WeakReference<BaseSelectableListAdapter<*, *>>? = null

    constructor(viewDataBinding: ViewDataBinding) : this(viewDataBinding.root)

    @CallSuper
    fun bind(item: T) {
        itemView.setOnClickListener {
            (adapter?.get() as? BaseSelectableListAdapter<out Any?, out BaseSelectableViewHolder<Markable<*>>>)?.let { listAdapter: BaseSelectableListAdapter<out Any?, out BaseSelectableViewHolder<Markable<*>>> ->
                item.let {
                    if (it.selectable) {
                        if (!multipleSelect) {
                            if (adapter != null && adapter!!.get() != null) {
                                if (it.isSelected) {
                                    listAdapter.clearAllSelectedItems()
                                } else {
                                    listAdapter.clearAllSelectedItems()
                                    it.isSelected = it.isSelected.not()
                                }
                            }
                        } else {
                            it.isSelected = !it.isSelected
                        }
                        listAdapter.notifyItemChanged(adapterPosition)
                        onSelectChange(it.isSelected, it)
                    }
                }
            }
        }
        bindView(item)
    }

    abstract fun onSelectChange(selected: Boolean, item: T)

    abstract fun bindView(item: T)
}
