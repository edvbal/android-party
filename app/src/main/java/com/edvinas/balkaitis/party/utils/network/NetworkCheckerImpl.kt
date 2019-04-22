package com.edvinas.balkaitis.party.utils.network

import android.content.Context
import com.edvinas.balkaitis.party.utils.extensions.hasNetwork

class NetworkCheckerImpl(private val context: Context) : NetworkChecker {
    override fun hasNetwork() = context.hasNetwork()
}
