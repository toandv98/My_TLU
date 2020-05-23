package com.toandv.mytlu.utils

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import com.toandv.mytlu.utils.NetWorkState.*

enum class NetWorkState(val isConnected: Boolean) {
    Available(true),
    Unavailable(false),
    Lost(false)
}

object NetworkStatusLive : LiveData<NetWorkState>() {

    private lateinit var networkRequest: NetworkRequest

    private lateinit var cm: ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network) {
            super.onLost(network)
            postValue(Lost)
        }

        override fun onUnavailable() {
            super.onUnavailable()
            postValue(Unavailable)
        }

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            postValue(Available)
        }
    }

    override fun onActive() {
        super.onActive()
        cm.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun onInactive() {
        super.onInactive()
        cm.unregisterNetworkCallback(networkCallback)
    }

    fun init(application: Application) {
        networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()
        cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
}