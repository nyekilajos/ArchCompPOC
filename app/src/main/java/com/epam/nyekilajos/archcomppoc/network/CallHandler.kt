package com.epam.nyekilajos.archcomppoc.network

interface CallHandler {

    fun callAddress(ipAddress: String, port: Int): String?
}
