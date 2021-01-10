package com.zolfagharipour.market.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.google.gson.reflect.TypeToken
import com.zolfagharipour.market.data.room.entities.CategoryModel
import com.zolfagharipour.market.data.room.entities.ProductRepository
import com.zolfagharipour.market.network.ApiRequestService
import com.zolfagharipour.market.network.CheckNetworkConnectivity
import com.zolfagharipour.market.network.NetworkParams
import com.zolfagharipour.market.network.RetrofitBuilder
import com.zolfagharipour.market.network.deserializer.CategoriesDeserializer
import com.zolfagharipour.market.network.deserializer.CategoryDeserializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CategoryViewModel(application: Application) : AndroidViewModel(application) {


    private var networkConnectivity = CheckNetworkConnectivity(application)
    private lateinit var lifecycleOwner: LifecycleOwner



    val digitalCategories = ProductRepository.digitalCategories
    val fashionCategories = ProductRepository.fashionCategories
    val artAndBookCategories = ProductRepository.artAndBookCategories
    val superMarketCategories = ProductRepository.superMarketCategories
    val otherCategories = ProductRepository.otherCategories

    fun checkNetwork(lifecycleOwner: LifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner
        networkConnectivity.observe(
            lifecycleOwner,
            Observer { isConnected ->
                if (isConnected)
                    fetchCategories()
            }
        )
    }

    fun isDataFetched(): LiveData<Boolean> = ProductRepository.isCategoriesDataFetched

    private fun fetchCategories() {

        viewModelScope.launch(IO){
            val digital = async { fetchDigitalCategories() }
            val fashion = async { fetchFashionCategories() }
            val artBook = async { fetchArtAndBookCategories() }
            val superMarket = async { fetchSuperMarket() }
            val other = async {  fetchOtherCategories() }

            if (digital.await() && fashion.await() && artBook.await() && superMarket.await() && other.await())
                ProductRepository.isCategoriesDataFetched.postValue(true)
        }
    }

    private suspend fun fetchDigitalCategories(): Boolean{

        val typeToken = object : TypeToken<ArrayList<CategoryModel>>() {}.type
        val typeAdapter = CategoriesDeserializer()
        val api = RetrofitBuilder.getInstance(typeToken, typeAdapter).create(ApiRequestService::class.java)

        val digitalResponse = api.categories(NetworkParams.QUERY_OPTIONS_DIGITAL_CATEGORY)
        if (!digitalResponse.isSuccessful) return false

        ProductRepository.digitalCategories.postValue(digitalResponse.body())
        return true

    }

    private suspend fun fetchFashionCategories(): Boolean{
        val typeToken = object : TypeToken<ArrayList<CategoryModel>>() {}.type
        val typeAdapter = CategoriesDeserializer()
        val api = RetrofitBuilder.getInstance(typeToken, typeAdapter).create(ApiRequestService::class.java)

        val fashionResponse = api.categories(NetworkParams.QUERY_OPTIONS_FASHION_AND_MODEL_CATEGORY)
        if (!fashionResponse.isSuccessful) return false

        ProductRepository.fashionCategories.postValue(fashionResponse.body())
        return true
    }

    private suspend fun fetchArtAndBookCategories(): Boolean {
        val typeToken = object : TypeToken<ArrayList<CategoryModel>>() {}.type
        val typeAdapter = CategoriesDeserializer()
        val api = RetrofitBuilder.getInstance(typeToken, typeAdapter).create(ApiRequestService::class.java)

        val artAndBookResponse = api.categories(NetworkParams.QUERY_OPTIONS_ART_AND_BOOK_CATEGORY)
        if (!artAndBookResponse.isSuccessful) return false

        ProductRepository.artAndBookCategories.postValue(artAndBookResponse.body())
        return true
    }

    private suspend fun fetchSuperMarket(): Boolean{
        val typeToken = object : TypeToken<ArrayList<CategoryModel>>() {}.type
        val typeAdapter = CategoriesDeserializer()
        val api = RetrofitBuilder.getInstance(typeToken, typeAdapter).create(ApiRequestService::class.java)


        val superMarketResponse = api.categories(NetworkParams.QUERY_OPTIONS_SUPER_MARKET_CATEGORY)
        if (!superMarketResponse.isSuccessful) return false

        ProductRepository.superMarketCategories.postValue(superMarketResponse.body())
        return true
    }

    private suspend fun fetchOtherCategories(): Boolean{
        val typeTokenCategory = object : TypeToken<CategoryModel>() {}.type
        val typeAdapterCategory = CategoryDeserializer()
        val apiCategory = RetrofitBuilder.getInstance(typeTokenCategory, typeAdapterCategory).create(ApiRequestService::class.java)

        val healthyResponse = apiCategory.category(NetworkParams.CategoryID.HEALTHY_ID, NetworkParams.QUERY_OPTIONS_BASIC)
        val specialSaleResponse = apiCategory.category(NetworkParams.CategoryID.SPECIAL_ID, NetworkParams.QUERY_OPTIONS_BASIC)

        if (!healthyResponse.isSuccessful || !specialSaleResponse.isSuccessful) return false

        val list = ArrayList<CategoryModel>()
        list.add(healthyResponse.body()!!)
        list.add(specialSaleResponse.body()!!)
        ProductRepository.otherCategories.postValue(list)
        return true
    }


}