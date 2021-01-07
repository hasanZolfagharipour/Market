package com.zolfagharipour.market.data.room.entities

import androidx.lifecycle.MutableLiveData

object ProductRepository {

    lateinit var lastProducts: ArrayList<Product>
    lateinit var popularProducts: ArrayList<Product>
    lateinit var mostRatingProducts: ArrayList<Product>
    lateinit var categoryProductSuggestion: ArrayList<CategoryProduct>
    lateinit var categoryProducts: ArrayList<CategoryProduct>

    var colorCategorySuggestions = listOf<String>("#EF3950", "#39AD00", "#0FAAC6", "#770FC6", "#F8A825", "#A63489", "#0041FF", "#F68618", "#EF3950", "#39AD00", "#0FAAC6", "#770FC6", "#F8A825", "#A63489", "#0041FF", "#F68618", "#EF3950", "#39AD00", "#0FAAC6", "#770FC6", "#F8A825", "#A63489", "#0041FF", "#F68618")

    var sliderHome: SliderModel? = null
    var isCategoryFechItemFetched: Boolean = false


}