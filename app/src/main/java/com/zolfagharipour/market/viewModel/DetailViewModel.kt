package com.zolfagharipour.market.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.google.gson.reflect.TypeToken
import com.zolfagharipour.market.data.room.entities.ProductModel
import com.zolfagharipour.market.network.ApiRequestService
import com.zolfagharipour.market.network.CheckNetworkConnectivity
import com.zolfagharipour.market.network.NetworkParams
import com.zolfagharipour.market.network.RetrofitBuilder
import com.zolfagharipour.market.network.deserializer.ProductDetailDeserializer
import com.zolfagharipour.market.network.deserializer.ProductSummaryItemRowDeserializer
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    var isDataFetched: MutableLiveData<Boolean> = MutableLiveData(false)
    private var networkConnectivity = CheckNetworkConnectivity(application)
    private lateinit var lifecycleOwner: LifecycleOwner
    var productModel: MutableLiveData<ProductModel> = MutableLiveData()

    fun checkNetwork(lifecycleOwner: LifecycleOwner, productModel: ProductModel) {
        this.lifecycleOwner = lifecycleOwner
        networkConnectivity.observe(
            lifecycleOwner,
            Observer { isConnected ->
                if (isConnected)
                    CoroutineScope(IO).launch { fetchProductDetail(productModel) }
            }
        )
    }

    private suspend fun fetchProductDetail(productModel: ProductModel) {
        val typeToken = object : TypeToken<ProductModel>() {}.type
        val typeAdapter = ProductDetailDeserializer()

        val api = RetrofitBuilder.getInstance(typeToken, typeAdapter)
            .create(ApiRequestService::class.java)


        val productResponse = api.product(productModel.id, NetworkParams.QUERY_OPTIONS_BASIC)

        if (productResponse.isSuccessful) {
            val relatedProducts = ArrayList<ProductModel>()
            val currentProductModel: ProductModel = productResponse.body()!!

            val apiSimilarProduct = RetrofitBuilder.getInstance(typeToken, ProductSummaryItemRowDeserializer()).create(ApiRequestService::class.java)

            for (i in 0 until currentProductModel.relatedIds.size) {
                val relatedProductResponse = apiSimilarProduct.product(currentProductModel.relatedIds[i], NetworkParams.QUERY_OPTIONS_BASIC)
                if (relatedProductResponse.isSuccessful)
                    relatedProducts.add(relatedProductResponse.body()!!)
            }
            currentProductModel.relatedProductModel.addAll(relatedProducts)

            withContext(Main) {
                this@DetailViewModel.productModel.postValue(currentProductModel)
                isDataFetched.postValue(true)
            }
        }
    }
}