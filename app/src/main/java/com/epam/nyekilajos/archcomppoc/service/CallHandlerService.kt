package com.epam.nyekilajos.archcomppoc.service

import android.content.Intent
import android.widget.Toast
import com.epam.nyekilajos.archcomppoc.repository.AddressItem
import dagger.android.DaggerIntentService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CallHandlerService : DaggerIntentService(CallHandlerService::class.java.simpleName) {

    @Inject
    lateinit var callHandler: CallHandler

    override fun onHandleIntent(intent: Intent?) {
        val addressItem: AddressItem = intent?.extras?.getParcelable(EXTRA_ADDRESS_ITEM)
                ?: throw IllegalArgumentException("Missing address item from the intent.")
        callHandler
                .call(addressItem)
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
