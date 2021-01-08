package com.zolfagharipour.market.data.room.entities
object ProductRepository {

    lateinit var lastProductModels: ArrayList<ProductModel>
    lateinit var popularProductModels: ArrayList<ProductModel>
    lateinit var mostRatingProductModels: ArrayList<ProductModel>
    lateinit var categoryModelSuggestion: ArrayList<CategoryModel>


    var colorCategorySuggestions = listOf("#EF3950", "#39AD00", "#0FAAC6", "#770FC6", "#F8A825", "#A63489", "#0041FF", "#F68618", "#EF3950", "#39AD00", "#0FAAC6", "#770FC6", "#F8A825", "#A63489", "#0041FF", "#F68618", "#EF3950", "#39AD00", "#0FAAC6", "#770FC6", "#F8A825", "#A63489", "#0041FF", "#F68618")

    var sliderHome: SliderModel? = null

}