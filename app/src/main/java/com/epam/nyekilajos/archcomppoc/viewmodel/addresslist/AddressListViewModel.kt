package com.epam.nyekilajos.archcomppoc.viewmodel.addresslist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.epam.nyekilajos.archcomppoc.network.CallHandler
import javax.inject.Inject

class AddressListViewModel @Inject constructor(val callHandler: CallHandler) : ViewModel() {

    val addressItems: MutableLiveData<List<AddressItem>> = MutableLiveData()

    init {
        addressItems.value = listOf(
                AddressItem("12.23.34.45", 1234),
                AddressItem("23.34.45.56", 2345),
                AddressItem("34.45.56.67", 3456),
                AddressItem("45.56.67.78", 4567),
                AddressItem("56.67.78.89", 5678))
    }

    override fun onCleared() {
        super.onCleared()
        addressItems.value = emptyList()
    }
}

data class AddressItem(val ipAddress: String, val port: Int)
