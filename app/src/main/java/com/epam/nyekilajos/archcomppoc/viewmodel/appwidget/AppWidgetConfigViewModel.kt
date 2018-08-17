package com.epam.nyekilajos.archcomppoc.viewmodel.appwidget

import androidx.lifecycle.MediatorLiveData
import com.epam.nyekilajos.archcomppoc.repository.AddressItem
import com.epam.nyekilajos.archcomppoc.repository.AddressRepository
import com.epam.nyekilajos.archcomppoc.repository.SelectedAddressItem
import com.epam.nyekilajos.archcomppoc.repository.WidgetProperties
import com.epam.nyekilajos.archcomppoc.viewmodel.addresslist.AbstractAddressViewModel
import io.reactivex.Completable
import javax.inject.Inject

class AppWidgetConfigViewModel @Inject constructor(
        private val widgetProperties: WidgetProperties,
        repository: AddressRepository
) : AbstractAddressViewModel(repository) {

    val selectedAddressItem: MediatorLiveData<AddressItem> = MediatorLiveData()

    fun saveWidgetSettings(appWidgetId: Int): Completable {
        return selectedAddressItem.value
                ?.let { widgetProperties.insert(SelectedAddressItem(appWidgetId, it.name)) }
                ?: Completable.error(IllegalArgumentException("Invalid address to save."))
    }
}
