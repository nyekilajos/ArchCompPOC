package com.epam.nyekilajos.archcomppoc.ui.adresslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.epam.nyekilajos.archcomppoc.databinding.AddressListFragmentBinding
import com.epam.nyekilajos.archcomppoc.inject.DaggerFragmentWithViewModel
import com.epam.nyekilajos.archcomppoc.inject.daggerViewModel
import com.epam.nyekilajos.archcomppoc.viewmodel.addresslist.AddressListViewModel

class AddressListFragment : DaggerFragmentWithViewModel() {

    private val viewModel: AddressListViewModel by daggerViewModel(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel.error.observe(this, Observer {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
        return AddressListFragmentBinding.inflate(inflater, container, false)
                .apply {
                    setLifecycleOwner(this@AddressListFragment)
                    adapter = AddressAdapter(viewModel)
                    context = this@AddressListFragment.context
                    viewmodel = viewModel
                }
                .root
    }
}
