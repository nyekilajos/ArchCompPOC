package com.epam.nyekilajos.archcomppoc.repository

import androidx.room.*

@Entity(
        foreignKeys = [ForeignKey(
                entity = AddressItem::class,
                parentColumns = ["name"],
                childColumns = ["address_id"],
                onDelete = ForeignKey.CASCADE
        )],
        indices = [Index("address_id")],
        tableName = "selectedAddressItems"
)
data class SelectedAddressItem(

        @PrimaryKey
        val id: Int,

        @ColumnInfo(name = "address_id")
        val addressId: String
)
