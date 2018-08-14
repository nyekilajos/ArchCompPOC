package com.epam.nyekilajos.archcomppoc.service

import android.app.IntentService
import android.content.Intent
import android.widget.Toast
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URL

class CallHandlerService : IntentService(CallHandlerService::class.java.simpleName) {

    override fun onHandleIntent(intent: Intent?) {
        Single
                .fromCallable {
                    val connection: HttpURLConnection = URL(
                            DEFAULT_PROTOCOL + intent?.extras?.getString(EXTRA_IP_ADDRESS)
                    ).openConnection() as HttpURLConnection
                    try {
                        val input = BufferedInputStream(connection.inputStream)
                        input.reader().readLines().joinToString(" ")
                    } finally {
                        connection.disconnect()
                    }
                }
                .doOnSubscribe {
                    AndroidSchedulers.mainThread().scheduleDirect {
                        Toast.makeText(
                                applicationContext,
                                "Calling ${intent?.extras?.getString(EXTRA_IP_ADDRESS, "")} address on ${intent?.extras?.getInt(EXTRA_PORT, 0)} port...",
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
        const val EXTRA_IP_ADDRESS = "extraIpAddress"
        const val EXTRA_PORT = "extraPort"
    }
}

private const val DEFAULT_PROTOCOL = "http://"
private const val MAX_RESULT_LENGTH = 20
