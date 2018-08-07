package com.epam.nyekilajos.archcomppoc.repository

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject

class InMemoryAddressListCache @Inject constructor() : AddressRepository {

    private val addressList: MutableList<AddressItem> = mutableListOf()

    private val subject: Subject<List<AddressItem>> = BehaviorSubject.createDefault(emptyList())

    override fun fetchAddressList(): Observable<List<AddressItem>> = subject.hide()

    override fun storeAddress(addressItem: AddressItem) {
        addressList += addressItem
        subject.onNext(addressList)
    }
}
