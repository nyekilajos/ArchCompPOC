package com.epam.nyekilajos.archcomppoc.viewmodel.addresslist

import androidx.lifecycle.MutableLiveData
import com.epam.nyekilajos.archcomppoc.repository.AddressItem
import com.epam.nyekilajos.archcomppoc.repository.AddressRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AddressListViewModel @Inject constructor(repository: AddressRepository) : AbstractAddressViewModel(repository) {

    val error: MutableLiveData<String> = MutableLiveData()

    private val disposables: CompositeDisposable = CompositeDisposable()

    fun removeAddressItem(addressItem: AddressItem) {
        disposables += repository.removeAddress(addressItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onError = {
                    error.value = "Failed to delete address: ${it.localizedMessage}"
                })
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
