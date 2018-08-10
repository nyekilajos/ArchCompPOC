package com.epam.nyekilajos.archcomppoc.viewmodel.addresslist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.epam.nyekilajos.archcomppoc.repository.AddressItem
import com.epam.nyekilajos.archcomppoc.repository.AddressRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

abstract class AbstractAddressViewModel(val repository: AddressRepository) : ViewModel() {

    val addressItems: MutableLiveData<List<AddressItem>> = MutableLiveData()

    private var disposable: Disposable? = null

    init {
        disposable = repository
                .fetchAddressList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { addressItems.value = it },
                        onError = { Log.e(AddressListViewModel::class.java.simpleName, it.localizedMessage) }
                )
    }

    override fun onCleared() {
        super.onCleared()
        addressItems.value = emptyList()
        disposable?.dispose()
        disposable = null
    }
}
