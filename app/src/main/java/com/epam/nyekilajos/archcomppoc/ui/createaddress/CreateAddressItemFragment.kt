package com.epam.nyekilajos.archcomppoc.ui.createaddress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.epam.nyekilajos.archcomppoc.R
import com.epam.nyekilajos.archcomppoc.databinding.CreateAddressItemFragmentBinding
import com.epam.nyekilajos.archcomppoc.inject.DaggerFragmentWithViewModel
import com.epam.nyekilajos.archcomppoc.inject.daggerViewModel
import com.epam.nyekilajos.archcomppoc.viewmodel.createaddress.CreateAddressViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

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
        viewModel.error.observe(viewLifecycleOwner, Observer<CreateAddressViewModel.Errors> { error ->
            when (error) {
                CreateAddressViewModel.Errors.INVALID_IP_ADDRESS -> getString(R.string.invalid_ip_error)
                CreateAddressViewModel.Errors.INVALID_PORT -> getString(R.string.invalid_port_error)
                else -> null
            }?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }

        })
    }

    fun createAddress() {
        viewModel.createAddress()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onComplete = {
                            navController.navigateUp()
                        },
                        onError = {
                            //NOP
                        })
    }
}
