package com.epam.nyekilajos.archcomppoc.ui.adresslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.epam.nyekilajos.archcomppoc.databinding.AddressListFragmentBinding
import com.epam.nyekilajos.archcomppoc.inject.DaggerFragmentWithViewModel
import com.epam.nyekilajos.archcomppoc.inject.daggerViewModel
import com.epam.nyekilajos.archcomppoc.viewmodel.addresslist.AddressListViewModel

class AddressListFragment : DaggerFragmentWithViewModel() {

    private val viewModel: AddressListViewModel by daggerViewModel(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return AddressListFragmentBinding.inflate(inflater, container, false)
                .apply {
                    adapter = AddressAdapter(this@AddressListFragment.context!!, viewModel)
                    context = this@AddressListFragment.context
                    viewmodel = viewModel
                }
                .root
    }
}
