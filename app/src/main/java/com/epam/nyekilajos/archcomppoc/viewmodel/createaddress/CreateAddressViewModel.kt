package com.epam.nyekilajos.archcomppoc.viewmodel.createaddress

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.epam.nyekilajos.archcomppoc.repository.AddressItem
import com.epam.nyekilajos.archcomppoc.repository.AddressRepository
import javax.inject.Inject

class CreateAddressViewModel @Inject constructor(private val repository: AddressRepository) : ViewModel() {

    val ipAddress: MutableLiveData<String> = MutableLiveData()

    val portText: MutableLiveData<String> = MutableLiveData()

    val error: MutableLiveData<Errors> = MutableLiveData<Errors>().apply { postValue(Errors.NO_ERROR) }

    fun createAddress() {
        ipAddress.value?.let { ip ->
            portText.value?.let {
                repository.storeAddress(AddressItem(ip, it.toInt()))
            } ?: notifyError(Errors.INVALID_PORT) ?: 0
        } ?: notifyError<String>(Errors.INVALID_IP_ADDRESS)
    }

    private fun <T> notifyError(errorType: Errors): T? {
        error.value = errorType
        return null
    }

    enum class Errors { INVALID_IP_ADDRESS, INVALID_PORT, NO_ERROR }
}
