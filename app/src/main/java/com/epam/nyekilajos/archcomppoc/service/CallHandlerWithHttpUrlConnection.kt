package com.epam.nyekilajos.archcomppoc.service

import com.epam.nyekilajos.archcomppoc.repository.AddressItem
import io.reactivex.Single
import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

class CallHandlerWithHttpUrlConnection @Inject constructor() : CallHandler {

    override fun call(addressItem: AddressItem): Single<String> {
        return Single.fromCallable {
            val connection: HttpURLConnection = getUrl(addressItem).openConnection() as HttpURLConnection
            try {
                val input = BufferedInputStream(connection.inputStream)
                input.reader().readLines().joinToString(" ")
            } finally {
                connection.disconnect()
            }
        }
    }
}

private fun getUrl(addressItem: AddressItem): URL = URL("${addressItem.protocol.displayName}${addressItem.ipAddress}:${addressItem.port}")
