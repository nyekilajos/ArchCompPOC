package com.epam.nyekilajos.archcomppoc.viewmodel.appwidget

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.epam.nyekilajos.archcomppoc.RxImmediateSchedulerRule
import com.epam.nyekilajos.archcomppoc.repository.*
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AppWidgetConfigViewModelTest {

    @Rule
    @JvmField
    var androidExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var widgetPropertiesMock: WidgetProperties

    @Mock
    private lateinit var addressRepositoryMock: AddressRepository

    private lateinit var sut: AppWidgetConfigViewModel

    @Before
    fun setUp() {
        whenever(addressRepositoryMock.fetchAddressList()).thenReturn(Observable.just(listOf(TEST_ADDRESS_ITEM1, TEST_ADDRESS_ITEM2)))
        sut = AppWidgetConfigViewModel(widgetPropertiesMock, addressRepositoryMock)
    }

    @Test
    fun saveWidgetSettingsShouldFailIfSelectedAddressIsNotSet() {
        sut.saveWidgetSettings(TEST_APP_WIDGET_ID1)
                .test()
                .assertError(IllegalArgumentException::class.java)
    }

    @Test
    fun saveWidgetSettingsShouldCallWidgetProperties() {
        whenever(widgetPropertiesMock.insert(eq(TEST_SELECTED_ADDRESS_ITEM1))).thenReturn(Completable.complete())
        sut.selectedAddressItem.value = TEST_ADDRESS_ITEM1

        sut.saveWidgetSettings(TEST_APP_WIDGET_ID1)
                .test()
                .assertComplete()
    }
}

private const val TEST_APP_WIDGET_ID1 = 100

private val TEST_ADDRESS_ITEM1 = AddressItem("Test item 01", Protocol.HTTP, "www.google.com", 80)
private val TEST_ADDRESS_ITEM2 = AddressItem("Test item 02", Protocol.HTTPS, "www.google.com", 8080)

private val TEST_SELECTED_ADDRESS_ITEM1 = SelectedAddressItem(TEST_APP_WIDGET_ID1, TEST_ADDRESS_ITEM1.name)
