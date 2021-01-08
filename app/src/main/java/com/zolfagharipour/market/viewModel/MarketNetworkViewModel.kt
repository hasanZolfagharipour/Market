package com.zolfagharipour.market.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.gson.reflect.TypeToken
import com.zolfagharipour.market.data.room.entities.ProductModel
import com.zolfagharipour.market.data.room.entities.CategoryModel
import com.zolfagharipour.market.data.room.entities.ProductRepository
import com.zolfagharipour.market.data.room.entities.SliderModel
import com.zolfagharipour.market.network.*
import com.zolfagharipour.market.network.deserializer.CategoriesDeserializer
import com.zolfagharipour.market.network.deserializer.ProductsDeserializer
import com.zolfagharipour.market.network.deserializer.SliderDeserializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MarketNetworkViewModel(application: Application) : AndroidViewModel(application) {


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

        val typeTokenProduct = object: TypeToken<ArrayList<ProductModel>>() {}.type
        val typeTokenSlider = object: TypeToken<SliderModel>() {}.type
        val typeTokenCategorySuggestion = object: TypeToken<ArrayList<CategoryModel>>(){}.type

        val typeAdapterProduct = ProductsDeserializer()
        val typeAdapterSlider = SliderDeserializer()
        val typeAdapterCategorySuggestion = CategoriesDeserializer()


        val productApi = RetrofitBuilder.getInstance(typeTokenProduct, typeAdapterProduct).create(ApiRequestService::class.java)
        val sliderApi = RetrofitBuilder.getInstance(typeTokenSlider, typeAdapterSlider).create(ApiRequestService::class.java)
        val categoryApi = RetrofitBuilder.getInstance(typeTokenCategorySuggestion, typeAdapterCategorySuggestion).create(ApiRequestService::class.java)

        val lastResponse = productApi.products(NetworkParams.QUERY_OPTIONS_BASIC)
        val popularResponse = productApi.products(NetworkParams.QUERY_OPTIONS_POPULAR_PRODUCTS)
        val sliderResponse = sliderApi.sliderItems("608", NetworkParams.QUERY_OPTIONS_BASIC)
        val mostRatingResponse = productApi.products(NetworkParams.QUERY_OPTIONS_MOST_RATING_PRODUCTS)
        val categorySuggestionsResponse = categoryApi.categories(NetworkParams.QUERY_OPTIONS_BASIC)
        val categoryResponse = categoryApi.categories(NetworkParams.QUERY_OPTIONS_CATEGORY)

        if (!lastResponse.isSuccessful &&
            !popularResponse.isSuccessful &&
            !sliderResponse.isSuccessful &&
            !categorySuggestionsResponse.isSuccessful &&
            !mostRatingResponse.isSuccessful &&
            !categoryResponse.isSuccessful)
            return

        ProductRepository.lastProductModels= lastResponse.body()!!
        ProductRepository.popularProductModels = popularResponse.body()!!
        ProductRepository.sliderHome = sliderResponse.body()!!
        ProductRepository.categoryModelSuggestion = categorySuggestionsResponse.body()!!
        ProductRepository.mostRatingProductModels = mostRatingResponse.body()!!


        withContext(Main){
            isDataFetched.value = true
        }

    }

     suspend fun fetchProductsOfEachCategory(){

        val typeTokenCategorySuggestion = object: TypeToken<ArrayList<CategoryModel>>(){}.type
        val typeAdapterCategorySuggestion = CategoriesDeserializer()
        val categoryApi = RetrofitBuilder.getInstance(typeTokenCategorySuggestion, typeAdapterCategorySuggestion).create(ApiRequestService::class.java)
        val categoryResponse = categoryApi.categories(NetworkParams.QUERY_OPTIONS_CATEGORY)

        if (!categoryResponse.isSuccessful)
            return

        val typeTokenProduct = object: TypeToken<ArrayList<ProductModel>>() {}.type
        val typeAdapterProduct = ProductsDeserializer()
        val productApi = RetrofitBuilder.getInstance(typeTokenProduct, typeAdapterProduct).create(ApiRequestService::class.java)

        for (i in 0 until categoryResponse.body()!!.size) {
            val productResponse =
                productApi.products(NetworkParams.queryOptionsProductOfCategory(categoryResponse.body()!![i].id))
            if (!productResponse.isSuccessful)
                return
        }
    }



}