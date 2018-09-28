package com.epam.nyekilajos.archcomppoc.viewmodel.addresslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.epam.nyekilajos.archcomppoc.repository.AddressItem
import com.epam.nyekilajos.archcomppoc.repository.AddressRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class AbstractAddressViewModel(val repository: AddressRepository) : ViewModel() {

    val addressItems: LiveData<List<AddressItem>> = repository
            .fetchAddressList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).toLiveData()
}
