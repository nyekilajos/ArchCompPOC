package com.epam.nyekilajos.archcomppoc.viewmodel.createaddress

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.epam.nyekilajos.archcomppoc.RxImmediateSchedulerRule
import com.epam.nyekilajos.archcomppoc.repository.AddressItem
import com.epam.nyekilajos.archcomppoc.repository.AddressRepository
import com.epam.nyekilajos.archcomppoc.repository.Protocol
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CreateAddressViewModelTest {

    @Rule
    @JvmField
    var androidExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var repositoryMock: AddressRepository

    private lateinit var sut: CreateAddressViewModel

    @Before
    fun setUp() {
        sut = CreateAddressViewModel(repositoryMock)
    }

    @Test
    fun createAddressForEmptyNameShouldFail() {
        sut.createAddress()
                .test()
                .assertError {
                    it is InvalidAddressException && it.error == CreateAddressViewModel.Errors.EMPTY_NAME
                }
    }

    @Test
    fun createAddressForEmptyIpAddressShouldFail() {
        sut.name.value = TEST_NAME

        sut.createAddress()
                .test()
                .assertError {
                    it is InvalidAddressException && it.error == CreateAddressViewModel.Errors.INVALID_IP_ADDRESS
                }
    }

    @Test
    fun createAddressForInvalidIpAddressShouldFail() {
        sut.name.value = TEST_NAME
        sut.ipAddress.value = INVALID_IP

        sut.createAddress()
                .test()
                .assertError {
                    it is InvalidAddressException && it.error == CreateAddressViewModel.Errors.INVALID_IP_ADDRESS
                }
    }

    @Test
    fun createAddressForEmptyPortShouldFail() {
        sut.name.value = TEST_NAME
        sut.ipAddress.value = VALID_IP

        sut.createAddress()
                .test()
                .assertError {
                    it is InvalidAddressException && it.error == CreateAddressViewModel.Errors.INVALID_PORT
                }
    }

    @Test
    fun createAddressForInvalidPortShouldFail() {
        sut.name.value = TEST_NAME
        sut.ipAddress.value = VALID_IP
        sut.portText.value = INVALID_PORT_TEXT


        sut.createAddress()
                .test()
                .assertError {
                    it is InvalidAddressException && it.error == CreateAddressViewModel.Errors.INVALID_PORT
                }
    }

    @Test
    fun createAddressShouldCallRepository() {
        whenever(repositoryMock.storeAddress(eq(TEST_ADDRESS_ITEM_HTTPS))).thenReturn(Completable.complete())

        sut.name.value = TEST_NAME
        sut.protocol.value = Protocol.HTTPS
        sut.ipAddress.value = VALID_IP
        sut.portText.value = VALID_PORT_TEXT

        sut.createAddress()
                .test()
                .assertComplete()

        verify(repositoryMock).storeAddress(eq(TEST_ADDRESS_ITEM_HTTPS))
    }

    @Test
    fun protocolShouldBeHttpByDefault() {
        whenever(repositoryMock.storeAddress(eq(TEST_ADDRESS_ITEM_HTTP))).thenReturn(Completable.complete())

        sut.name.value = TEST_NAME
        sut.ipAddress.value = VALID_IP
        sut.portText.value = VALID_PORT_TEXT

        sut.createAddress()
                .test()
                .assertComplete()

        verify(repositoryMock).storeAddress(eq(TEST_ADDRESS_ITEM_HTTP))
    }
}

private const val TEST_NAME = "address name"
private const val INVALID_IP = "This is a truly invalid IP"
private const val VALID_IP = "127.0.0.1"
private const val INVALID_PORT_TEXT = "This is a truly invalid port"
private const val VALID_PORT_TEXT = "8080"

private val TEST_ADDRESS_ITEM_HTTP = AddressItem(TEST_NAME, Protocol.HTTP, VALID_IP, VALID_PORT_TEXT.toInt())
private val TEST_ADDRESS_ITEM_HTTPS = AddressItem(TEST_NAME, Protocol.HTTPS, VALID_IP, VALID_PORT_TEXT.toInt())
