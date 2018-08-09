package com.epam.nyekilajos.archcomppoc.repository

import io.reactivex.Completable
import io.reactivex.Maybe
import javax.inject.Inject

class MockWidgetProperties @Inject constructor(private val addressRepository: AddressRepository) : WidgetProperties {

    private var appWidgetId: Int = 0

    override fun getWidgetSettings(appWidgetId: Int): Maybe<AddressItemWithAppWidgetId> {
        return addressRepository.fetchAddressList().firstElement().map { AddressItemWithAppWidgetId(appWidgetId, it.first()) }
    }

    override fun insert(selectedAddressItem: SelectedAddressItem): Completable {
        appWidgetId = selectedAddressItem.id
        return Completable.complete()
    }

    override fun delete(selectedAddressItem: SelectedAddressItem): Completable {
        return Completable.complete()
    }
}
