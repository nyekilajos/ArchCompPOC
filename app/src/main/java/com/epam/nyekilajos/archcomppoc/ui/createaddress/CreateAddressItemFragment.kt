package com.epam.nyekilajos.archcomppoc.ui.createaddress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.epam.nyekilajos.archcomppoc.databinding.CreateAddressItemFragmentBinding
import com.epam.nyekilajos.archcomppoc.inject.DaggerFragmentWithViewModel
import com.epam.nyekilajos.archcomppoc.inject.daggerViewModel
import com.epam.nyekilajos.archcomppoc.viewmodel.createaddress.CreateAddressViewModel

class CreateAddressItemFragment : DaggerFragmentWithViewModel() {

    private val viewModel: CreateAddressViewModel by daggerViewModel(this)

    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return CreateAddressItemFragmentBinding.inflate(inflater, container, false)
                .apply {
                    fragment = this@CreateAddressItemFragment
                    vm = viewModel
                }
                .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()
    }

    fun createAddress() {
        viewModel.createAddress()
        navController.navigateUp()
    }

    companion object {
        fun newInstance() = CreateAddressItemFragment()
    }
}
