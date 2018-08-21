package com.epam.nyekilajos.archcomppoc.repository

import android.os.Parcel
import android.os.Parcelable
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
        val port: Int) : TitleProvider, Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            Protocol.valueOf(parcel.readString()!!),
            parcel.readString()!!,
            parcel.readInt())

    override fun getTitle(): String = name

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(protocol.name)
        parcel.writeString(ipAddress)
        parcel.writeInt(port)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AddressItem> {

        override fun createFromParcel(parcel: Parcel): AddressItem {
            return AddressItem(parcel)
        }

        override fun newArray(size: Int): Array<AddressItem?> {
            return arrayOfNulls(size)
        }
    }
}

enum class Protocol(val displayName: String) : TitleProvider {
    HTTP("http://"), HTTPS("https://");

    override fun getTitle(): String = name
}
