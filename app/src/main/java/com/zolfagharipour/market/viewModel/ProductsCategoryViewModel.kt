package com.zolfagharipour.market.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.gson.reflect.TypeToken
import com.zolfagharipour.market.data.room.entities.CategoryModel
import com.zolfagharipour.market.data.room.entities.ProductModel
import com.zolfagharipour.market.network.ApiRequestService
import com.zolfagharipour.market.network.CheckNetworkConnectivity
import com.zolfagharipour.market.network.NetworkParams
import com.zolfagharipour.market.network.RetrofitBuilder
import com.zolfagharipour.market.network.deserializer.ProductsDeserializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class ProductsCategoryViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var category: CategoryModel
    private var networkConnectivity = CheckNetworkConnectivity(application)
    private lateinit var lifecycleOwner: LifecycleOwner
    var isDataFetched: MutableLiveData<Boolean> = MutableLiveData(false)

    fun searchTitle(): String = "جستجو در ${category.name}"

    fun checkNetwork(lifecycleOwner: LifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner
        networkConnectivity.observe(
            lifecycleOwner,
            Observer { isConnected ->
                if (isConnected)
                    CoroutineScope(IO).launch { fetchItems() }
            }
        )
    }

    private suspend fun fetchItems() {

        val typeToken = object : TypeToken<ArrayList<ProductModel>>() {}.type
        val typeAdapter = ProductsDeserializer()

        val api = RetrofitBuilder.getInstance(typeToken, typeAdapter)
            .create(ApiRequestService::class.java)

        val categoryResponse =
            api.products(NetworkParams.queryOptionsProductOfCategory(category.id))
        if (categoryResponse.isSuccessful) {
            category.products = categoryResponse.body()!!
            isDataFetched.postValue(true)
        }
    }
}