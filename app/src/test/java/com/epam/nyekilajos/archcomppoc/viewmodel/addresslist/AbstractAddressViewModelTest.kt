package com.epam.nyekilajos.archcomppoc.viewmodel.addresslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.epam.nyekilajos.archcomppoc.RxImmediateSchedulerRule
import com.epam.nyekilajos.archcomppoc.repository.AddressItem
import com.epam.nyekilajos.archcomppoc.repository.AddressRepository
import com.epam.nyekilajos.archcomppoc.repository.Protocol
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.subjects.PublishSubject
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AbstractAddressViewModelTest {

    @Rule
    @JvmField
    var androidExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var repositoryMock: AddressRepository

    @Mock
    private lateinit var addressItemsObserverMock: Observer<List<AddressItem>>

    @Test
    fun listenersShouldBeNotifiedOnInit() {
        val testSubject = PublishSubject.create<List<AddressItem>>()
        whenever(repositoryMock.fetchAddressList()).thenReturn(testSubject)

        object : AbstractAddressViewModel(repositoryMock) {}.addressItems.observeForever(addressItemsObserverMock)

        testSubject.onNext(listOf(TEST_ADDRESS_ITEM1, TEST_ADDRESS_ITEM2))

        verify(addressItemsObserverMock).onChanged(eq(listOf(TEST_ADDRESS_ITEM1, TEST_ADDRESS_ITEM2)))
    }
}

private val TEST_ADDRESS_ITEM1 = AddressItem("Test item 01", Protocol.HTTP, "www.google.com", 80)
private val TEST_ADDRESS_ITEM2 = AddressItem("Test item 02", Protocol.HTTPS, "www.google.com", 8080)
