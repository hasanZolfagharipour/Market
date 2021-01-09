package com.zolfagharipour.market.data.room.entities

import androidx.lifecycle.MutableLiveData

object ProductRepository {

    lateinit var lastProductModels: ArrayList<ProductModel>
    lateinit var popularProductModels: ArrayList<ProductModel>
    lateinit var mostRatingProductModels: ArrayList<ProductModel>
    lateinit var categoryModelSuggestion: ArrayList<CategoryModel>

    val digitalCategories = MutableLiveData<ArrayList<CategoryModel>>()
    val fashionCategories = MutableLiveData<ArrayList<CategoryModel>>()
    val artAndBookCategories = MutableLiveData<ArrayList<CategoryModel>>()
    val superMarketCategories = MutableLiveData<ArrayList<CategoryModel>>()
    val otherCategories = MutableLiveData<ArrayList<CategoryModel>>()
    var isCategoriesDataFetched: MutableLiveData<Boolean> = MutableLiveData(false)



    var colorCategorySuggestions = listOf("#EF3950", "#39AD00", "#0FAAC6", "#770FC6", "#F8A825", "#A63489", "#0041FF", "#F68618", "#EF3950", "#39AD00", "#0FAAC6", "#770FC6", "#F8A825", "#A63489", "#0041FF", "#F68618", "#EF3950", "#39AD00", "#0FAAC6", "#770FC6", "#F8A825", "#A63489", "#0041FF", "#F68618")

    var sliderHome: SliderModel? = null

}