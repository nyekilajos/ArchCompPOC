package com.epam.nyekilajos.archcomppoc.repository

import io.reactivex.Completable
import io.reactivex.Observable

interface AddressRepository {

    fun fetchAddressList(): Observable<List<AddressItem>>

    fun storeAddress(addressItem: AddressItem): Completable

    fun removeAddress(addressItem: AddressItem): Completable
}
