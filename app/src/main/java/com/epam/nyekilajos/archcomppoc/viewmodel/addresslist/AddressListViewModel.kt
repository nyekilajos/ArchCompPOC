package com.epam.nyekilajos.archcomppoc.viewmodel.addresslist

import com.epam.nyekilajos.archcomppoc.repository.AddressItem
import com.epam.nyekilajos.archcomppoc.repository.AddressRepository
import javax.inject.Inject

class AddressListViewModel @Inject constructor(repository: AddressRepository) : AbstractAddressViewModel(repository) {

    fun removeAddressItem(addressItem: AddressItem) = repository.removeAddress(addressItem)
}
