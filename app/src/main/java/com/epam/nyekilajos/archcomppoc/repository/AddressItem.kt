package com.epam.nyekilajos.archcomppoc.repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.epam.nyekilajos.archcomppoc.ui.common.TitleProvider

@Entity(tableName = "addressItems")
data class AddressItem(

        @PrimaryKey
        @ColumnInfo(name = "name")
        val name: String,

        @ColumnInfo(name = "protocol")
        val protocol: Protocol,

        @ColumnInfo(name = "ip_address")
        val ipAddress: String,

        @ColumnInfo(name = "port")
        val port: Int) : TitleProvider {

    override fun getTitle(): String = "$name (${protocol.displayName}$ipAddress:$port)"
}

enum class Protocol(val displayName: String) {
    HTTP("http://"), HTTPS("https://")
}
