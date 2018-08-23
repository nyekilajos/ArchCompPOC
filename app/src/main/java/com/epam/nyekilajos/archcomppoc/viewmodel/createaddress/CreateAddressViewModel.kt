package com.epam.nyekilajos.archcomppoc.viewmodel.createaddress

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.epam.nyekilajos.archcomppoc.repository.AddressItem
import com.epam.nyekilajos.archcomppoc.repository.AddressRepository
import com.epam.nyekilajos.archcomppoc.repository.Protocol
import io.reactivex.Completable
import io.reactivex.Single
import java.net.InetAddress
import java.net.UnknownHostException
import javax.inject.Inject

class CreateAddressViewModel @Inject constructor(private val repository: AddressRepository) : ViewModel() {

    val name: MediatorLiveData<String> = MediatorLiveData()

    val protocol: MediatorLiveData<Protocol> = MediatorLiveData()

    val ipAddress: MediatorLiveData<String> = MediatorLiveData()

    val portText: MediatorLiveData<String> = MediatorLiveData()

    fun createAddress(): Completable {
        return validateValuesAndCreate()
                .flatMapCompletable {
                    repository.storeAddress(it)
                }
    }

    private fun validateValuesAndCreate(): Single<AddressItem> {
        return Single.fromCallable<AddressItem> {
            try {
                val validName = if (name.value?.isNotEmpty() == true) {
                    name.value!!
                } else {
                    throw InvalidAddressException("Name should not be empty", Errors.EMPTY_NAME)
                }
                val ip = validateIpAddress(ipAddress.value)
                val port = validatePort(portText.value)

                AddressItem(validName, protocol.value ?: Protocol.HTTP, ip, port)
            } catch (ex: UnknownHostException) {
                throw InvalidAddressException(ex.localizedMessage, Errors.INVALID_IP_ADDRESS)
            }
        }
    }

    enum class Errors { EMPTY_NAME, INVALID_IP_ADDRESS, INVALID_PORT, NO_ERROR }
}

private fun validateIpAddress(ip: String?): String {
    InetAddress.getByName(ip)
    return ip
            ?: throw InvalidAddressException("IP address should not be null", CreateAddressViewModel.Errors.INVALID_IP_ADDRESS)
}

private fun validatePort(port: String?): Int {
    return try {
        port?.toInt()
    } catch (ex: NumberFormatException) {
        null
    }
            ?: throw InvalidAddressException("Port should not be null", CreateAddressViewModel.Errors.INVALID_PORT)
}

class InvalidAddressException(message: String, val error: CreateAddressViewModel.Errors) : IllegalArgumentException(message)
