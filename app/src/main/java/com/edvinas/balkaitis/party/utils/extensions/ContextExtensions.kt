package com.edvinas.balkaitis.party.utils.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

fun Context.hasNetwork(): Boolean {
    var isConnected: Boolean = false // Initial Value
    val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
    if (activeNetwork != null && activeNetwork.isConnected)
        isConnected = true
    return isConnected
}
