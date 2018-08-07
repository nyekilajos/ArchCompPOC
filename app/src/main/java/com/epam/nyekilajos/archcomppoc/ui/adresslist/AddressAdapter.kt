package com.epam.nyekilajos.archcomppoc.ui.adresslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.epam.nyekilajos.archcomppoc.databinding.AddressItemBinding
import com.epam.nyekilajos.archcomppoc.repository.AddressItem
import com.epam.nyekilajos.archcomppoc.ui.common.BindableAdapter

class AddressAdapter : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>(), BindableAdapter<List<AddressItem>> {

    private val addressItems: MutableList<AddressItem> = mutableListOf()

    override fun setData(items: List<AddressItem>) {
        addressItems += items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        return AddressViewHolder(AddressItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = addressItems.size

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.bind(addressItems[position])
    }


    class AddressViewHolder(private val addressItemBinding: AddressItemBinding) : RecyclerView.ViewHolder(addressItemBinding.root) {

        fun bind(addressItem: AddressItem) {
            addressItemBinding.item = addressItem
            addressItemBinding.executePendingBindings()
        }
    }
}