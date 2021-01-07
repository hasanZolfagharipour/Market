package com.zolfagharipour.market.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.google.gson.reflect.TypeToken
import com.zolfagharipour.market.data.room.entities.Product
import com.zolfagharipour.market.network.ApiRequestService
import com.zolfagharipour.market.network.CheckNetworkConnectivity
import com.zolfagharipour.market.network.NetworkParams
import com.zolfagharipour.market.network.RetrofitBuilder
import com.zolfagharipour.market.network.deserializer.ProductDetailDeserializer
import com.zolfagharipour.market.network.deserializer.ProductSummaryForItemRowWithIdDeserializer
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    var isDataFetched: MutableLiveData<Boolean> = MutableLiveData(false)
    private var networkConnectivity = CheckNetworkConnectivity(application)
    private lateinit var lifecycleOwner: LifecycleOwner
    var product: MutableLiveData<Product> = MutableLiveData()

    fun checkNetwork(lifecycleOwner: LifecycleOwner, product: Product) {
        this.lifecycleOwner = lifecycleOwner
        networkConnectivity.observe(
            lifecycleOwner,
            Observer { isConnected ->
                if (isConnected)
                    CoroutineScope(IO).launch { fetchProductDetail(product) }
            }
        )
    }

    private suspend fun fetchProductDetail(product: Product) {
        val typeToken = object : TypeToken<Product>() {}.type
        val typeAdapter = ProductDetailDeserializer()

        val api = RetrofitBuilder.getInstance(typeToken, typeAdapter)
            .create(ApiRequestService::class.java)


        val productResponse = api.product(product.id, NetworkParams.QUERY_OPTIONS_BASIC)

        if (productResponse.isSuccessful) {
            val relatedProducts = ArrayList<Product>()
            val currentProduct: Product = productResponse.body()!!

            val apiSimilarProduct = RetrofitBuilder.getInstance(typeToken, ProductSummaryForItemRowWithIdDeserializer()).create(ApiRequestService::class.java)

            for (i in 0 until currentProduct.relatedIds.size) {
                val relatedProductResponse = apiSimilarProduct.product(currentProduct.relatedIds[i], NetworkParams.QUERY_OPTIONS_BASIC)
                if (relatedProductResponse.isSuccessful)
                    relatedProducts.add(relatedProductResponse.body()!!)
            }
            currentProduct.relatedProduct.addAll(relatedProducts)

            withContext(Main) {
                this@DetailViewModel.product.postValue(currentProduct)
                isDataFetched.postValue(true)
            }
        }
    }
}