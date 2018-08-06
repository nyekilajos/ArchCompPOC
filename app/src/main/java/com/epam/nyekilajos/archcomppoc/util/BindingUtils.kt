package com.epam.nyekilajos.archcomppoc.util

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.epam.nyekilajos.archcomppoc.ui.common.BindableAdapter

@BindingAdapter("itemDecoration")
fun setItemDecoration(recyclerView: RecyclerView, itemDecoration: RecyclerView.ItemDecoration) {
    recyclerView.addItemDecoration(itemDecoration)
}

@BindingAdapter("itemAnimator")
fun setItemAnimator(recyclerView: RecyclerView, animator: RecyclerView.ItemAnimator) {
    recyclerView.itemAnimator = animator
}

@BindingAdapter(value = ["adapter", "layoutManager", "data"], requireAll = false)
@Suppress("UNCHECKED_CAST")
fun <T> setRecyclerViewProperties(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>?, layoutManager: RecyclerView.LayoutManager?, data: T?) {
    if (adapter != null) {
        recyclerView.adapter = adapter
    }

    if (layoutManager != null) {
        recyclerView.layoutManager = layoutManager
        if (data != null) {
            if (recyclerView.adapter is BindableAdapter<*>) {
                (recyclerView.adapter as BindableAdapter<T>).setData(data)
            }
        }
    }
}
