package com.epam.nyekilajos.archcomppoc.repository

import io.reactivex.Completable
import io.reactivex.Maybe

interface WidgetProperties {

    fun getWidgetSettings(appWidgetId: Int): Maybe<AddressItemWithAppWidgetId>

    fun insert(selectedAddressItem: SelectedAddressItem): Completable

    fun delete(selectedAddressItem: SelectedAddressItem): Completable
}
