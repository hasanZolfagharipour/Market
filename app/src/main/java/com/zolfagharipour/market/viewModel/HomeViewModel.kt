package com.zolfagharipour.market.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.zolfagharipour.market.data.room.entities.ProductModel
import com.zolfagharipour.market.data.room.entities.CategoryModel
import com.zolfagharipour.market.data.room.entities.ProductRepository
import com.zolfagharipour.market.data.room.entities.SliderModel
import kotlin.collections.ArrayList

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    var lastProducts: MutableLiveData<ArrayList<ProductModel>> = MutableLiveData(ProductRepository.lastProductModels)
    var popularProducts: MutableLiveData<ArrayList<ProductModel>> = MutableLiveData(ProductRepository.popularProductModels)
    var mostRatingProductModel: MutableLiveData<ArrayList<ProductModel>> = MutableLiveData(ProductRepository.mostRatingProductModels)
    var categoryModelSuggestionList: MutableLiveData<ArrayList<CategoryModel>> = MutableLiveData(ProductRepository.categoryModelSuggestion)


    var slider: MutableLiveData<SliderModel> = MutableLiveData(ProductRepository.sliderHome)
    private var isLastPosition: Boolean = true

     private fun isSliderLastPosition(currentPosition: Int): Boolean{

        if (currentPosition == slider.value!!.imageList.size - 1)
            isLastPosition = true
        else if(currentPosition == 0)
            isLastPosition = false

        return isLastPosition

    }

     fun getSliderNextPosition(currentPosition: Int):Int{
       return if (isSliderLastPosition(currentPosition))
           currentPosition - 1
        else
            currentPosition + 1
    }







}