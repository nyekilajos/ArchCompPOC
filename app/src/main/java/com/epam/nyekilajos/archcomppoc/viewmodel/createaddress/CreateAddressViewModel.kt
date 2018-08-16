package com.epam.nyekilajos.archcomppoc.viewmodel.createaddress

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.epam.nyekilajos.archcomppoc.repository.AddressItem
import com.epam.nyekilajos.archcomppoc.repository.AddressRepository
import com.epam.nyekilajos.archcomppoc.repository.Protocol
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import java.net.InetAddress
import javax.inject.Inject

class CreateAddressViewModel @Inject constructor(private val repository: AddressRepository) : ViewModel() {

    val name: MutableLiveData<String> = MutableLiveData()

    val protocol: MutableLiveData<Protocol> = MutableLiveData()

    val ipAddress: MutableLiveData<String> = MutableLiveData()

    val portText: MutableLiveData<String> = MutableLiveData()

    val error: MutableLiveData<Errors> = MutableLiveData<Errors>().apply { postValue(Errors.NO_ERROR) }

    fun createAddress(): Completable {
        return validateValuesAndCreate()
                .flatMapCompletable {
                    repository.storeAddress(it)
                }
    }

    @Suppress("TooGenericExceptionCaught")
    private fun validateValuesAndCreate(): Single<AddressItem> {
        return Single.create<AddressItem> { emitter ->
            val ip = try {
                validateIpAddress(ipAddress.value)
            } catch (ex: Exception) {
                notifyError(Errors.INVALID_IP_ADDRESS)
                emitter.onError(ex)
                null
            }
            val port: Int? = ip?.let {
                try {
                    validatePort(portText.value)
                } catch (ex: Exception) {
                    notifyError(Errors.INVALID_PORT)
                    emitter.onError(ex)
                    null
                }
            }
            if (ip != null && port != null) {
                notifyError(Errors.NO_ERROR)
                emitter.onSuccess(AddressItem(name.value ?: "", protocol.value ?: Protocol.HTTP, ip, port))
            }
        }
    }

    private fun notifyError(errorType: Errors) {
        AndroidSchedulers.mainThread().scheduleDirect { error.value = errorType }
    }

    enum class Errors { INVALID_IP_ADDRESS, INVALID_PORT, NO_ERROR }
}

private fun validateIpAddress(ip: String?): String {
    InetAddress.getByName(ip)
    return ip ?: throw IllegalAccessException("IP address should not be null")
}

private fun validatePort(port: String?): Int {
    return port?.toInt() ?: throw IllegalAccessException("Port should not be null")
}
