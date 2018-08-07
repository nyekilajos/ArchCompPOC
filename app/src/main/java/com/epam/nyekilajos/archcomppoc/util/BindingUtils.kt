package com.epam.nyekilajos.archcomppoc.util

import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.navigation.Navigation
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

@BindingAdapter("navigateToOnClick")
fun setNavigateToOnClick(view: View, navigateToOnClick: Int) {
    view.setOnClickListener(Navigation.createNavigateOnClickListener(navigateToOnClick))
}

@BindingAdapter("onImeActionDone")
fun setOnImeActionDone(editText: EditText, onImeActionDone: Runnable) {
    editText.setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            onImeActionDone.run()
        }
        false
    }
}
