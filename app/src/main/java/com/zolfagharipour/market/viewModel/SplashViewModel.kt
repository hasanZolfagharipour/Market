package com.zolfagharipour.market.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.google.gson.reflect.TypeToken
import com.zolfagharipour.market.data.room.entities.CategoryModel
import com.zolfagharipour.market.data.room.entities.ProductModel
import com.zolfagharipour.market.data.room.entities.ProductRepository
import com.zolfagharipour.market.data.room.entities.SliderModel
import com.zolfagharipour.market.network.ApiRequestService
import com.zolfagharipour.market.network.CheckNetworkConnectivity
import com.zolfagharipour.market.network.NetworkParams
import com.zolfagharipour.market.network.RetrofitBuilder
import com.zolfagharipour.market.network.deserializer.CategoriesDeserializer
import com.zolfagharipour.market.network.deserializer.ProductsDeserializer
import com.zolfagharipour.market.network.deserializer.SliderDeserializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SplashViewModel(application: Application) : AndroidViewModel(application) {


    var isConnect: MutableLiveData<Boolean> = MutableLiveData(true)
    var isDataFetched: MutableLiveData<Boolean> = MutableLiveData(false)

    private var networkConnectivity = CheckNetworkConnectivity(application)
    private lateinit var lifecycleOwner: LifecycleOwner

    fun checkNetwork(lifecycleOwner: LifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner
        networkConnectivity.observe(lifecycleOwner, Observer { isConnected ->
                isConnect.value = isConnected
                if (isConnected)
                    fetchInitialProducts()
            })
    }

    private fun fetchInitialProducts() {
        viewModelScope.launch(Default) {
            val slider = async { fetchSliderItems() }
            val suggestion = async { fetchSuggestionCategory() }
            val last = async { fetchLastProduct() }
            val popular = async { fetchPopularProduct() }
            val most = async { fetchMostRating() }

            if (slider.await() && suggestion.await() && last.await() && popular.await() && most.await())
                isDataFetched.postValue(true)
        }
    }

    private suspend fun fetchSliderItems(): Boolean {

        val typeTokenSlider = object : TypeToken<SliderModel>() {}.type
        val typeAdapterSlider = SliderDeserializer()
        val sliderApi = RetrofitBuilder.getInstance(typeTokenSlider, typeAdapterSlider)
            .create(ApiRequestService::class.java)
        val sliderResponse = sliderApi.sliderItems(
            NetworkParams.CategoryID.SLIDER_ID,
            NetworkParams.QUERY_OPTIONS_BASIC
        )

        if (!sliderResponse.isSuccessful) return false
        ProductRepository.sliderHome = sliderResponse.body()!!
        return true
    }

    private suspend fun fetchSuggestionCategory(): Boolean {

        val typeAdapterCategorySuggestion = CategoriesDeserializer()
        val typeTokenCategorySuggestion = object : TypeToken<ArrayList<CategoryModel>>() {}.type

        val categoryApi =
            RetrofitBuilder.getInstance(typeTokenCategorySuggestion, typeAdapterCategorySuggestion)
                .create(ApiRequestService::class.java)
        val categorySuggestionsResponse = categoryApi.categories(NetworkParams.QUERY_OPTIONS_BASIC)

        if (!categorySuggestionsResponse.isSuccessful) return false
        ProductRepository.categoryModelSuggestion = categorySuggestionsResponse.body()!!
        return true
    }

    private suspend fun fetchLastProduct(): Boolean {
        val typeTokenProduct = object : TypeToken<ArrayList<ProductModel>>() {}.type
        val typeAdapterProduct = ProductsDeserializer()
        val productApi = RetrofitBuilder.getInstance(typeTokenProduct, typeAdapterProduct)
            .create(ApiRequestService::class.java)

        val lastResponse = productApi.products(NetworkParams.QUERY_OPTIONS_BASIC)
        if (!lastResponse.isSuccessful) return false

        ProductRepository.lastProductModels = lastResponse.body()!!
        return true
    }

    private suspend fun fetchPopularProduct(): Boolean {

        val typeTokenProduct = object : TypeToken<ArrayList<ProductModel>>() {}.type
        val typeAdapterProduct = ProductsDeserializer()
        val productApi = RetrofitBuilder.getInstance(typeTokenProduct, typeAdapterProduct)
            .create(ApiRequestService::class.java)

        val popularResponse = productApi.products(NetworkParams.QUERY_OPTIONS_POPULAR_PRODUCTS)
        if (!popularResponse.isSuccessful) return false

        ProductRepository.popularProductModels = popularResponse.body()!!
        return true
    }

    private suspend fun fetchMostRating(): Boolean {
        val typeTokenProduct = object : TypeToken<ArrayList<ProductModel>>() {}.type
        val typeAdapterProduct = ProductsDeserializer()
        val productApi = RetrofitBuilder.getInstance(typeTokenProduct, typeAdapterProduct)
            .create(ApiRequestService::class.java)

        val mostRatingResponse =
            productApi.products(NetworkParams.QUERY_OPTIONS_MOST_RATING_PRODUCTS)
        if (!mostRatingResponse.isSuccessful) return false

        ProductRepository.mostRatingProductModels = mostRatingResponse.body()!!
        return true
    }
}