package com.zolfagharipour.market.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.google.gson.reflect.TypeToken
import com.zolfagharipour.market.data.room.entities.CategoryModel
import com.zolfagharipour.market.data.room.entities.ProductModel
import com.zolfagharipour.market.network.ApiRequestService
import com.zolfagharipour.market.network.CheckNetworkConnectivity
import com.zolfagharipour.market.network.NetworkParams
import com.zolfagharipour.market.network.RetrofitBuilder
import com.zolfagharipour.market.network.deserializer.ProductsDeserializer
import com.zolfagharipour.market.other.TAG
import com.zolfagharipour.market.other.Utilities
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class ProductsCategoryViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var category: CategoryModel
    private var networkConnectivity = CheckNetworkConnectivity(application)
    private lateinit var lifecycleOwner: LifecycleOwner
    private lateinit var apiService: ApiRequestService
    var isDataFetched: MutableLiveData<Boolean> = MutableLiveData(false)

    var isConnected: Boolean = true
    var isAllDataFetched = false


    var showLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    var showDisconnected: MutableLiveData<Boolean> = MutableLiveData(false)
    var isLoadingMore: MutableLiveData<Boolean> = MutableLiveData(false)
    var page: Int = 2


    fun searchTitle(): String = "جستجو در ${category.name}"

    fun checkNetwork(lifecycleOwner: LifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner
        networkConnectivity.observe(
            lifecycleOwner,
            Observer { isConnected ->
                this.isConnected = isConnected
                showLoading(isConnected)
                showDisconnected(isConnected)
                fetchMoreItem(isConnected)
                if (isConnected)
                    viewModelScope.launch(IO + Utilities.exceptionHandler) {
                        if (!isDataFetched.value!!)
                            fetchItems()
                    }
            }
        )
    }

    private fun showLoading(isConnected: Boolean) {
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

    private fun showDisconnected(isConnected: Boolean) {
        if (isConnected)
            showDisconnected.postValue(false)
        else {
            isDataFetched.observe(owner = lifecycleOwner, onChanged = {
                if (it)
                    showDisconnected.postValue(false)
                else
                    showDisconnected.postValue(true)
            })
        }

    }

    private suspend fun fetchItems() {

        val typeToken = object : TypeToken<ArrayList<ProductModel>>() {}.type
        val typeAdapter = ProductsDeserializer()

        apiService = RetrofitBuilder.getInstance(typeToken, typeAdapter)
            .create(ApiRequestService::class.java)

        val categoryResponse = apiService.products(queryOptions(1))

        if (categoryResponse.isSuccessful) {
            category.products.addAll(categoryResponse.body()!!)
            isDataFetched.postValue(true)
        }
    }

    private fun queryOptions(currentPage: Int): HashMap<String, String> {
        return when (category.id) {

            NetworkParams.CategoryID.POPULAR_PRODUCTS_ID -> NetworkParams.queryOptionsProductsByOrder(
                NetworkParams.ORDER_BY_POPULAR,
                NetworkParams.NUMBER_OF_PER_PAGE_PRODUCTS_IN_CATEGORY,
                currentPage
            )
            NetworkParams.CategoryID.LAST_PRODUCTS_ID -> NetworkParams.queryOptionsProductsByOrder(
                NetworkParams.ORDER_BY_DATE,
                NetworkParams.NUMBER_OF_PER_PAGE_PRODUCTS_IN_CATEGORY,
                currentPage
            )
            NetworkParams.CategoryID.BEST_PRODUCTS_ID -> NetworkParams.queryOptionsProductsByOrder(
                NetworkParams.ORDER_BY_RATE,
                NetworkParams.NUMBER_OF_PER_PAGE_PRODUCTS_IN_CATEGORY,
                currentPage
            )
            else -> NetworkParams.queryOptionsProductInCategory(
                category.id,
                currentPage
            )
        }
    }

    private fun fetchMoreItem(isConnected: Boolean) {
        isLoadingMore.observe(owner = lifecycleOwner, {
            if (it && isConnected) {
                viewModelScope.launch(IO + Utilities.exceptionHandler) {
                    val categoryResponse = apiService.products(queryOptions(page))
                    if (categoryResponse.isSuccessful) {
                        if (categoryResponse.body()?.size == 0 )
                            isAllDataFetched = true
                        category.products.addAll(categoryResponse.body()!!)
                        isLoadingMore.postValue(false)
                        page++
                    }
                }
            }
        })
    }


}