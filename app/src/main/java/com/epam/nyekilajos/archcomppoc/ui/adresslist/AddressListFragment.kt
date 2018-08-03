package com.epam.nyekilajos.archcomppoc.ui.adresslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.epam.nyekilajos.archcomppoc.R
import com.epam.nyekilajos.archcomppoc.inject.DaggerFragmentWithViewModel
import com.epam.nyekilajos.archcomppoc.inject.InjectViewModel

class AddressListFragment : DaggerFragmentWithViewModel() {

    @InjectViewModel
    lateinit var viewModel: AddressListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.adress_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.callHandler.callAddress("google.com", 8888)
    }

    companion object {
        fun newInstance() = AddressListFragment()
    }

}
