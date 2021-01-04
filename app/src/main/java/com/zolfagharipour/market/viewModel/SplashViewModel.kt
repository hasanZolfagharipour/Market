package com.zolfagharipour.market.viewModel

import android.app.Application
import android.content.res.TypedArray
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.gson.reflect.TypeToken
import com.zolfagharipour.market.R
import com.zolfagharipour.market.data.room.entities.Product
import com.zolfagharipour.market.data.room.entities.ProductCategory
import com.zolfagharipour.market.data.room.entities.ProductRepository
import com.zolfagharipour.market.data.room.entities.SliderModel
import com.zolfagharipour.market.network.*
import com.zolfagharipour.market.network.deserializer.CategoryDeserializer
import com.zolfagharipour.market.network.deserializer.ProductDeserializer
import com.zolfagharipour.market.network.deserializer.SliderDeserializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
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
                    CoroutineScope(Default).launch { fetchInitialProducts() }
            }
        )
    }


    private suspend fun fetchInitialProducts() {

        val typeTokenProduct = object: TypeToken<ArrayList<Product>>() {}.type
        val typeTokenSlider = object: TypeToken<SliderModel>() {}.type
        val typeTokenCategorySuggestion = object: TypeToken<ArrayList<ProductCategory>>(){}.type

        val typeAdapterProduct = ProductDeserializer()
        val typeAdapterSlider = SliderDeserializer()
        val typeAdapterCategorySuggestion = CategoryDeserializer()


        val productApi = RetrofitBuilder.getInstance(typeTokenProduct, typeAdapterProduct).create(ApiRequestService::class.java)
        val sliderApi = RetrofitBuilder.getInstance(typeTokenSlider, typeAdapterSlider).create(ApiRequestService::class.java)
        val categorySuggestionApi = RetrofitBuilder.getInstance(typeTokenCategorySuggestion, typeAdapterCategorySuggestion).create(ApiRequestService::class.java)

        val lastResponse = productApi.products(NetworkParams.QUERY_OPTIONS_BASIC)
        val popularResponse = productApi.products(NetworkParams.QUERY_OPTIONS_POPULAR_PRODUCTS)
        val sliderResponse = sliderApi.slideItems("608", NetworkParams.QUERY_OPTIONS_BASIC)
        val mostRatingResponse = productApi.products(NetworkParams.QUERY_OPTIONS_MOST_RATING_PRODUCTS)
        val categorySuggestionsResponse = categorySuggestionApi.suggestionCategory(NetworkParams.QUERY_OPTIONS_BASIC)

        if (!lastResponse.isSuccessful && !popularResponse.isSuccessful && !sliderResponse.isSuccessful && !categorySuggestionsResponse.isSuccessful && !mostRatingResponse.isSuccessful)
            return

        ProductRepository.lastProducts.addAll(lastResponse.body()!!)
        ProductRepository.popularProducts.addAll(popularResponse.body()!!)
        ProductRepository.sliderHome = sliderResponse.body()!!
        ProductRepository.categorySuggestion.addAll(categorySuggestionsResponse.body()!!)

        ProductRepository.mostRatingProducts.addAll(mostRatingResponse.body()!!)

        withContext(Main){
            isDataFetched.value = true
        }

    }



}