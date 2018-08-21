package com.epam.nyekilajos.archcomppoc.service

import android.app.IntentService
import android.content.Intent
import android.widget.Toast
import com.epam.nyekilajos.archcomppoc.repository.AddressItem
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URL

class CallHandlerService : IntentService(CallHandlerService::class.java.simpleName) {

    override fun onHandleIntent(intent: Intent?) {
        val addressItem: AddressItem = intent?.extras?.getParcelable(EXTRA_ADDRESS_ITEM)
                ?: throw IllegalArgumentException("Missing address item from the intent.")
        Single
                .fromCallable {
                    callUrl(addressItem)
                }
                .doOnSubscribe {
                    AndroidSchedulers.mainThread().scheduleDirect {
                        Toast.makeText(
                                applicationContext,
                                "Calling ${addressItem.name} address...",
                                Toast.LENGTH_SHORT)
                                .show()
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            Toast.makeText(applicationContext, "The result is: ${it.slice(0..MAX_RESULT_LENGTH)}...", Toast.LENGTH_SHORT).show()
                        },
                        onError = {
                            Toast.makeText(applicationContext, "An error happened: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                        })
    }

    companion object {
        const val EXTRA_ADDRESS_ITEM = "extraAddressItem"
    }
}

private const val MAX_RESULT_LENGTH = 20

private fun callUrl(addressItem: AddressItem): String {
    val connection: HttpURLConnection = getUrl(addressItem).openConnection() as HttpURLConnection
    return try {
        val input = BufferedInputStream(connection.inputStream)
        input.reader().readLines().joinToString(" ")
    } finally {
        connection.disconnect()
    }
}

private fun getUrl(addressItem: AddressItem): URL = URL("${addressItem.protocol.displayName}${addressItem.ipAddress}:${addressItem.port}")
