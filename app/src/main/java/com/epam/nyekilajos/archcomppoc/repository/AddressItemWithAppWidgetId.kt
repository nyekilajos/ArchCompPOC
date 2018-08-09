package com.epam.nyekilajos.archcomppoc.repository

import androidx.room.Embedded

data class AddressItemWithAppWidgetId(

        val appWidgetId: Int,

        @Embedded
        val addressItem: AddressItem
)
