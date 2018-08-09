package com.epam.nyekilajos.archcomppoc.repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.epam.nyekilajos.archcomppoc.ui.common.TitleProvider

@Entity(primaryKeys = ["ip_address", "port"], tableName = "addressItems")
data class AddressItem(

        @ColumnInfo(name = "ip_address")
        val ipAddress: String,

        @ColumnInfo(name = "port")
        val port: Int) : TitleProvider {

    override fun getTitle(): String = "$ipAddress:$port"
}
