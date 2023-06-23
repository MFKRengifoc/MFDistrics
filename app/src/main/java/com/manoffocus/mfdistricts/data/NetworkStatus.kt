package com.manoffocus.mfdistricts.data

sealed class NetworkStatus {
    object Unknown: NetworkStatus()
    object Connected: NetworkStatus()
    object Disconnected: NetworkStatus()
}