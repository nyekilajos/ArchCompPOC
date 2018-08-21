package com.epam.nyekilajos.archcomppoc.util

import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.epam.nyekilajos.archcomppoc.ui.common.BindableAdapter
import com.epam.nyekilajos.archcomppoc.ui.common.BindableSpinnerAdapter
import com.epam.nyekilajos.archcomppoc.ui.common.TitleProvider

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
        if (data != null && recyclerView.adapter is BindableAdapter<*>) {
            (recyclerView.adapter as BindableAdapter<T>).setData(data)
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

@Suppress("UNCHECKED_CAST")
@BindingAdapter(value = ["selectedItem", "selectedItemAttrChanged"], requireAll = false)
fun setSelectedItem(spinner: Spinner, item: Any?, inverseBindingListener: InverseBindingListener?) {
    (item as? TitleProvider)?.let {
        (spinner.adapter as? BindableSpinnerAdapter<TitleProvider>)?.getItemPosition(it)?.let { selection ->
            if (spinner.selectedItemPosition != selection) {
                spinner.setSelection(selection)
            }
        }
    }
    if (spinner.onItemSelectedListener == null) {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //NOP
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                inverseBindingListener?.onChange()
            }
        }
    }
}

@InverseBindingAdapter(attribute = "selectedItem", event = "selectedItemAttrChanged")
fun captureValue(spinner: Spinner): Any? = spinner.selectedItem

@BindingAdapter("itemTouchCallback")
fun setItemTouchCallback(recyclerView: RecyclerView, itemTouchCallback: ItemTouchHelper.Callback) {
    ItemTouchHelper(itemTouchCallback).attachToRecyclerView(recyclerView)
}
