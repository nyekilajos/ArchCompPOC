package com.epam.nyekilajos.archcomppoc.ui.createaddress

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.epam.nyekilajos.archcomppoc.R
import com.epam.nyekilajos.archcomppoc.databinding.CreateAddressItemFragmentBinding
import com.epam.nyekilajos.archcomppoc.inject.DaggerFragmentWithViewModel
import com.epam.nyekilajos.archcomppoc.inject.daggerViewModel
import com.epam.nyekilajos.archcomppoc.repository.Protocol
import com.epam.nyekilajos.archcomppoc.ui.common.BindableSpinnerAdapter
import com.epam.nyekilajos.archcomppoc.viewmodel.createaddress.CreateAddressViewModel
import com.epam.nyekilajos.archcomppoc.viewmodel.createaddress.InvalidAddressException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class CreateAddressItemFragment : DaggerFragmentWithViewModel() {

    private val viewModel: CreateAddressViewModel by daggerViewModel(this)

    private lateinit var navController: NavController

    private lateinit var addressListAdapter: BindableSpinnerAdapter<Protocol>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        addressListAdapter = BindableSpinnerAdapter(
                context!!,
                R.layout.selected_protocol_picker_item,
                R.layout.dropdown_protocol_picker_item,
                Protocol.values().toList()
        )
        return CreateAddressItemFragmentBinding.inflate(inflater, container, false)
                .apply {
                    fragment = this@CreateAddressItemFragment
                    adapter = addressListAdapter
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onComplete = {
                            navController.navigateUp()
                        },
                        onError = { throwable ->
                            val message: String = (throwable as? InvalidAddressException?)
                                    ?.let {
                                        when (it.error) {
                                            CreateAddressViewModel.Errors.INVALID_IP_ADDRESS -> getString(R.string.invalid_ip_error)
                                            CreateAddressViewModel.Errors.INVALID_PORT -> getString(R.string.invalid_port_error)
                                            CreateAddressViewModel.Errors.EMPTY_NAME -> getString(R.string.empty_name_error)
                                            else -> null
                                        }
                                    } ?: (throwable as? SQLiteConstraintException?)
                                    ?.let {
                                        getString(R.string.name_already_used_error)
                                    } ?: throwable.localizedMessage

                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        })
    }
}
