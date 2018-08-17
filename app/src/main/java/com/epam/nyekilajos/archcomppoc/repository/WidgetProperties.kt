package com.epam.nyekilajos.archcomppoc.repository

import io.reactivex.Completable
import io.reactivex.Maybe

interface WidgetProperties {

    fun getWidgetSettings(appWidgetId: Int): Maybe<AddressItem>

    fun insert(selectedAddressItem: SelectedAddressItem): Completable

    fun delete(appWidgetId: Int): Completable
}
