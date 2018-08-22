package com.epam.nyekilajos.archcomppoc.repository

import com.epam.nyekilajos.archcomppoc.RxImmediateSchedulerRule
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AddressDataBaseTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var addressDaoMock: AddressDao

    @Mock
    private lateinit var widgetPropertiesDaoMock: WidgetPropertiesDao

    @Spy
    private lateinit var sut: AddressDataBase

    @Before
    fun setUp() {
        whenever(sut.addressDao()).thenReturn(addressDaoMock)
        whenever(sut.widgetPropertiesDao()).thenReturn(widgetPropertiesDaoMock)
    }

    @Test
    fun fetchAddressListShouldReturnAddresses() {
        whenever(addressDaoMock.getAllAddressItems()).thenReturn(listOf(TEST_ADDRESS_ITEM1, TEST_ADDRESS_ITEM2))

        sut.fetchAddressList()
                .test()
                .assertValue(listOf(TEST_ADDRESS_ITEM1, TEST_ADDRESS_ITEM2))
    }

    @Test
    fun fetchAddressListLoadsTheAddressesOnlyOnce() {
        whenever(addressDaoMock.getAllAddressItems()).thenReturn(listOf(TEST_ADDRESS_ITEM1, TEST_ADDRESS_ITEM2))

        sut.fetchAddressList()

        verify(addressDaoMock).getAllAddressItems()

        sut.fetchAddressList()
                .test()
                .assertValue(listOf(TEST_ADDRESS_ITEM1, TEST_ADDRESS_ITEM2))

        verifyNoMoreInteractions(addressDaoMock)
    }

    @Test
    fun storeAddressShouldNotifyListenersForTheAddressList() {
        whenever(addressDaoMock.getAllAddressItems()).thenReturn(listOf(TEST_ADDRESS_ITEM1))
        val testObserver = sut.fetchAddressList().test()

        sut.storeAddress(TEST_ADDRESS_ITEM2).test().assertComplete()

        testObserver.assertValues(listOf(TEST_ADDRESS_ITEM1), listOf(TEST_ADDRESS_ITEM1, TEST_ADDRESS_ITEM2))
    }

    @Test
    fun storeAddressShouldInsertIntoDatabase() {
        sut.storeAddress(TEST_ADDRESS_ITEM2).test().assertComplete()

        verify(addressDaoMock).insert(eq(TEST_ADDRESS_ITEM2))
    }

    @Test
    fun removeAddressShouldNotifyListenersForTheAddressList() {
        whenever(addressDaoMock.getAllAddressItems()).thenReturn(listOf(TEST_ADDRESS_ITEM1, TEST_ADDRESS_ITEM2))
        val testObserver = sut.fetchAddressList().test()

        sut.removeAddress(TEST_ADDRESS_ITEM1).test().assertComplete()

        testObserver.assertValues(listOf(TEST_ADDRESS_ITEM1, TEST_ADDRESS_ITEM2), listOf(TEST_ADDRESS_ITEM2))
    }

    @Test
    fun removeAddressShouldRemoveFromDatabase() {
        sut.removeAddress(TEST_ADDRESS_ITEM1).test().assertComplete()

        verify(addressDaoMock).delete(eq(TEST_ADDRESS_ITEM1))
    }

    @Test
    fun getWidgetSettingsShouldReturnAddressItemForExistingId() {
        whenever(addressDaoMock.getAddressByName(TEST_ADDRESS_ITEM1.name)).thenReturn(TEST_ADDRESS_ITEM1)
        whenever(widgetPropertiesDaoMock.getAddressNameForId(eq(TEST_APP_WIDGET_ID1))).thenReturn(TEST_ADDRESS_ITEM1.name)

        sut.getWidgetSettings(TEST_APP_WIDGET_ID1)
                .test()
                .assertValue(TEST_ADDRESS_ITEM1)
    }

    @Test
    fun getWidgetSettingsShouldFailOnUnknownId() {
        sut.getWidgetSettings(TEST_APP_WIDGET_ID1)
                .test()
                .assertError(NoSuchElementException::class.java)
    }

    @Test
    fun insertShouldInsertIntoDatabase() {
        sut.insert(TEST_SELECTED_ADDRESS_ITEM1).test().assertComplete()

        verify(widgetPropertiesDaoMock).insert(eq(TEST_SELECTED_ADDRESS_ITEM1))
    }

    @Test
    fun deleteShouldDeleteFromDatabaseForExistingId() {
        whenever(widgetPropertiesDaoMock.getSelectedAddressItemForId(eq(TEST_APP_WIDGET_ID1))).thenReturn(TEST_SELECTED_ADDRESS_ITEM1)

        sut.delete(TEST_APP_WIDGET_ID1).test().assertComplete()

        verify(widgetPropertiesDaoMock).delete(eq(TEST_SELECTED_ADDRESS_ITEM1))
    }

    @Test
    fun deleteShouldFailOnUnknownId() {
        sut.delete(TEST_APP_WIDGET_ID1)
                .test()
                .assertError(NoSuchElementException::class.java)
    }
}

private const val TEST_APP_WIDGET_ID1 = 100

private val TEST_ADDRESS_ITEM1 = AddressItem("Test item 01", Protocol.HTTP, "www.google.com", 80)
private val TEST_ADDRESS_ITEM2 = AddressItem("Test item 02", Protocol.HTTPS, "www.google.com", 8080)
private val TEST_SELECTED_ADDRESS_ITEM1 = SelectedAddressItem(TEST_APP_WIDGET_ID1, TEST_ADDRESS_ITEM1.name)
