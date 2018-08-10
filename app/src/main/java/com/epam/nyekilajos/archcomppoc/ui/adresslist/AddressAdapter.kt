package com.epam.nyekilajos.archcomppoc.ui.adresslist

import android.content.Context
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.epam.nyekilajos.archcomppoc.databinding.AddressItemBinding
import com.epam.nyekilajos.archcomppoc.repository.AddressItem
import com.epam.nyekilajos.archcomppoc.ui.common.BindableAdapter
import com.epam.nyekilajos.archcomppoc.viewmodel.addresslist.AddressListViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class AddressAdapter(
        private val context: Context,
        private val viewModel: AddressListViewModel
) : RecyclerView.Adapter<AddressViewHolder>(), BindableAdapter<List<AddressItem>>, RecyclerItemTouchHelperListener {

    val itemTouchCallback = SwipeToDismissTouchHelper(this)

    private val addressItems: MutableList<AddressItem> = mutableListOf()

    override fun setData(items: List<AddressItem>) {
        addressItems += items
        notifyDataSetChanged()
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        val item = addressItems[position]
        addressItems -= item
        notifyItemRemoved(position)
        viewModel.removeAddressItem(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onComplete = {
                            addressItems
                        },
                        onError = {
                            Toast.makeText(context, "Failed to delete address: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                            addressItems.add(position, item)
                            notifyItemInserted(position)
                        }
                )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        return AddressViewHolder(AddressItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = addressItems.size

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.bind(addressItems[position])
    }
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
