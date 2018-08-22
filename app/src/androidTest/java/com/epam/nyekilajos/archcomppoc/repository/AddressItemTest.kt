package com.epam.nyekilajos.archcomppoc.repository

import android.os.Parcel
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddressItemTest {

    @Test
    fun addressItemShouldBeParcelable() {
        val testItem = AddressItem("name1", Protocol.HTTPS, "ip", 8888)
        val parcel = Parcel.obtain()

        testItem.writeToParcel(parcel, testItem.describeContents())
        parcel.setDataPosition(0)

        val result = AddressItem(parcel)

        assertThat(result, equalTo(testItem))
    }
}
