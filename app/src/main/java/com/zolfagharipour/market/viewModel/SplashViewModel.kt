package com.zolfagharipour.market.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.google.gson.reflect.TypeToken
import com.zolfagharipour.market.data.room.entities.Product
import com.zolfagharipour.market.data.room.entities.ProductRepository
import com.zolfagharipour.market.network.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashViewModel(application: Application) : AndroidViewModel(application) {


    var isConnect: MutableLiveData<Boolean> = MutableLiveData(true)
    var isDataFetched: MutableLiveData<Boolean> = MutableLiveData(false)

    private var networkConnectivity = CheckNetworkConnectivity(application)
    private lateinit var lifecycleOwner: LifecycleOwner

    fun checkNetwork(lifecycleOwner: LifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner
        networkConnectivity.observe(
            lifecycleOwner,
            Observer { isConnected ->
                isConnect.value = isConnected
                if (isConnected)
                CoroutineScope(IO).launch { fetchInitialProducts() }
                }
        )
    }

    private suspend fun fetchInitialProducts() {

        val type = object : TypeToken<ArrayList<Product>>() {}.type
        val typeAdapter = ProductDeserializer()

        val api =
            RetrofitBuilder.getInstance(type, typeAdapter).create(ApiRequestService::class.java)
        val response = api.getLastProducts(NetworkParams.QUERY_OPTIONS_PRODUCTS)
        ProductRepository.lastProductList.addAll(response.body()!!)
        withContext(Main){
            isDataFetched.value = true
        }

    }


}