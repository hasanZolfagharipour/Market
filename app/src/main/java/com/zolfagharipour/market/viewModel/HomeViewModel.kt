package com.zolfagharipour.market.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.zolfagharipour.market.data.room.entities.Product
import com.zolfagharipour.market.data.room.entities.ProductCategory
import com.zolfagharipour.market.data.room.entities.ProductRepository
import com.zolfagharipour.market.data.room.entities.SliderModel

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    var lastProducts: MutableLiveData<ArrayList<Product>> = MutableLiveData(ProductRepository.lastProducts)
    var popularProducts: MutableLiveData<ArrayList<Product>> = MutableLiveData(ProductRepository.popularProducts)
    var mostRatingProduct: MutableLiveData<ArrayList<Product>> = MutableLiveData(ProductRepository.mostRatingProducts)
    var categorySuggestion: MutableLiveData<ArrayList<ProductCategory>> = MutableLiveData(ProductRepository.categorySuggestion)

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