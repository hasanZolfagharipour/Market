package com.zolfagharipour.market.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.zolfagharipour.market.network.CheckNetworkConnectivity

class SplashViewModel(application: Application) : AndroidViewModel(application) {


    private var isConnect: MutableLiveData<Boolean> = MutableLiveData(true)
    private var networkConnectivity = CheckNetworkConnectivity(application)
    private lateinit var lifecycleOwner: LifecycleOwner

    fun checkNetwork(lifecycleOwner: LifecycleOwner) {

        this.lifecycleOwner = lifecycleOwner
        networkConnectivity.observe(
            lifecycleOwner,
            Observer { isConnected -> isConnect.value = isConnected }
        )
    }

    fun connectedToInternet(): MutableLiveData<Boolean> = isConnect


}