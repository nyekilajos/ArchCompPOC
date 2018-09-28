package com.epam.nyekilajos.archcomppoc.ui.adresslist

import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.epam.nyekilajos.archcomppoc.databinding.AddressItemBinding
import com.epam.nyekilajos.archcomppoc.repository.AddressItem
import com.epam.nyekilajos.archcomppoc.viewmodel.addresslist.AddressListViewModel

class AddressAdapter(
        private val viewModel: AddressListViewModel
) : ListAdapter<AddressItem, AddressViewHolder>(addressItemsDiffCallback), RecyclerItemTouchHelperListener {

    val itemTouchCallback = SwipeToDismissTouchHelper(this)

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        viewModel.removeAddressItem(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        return AddressViewHolder(AddressItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private val addressItemsDiffCallback = object : DiffUtil.ItemCallback<AddressItem>() {

    override fun areItemsTheSame(oldItem: AddressItem, newItem: AddressItem): Boolean = oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: AddressItem, newItem: AddressItem): Boolean = oldItem == newItem
}

class AddressViewHolder(val addressItemBinding: AddressItemBinding) : RecyclerView.ViewHolder(addressItemBinding.root) {

    fun bind(addressItem: AddressItem) {
        addressItemBinding.foreground.item = addressItem
        addressItemBinding.executePendingBindings()
    }
}

class SwipeToDismissTouchHelper(private val listener: RecyclerItemTouchHelperListener) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.onSwiped(viewHolder, direction, viewHolder.adapterPosition)
    }

    override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean) {
        val foregroundView = (viewHolder as AddressViewHolder).addressItemBinding.foreground.root

        ItemTouchHelper.Callback.getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive)
    }

    override fun onChildDrawOver(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder?,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean) {
        val foregroundView = (viewHolder as AddressViewHolder).addressItemBinding.foreground.root
        ItemTouchHelper.Callback.getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (viewHolder != null) {
            val foregroundView = (viewHolder as AddressViewHolder).addressItemBinding.foreground.root

            ItemTouchHelper.Callback.getDefaultUIUtil().onSelected(foregroundView)
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        val foregroundView = (viewHolder as AddressViewHolder).addressItemBinding.foreground.root
        ItemTouchHelper.Callback.getDefaultUIUtil().clearView(foregroundView)
    }
}

interface RecyclerItemTouchHelperListener {
    fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int)
}
