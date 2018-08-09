package com.epam.nyekilajos.archcomppoc.network

import javax.inject.Inject

class CallHandlerImpl @Inject constructor() : CallHandler {
    override fun callAddress(ipAddress: String, port: Int): String? {
        return ""
    }
}
