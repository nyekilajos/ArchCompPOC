package com.epam.nyekilajos.archcomppoc.service

import android.app.IntentService
import android.content.Intent
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers

class CallHandlerService : IntentService(CallHandlerService::class.java.simpleName) {

    override fun onHandleIntent(intent: Intent?) {
        AndroidSchedulers.mainThread().scheduleDirect {
            Toast.makeText(
                    applicationContext,
                    "Calling ${intent?.extras?.getString(EXTRA_IP_ADDRESS, "")} address on ${intent?.extras?.getInt(EXTRA_PORT, 0)} port...",
                    Toast.LENGTH_SHORT)
                    .show()
        }
    }

    companion object {
        const val EXTRA_IP_ADDRESS = "extraIpAddress"
        const val EXTRA_PORT = "extraPort"
    }
}
