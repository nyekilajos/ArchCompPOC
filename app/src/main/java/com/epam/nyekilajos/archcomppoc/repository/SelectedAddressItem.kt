package com.epam.nyekilajos.archcomppoc.repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = AddressItem::class, parentColumns = ["id"], childColumns = ["address_id"])], tableName = "selectedAddressItems")
data class SelectedAddressItem(

        @PrimaryKey
        val id: Int,

        @ColumnInfo(name = "address_id")
        val addressId: String
)
