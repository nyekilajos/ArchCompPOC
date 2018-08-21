package com.epam.nyekilajos.archcomppoc.service

import com.epam.nyekilajos.archcomppoc.repository.AddressItem
import io.reactivex.Single

interface CallHandler {

    fun call(addressItem: AddressItem): Single<String>
}
