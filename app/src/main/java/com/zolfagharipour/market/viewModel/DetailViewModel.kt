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
import com.zolfagharipour.market.network.deserializer.RelatedIdDeserializer
import com.zolfagharipour.market.other.Utilities
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    var isDataFetched: MutableLiveData<Boolean> = MutableLiveData(false)
    var isSimilarProductFetched: MutableLiveData<Boolean> = MutableLiveData(false)

    var showLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    var showDisconnected: MutableLiveData<Boolean> = MutableLiveData(false)

    private var networkConnectivity = CheckNetworkConnectivity(application)
    private lateinit var lifecycleOwner: LifecycleOwner
    var product: MutableLiveData<ProductModel> = MutableLiveData()

    private lateinit var apiSimilarProduct: ApiRequestService
    private val relatedProducts = ArrayList<ProductModel>()

    fun checkNetwork(lifecycleOwner: LifecycleOwner, product: ProductModel) {
        this.lifecycleOwner = lifecycleOwner
        networkConnectivity.observe(
            lifecycleOwner,
            Observer { isConnected ->
                showLoading(isConnected)
                showDisconnected(isConnected)
                if (isConnected)
                    fetchItems(product)
            }
        )
    }

    private fun showDisconnected(isConnected: Boolean) {
        if (isConnected)
            showDisconnected.postValue(false)
        else{
            isDataFetched.observe(owner = lifecycleOwner, onChanged = {
                if (it)
                    showDisconnected.postValue(false)
                else
                    showDisconnected.postValue(true)
            })
        }

    }

    private fun showLoading(isConnected: Boolean){
        isDataFetched.observe(owner = lifecycleOwner, onChanged = { itOuter ->
            if (itOuter)
                showLoading.postValue(false)
            else {
                if (isConnected)
                    showLoading.postValue(true)
                else
                    showLoading.postValue(false)
            }
        })
    }

    private fun fetchItems(product: ProductModel) {
        viewModelScope.launch(IO + Utilities.exceptionHandler) {
            async {
                if (!isDataFetched.value!!)
                fetchProductDetail(product) }
            async {
                if (!isSimilarProductFetched.value!!)
                fetchSimilarProduct(product) }
        }
    }

    private suspend fun fetchProductDetail(productModel: ProductModel) {
        val typeToken = object : TypeToken<ProductModel>() {}.type
        val typeAdapter = ProductDetailDeserializer()
        val api = RetrofitBuilder.getInstance(typeToken, typeAdapter)
            .create(ApiRequestService::class.java)


        val productResponse = api.product(productModel.id, NetworkParams.QUERY_OPTIONS_BASIC)


        val tempRelatedProduct = ArrayList<ProductModel>()

        if (productResponse.isSuccessful) {
            val tempProduct = productResponse.body()

            val apiSimilarProduct =
                RetrofitBuilder.getInstance(typeToken, ProductSummaryItemRowDeserializer())
                    .create(ApiRequestService::class.java)
            for (i in 0 until tempProduct!!.relatedIds.size) {
                val relatedProductResponse = apiSimilarProduct.product(
                    tempProduct.relatedIds[i],
                    NetworkParams.QUERY_OPTIONS_BASIC
                )
                if (relatedProductResponse.isSuccessful)
                    relatedProductResponse.body()?.let { tempRelatedProduct.add(it) }
            }

            tempProduct.relatedProductModel.addAll(tempRelatedProduct)
            product.postValue(tempProduct)
            isDataFetched.postValue(true)
        }
    }

    private suspend fun fetchSimilarProduct(product: ProductModel) {

        val typeToken = object : TypeToken<ProductModel>() {}.type
        val typeAdapter = RelatedIdDeserializer()
        val api = RetrofitBuilder.getInstance(typeToken, typeAdapter)
            .create(ApiRequestService::class.java)
        val productResponse = api.product(product.id, NetworkParams.QUERY_OPTIONS_BASIC)

        if (productResponse.isSuccessful) {
            val tempProduct = productResponse.body()

            apiSimilarProduct = RetrofitBuilder.getInstance(typeToken, ProductSummaryItemRowDeserializer()).create(ApiRequestService::class.java)

            val deferred = ArrayList<Deferred<ProductModel?>>()
            viewModelScope.launch(IO + Utilities.exceptionHandler) {
            for (i in 0 until tempProduct!!.relatedIds.size) {
                async { fetchSimilarItem(tempProduct.relatedIds[i]) }.let {
                    deferred.add(it)
                }
            }

                for (i in 0 until deferred.size)
                {
                    deferred[i].await()?.let { relatedProducts.add(it) }
                }
                this@DetailViewModel.product.value!!.relatedProductModel.addAll(relatedProducts)
                isSimilarProductFetched.postValue(true)

            }
        }
    }

    private suspend fun fetchSimilarItem(id: String):ProductModel? {
        val relatedProductResponse = apiSimilarProduct.product(id, NetworkParams.QUERY_OPTIONS_BASIC)
        return if (relatedProductResponse.isSuccessful && relatedProductResponse.body() != null)
            relatedProductResponse.body()
        else
            null
    }
}