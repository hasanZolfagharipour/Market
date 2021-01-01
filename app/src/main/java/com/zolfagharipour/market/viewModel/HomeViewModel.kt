package com.zolfagharipour.market.viewModel

import android.app.Application
import android.text.SpannableString
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.zolfagharipour.market.data.room.entities.Product
import com.zolfagharipour.market.data.room.entities.ProductRepository
import com.zolfagharipour.market.data.room.entities.SliderModel
import com.zolfagharipour.market.other.Utilities
import com.zolfagharipour.market.view.fragment.HomeFragmentDirections
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    var productList: MutableLiveData<ArrayList<Product>> = MutableLiveData()
    var sliderBanner: MutableLiveData<SliderModel> =
        MutableLiveData(ProductRepository.sliderBanner)
    private var isLastPosition: Boolean = true

    fun currentPrice(position: Int): String {
        val text = Utilities.separator(productList.value!![position].price)
        return "$text تومان"
    }

    fun regularPrice(position: Int): SpannableString {
        return if (productList.value!![position].price == productList.value!![position].regularPrice)
            SpannableString("")
        else
            Utilities.getSpannedText(Utilities.separator(productList.value!![position].regularPrice))
    }

    fun discountPercent(position: Int): String {
        val regularPrice = productList.value!![position].regularPrice.toDouble()
        val currentPrice = productList.value!![position].price.toDouble()

        val value = (((regularPrice - currentPrice) / regularPrice) * 100).roundToInt()

        return if (value == 0) ""
        else " $value% "
    }

     private fun isSliderLastPosition(currentPosition: Int): Boolean{

        if (currentPosition == sliderBanner.value!!.imageList.size - 1)
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