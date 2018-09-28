package com.epam.nyekilajos.archcomppoc.viewmodel.addresslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.epam.nyekilajos.archcomppoc.RxImmediateSchedulerRule
import com.epam.nyekilajos.archcomppoc.repository.AddressItem
import com.epam.nyekilajos.archcomppoc.repository.AddressRepository
import com.epam.nyekilajos.archcomppoc.repository.Protocol
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import io.reactivex.Flowable
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AddressListViewModelTest {

    @Rule
    @JvmField
    var androidExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var repositoryMock: AddressRepository

    @Test
    fun removeAddressItemShouldCallRepository() {
        whenever(repositoryMock.fetchAddressList()).thenReturn(Flowable.empty())
        whenever(repositoryMock.removeAddress(eq(TEST_ADDRESS_ITEM1))).thenReturn(Completable.complete())

        AddressListViewModel(repositoryMock).removeAddressItem(TEST_ADDRESS_ITEM1)

        verify(repositoryMock).removeAddress(eq(TEST_ADDRESS_ITEM1))
    }
}

private val TEST_ADDRESS_ITEM1 = AddressItem("Test item 01", Protocol.HTTP, "www.google.com", 80)
