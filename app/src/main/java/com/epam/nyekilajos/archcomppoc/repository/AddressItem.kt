package com.epam.nyekilajos.archcomppoc.repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.epam.nyekilajos.archcomppoc.ui.common.TitleProvider

@Entity(primaryKeys = ["protocol", "ip_address", "port"], tableName = "addressItems")
data class AddressItem(

        @ColumnInfo(name = "protocol")
        val protocol: Protocol,

        @ColumnInfo(name = "ip_address")
        val ipAddress: String,

        @ColumnInfo(name = "port")
        val port: Int) : TitleProvider {

    override fun getTitle(): String = "$protocol$ipAddress:$port"
}

enum class Protocol(val displayName: String) {
    HTTP("http://"), HTTPS("https://")
}
