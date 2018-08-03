package com.epam.nyekilajos.archcomppoc.network

import java.net.InetAddress

interface CallHandler {

    fun callAddress(ipAddress: String, port: Int): String?
}