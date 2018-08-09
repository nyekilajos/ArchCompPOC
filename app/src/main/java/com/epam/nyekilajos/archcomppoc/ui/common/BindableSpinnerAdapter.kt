package com.epam.nyekilajos.archcomppoc.ui.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.epam.nyekilajos.archcomppoc.R

class BindableSpinnerAdapter<T : TitleProvider>(
        private val context: Context,
        private val selectedLayoutId: Int,
        private val dropDownLayoutId: Int,
        private var items: List<T> = emptyList()) : BaseAdapter() {

    fun setItems(items: List<T>) {
        this.items = items.toList()
        notifyDataSetChanged()
    }

    fun getItemPosition(item: T): Int = items.indexOf(item)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return (convertView?.tag as? SelectedViewHolder
                ?: SelectedViewHolder(LayoutInflater.from(context).inflate(selectedLayoutId, parent, false)))
                .apply { textView.text = items[position].getTitle() }
                .also { convertView?.tag = it }
                .view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return (convertView?.tag as? DropDownViewHolder
                ?: DropDownViewHolder(LayoutInflater.from(context).inflate(dropDownLayoutId, parent, false)))
                .apply { textView.text = items[position].getTitle() }
                .also { convertView?.tag = it }
                .view
    }

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = getItem(position).hashCode().toLong()

    override fun getCount(): Int = items.size
}

private data class SelectedViewHolder(val view: View) {

    val textView: TextView = view.findViewById(R.id.selected_dropdown_text)
}

private data class DropDownViewHolder(val view: View) {

    val textView: TextView = view.findViewById(R.id.dropdown_text)
}
