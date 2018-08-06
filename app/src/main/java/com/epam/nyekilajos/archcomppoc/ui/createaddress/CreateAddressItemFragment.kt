package com.epam.nyekilajos.archcomppoc.ui.createaddress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.epam.nyekilajos.archcomppoc.databinding.CreateAddressItemFragmentBinding
import com.epam.nyekilajos.archcomppoc.inject.DaggerFragmentWithViewModel

class CreateAddressItemFragment : DaggerFragmentWithViewModel() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return CreateAddressItemFragmentBinding.inflate(inflater, container, false)
                .root
    }

    companion object {
        fun newInstance() = CreateAddressItemFragment()
    }
}