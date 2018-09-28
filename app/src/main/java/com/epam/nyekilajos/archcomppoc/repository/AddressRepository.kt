package com.epam.nyekilajos.archcomppoc.repository

import io.reactivex.Completable
import io.reactivex.Flowable

interface AddressRepository {

    fun fetchAddressList(): Flowable<List<AddressItem>>

    fun storeAddress(addressItem: AddressItem): Completable

    fun removeAddress(addressItem: AddressItem): Completable
}
