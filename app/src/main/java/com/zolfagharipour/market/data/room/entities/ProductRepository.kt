package com.zolfagharipour.market.data.room.entities

object ProductRepository {

    var lastProducts = ArrayList<Product>()
    var popularProducts = ArrayList<Product>()
    var mostRatingProducts = ArrayList<Product>()
    var categorySuggestion = ArrayList<ProductCategory>()
    var colorCategorySuggestions = listOf<String>("#EF3950", "#39AD00", "#0FAAC6", "#770FC6", "#F8A825", "#A63489", "#0041FF", "#F68618", "#EF3950", "#39AD00", "#0FAAC6", "#770FC6", "#F8A825", "#A63489", "#0041FF", "#F68618", "#EF3950", "#39AD00", "#0FAAC6", "#770FC6", "#F8A825", "#A63489", "#0041FF", "#F68618")

    var sliderHome: SliderModel? = null


}