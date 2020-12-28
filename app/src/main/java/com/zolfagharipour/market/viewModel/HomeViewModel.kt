package com.zolfagharipour.market.viewModel

import android.app.Application
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zolfagharipour.market.data.room.entities.Product
import com.zolfagharipour.market.other.Utilities

class HomeViewModel(application: Application) : AndroidViewModel(application) {

     var productList: MutableLiveData<ArrayList<Product>> = MutableLiveData()

     fun currentPrice(position: Int): String{
          val text = Utilities.separator(productList.value!![position].price)
          return "$text تومان"
     }

     fun regularPrice(position: Int): SpannableString{
          return Utilities.getSpannedText(Utilities.separator(productList.value!![position].regularPrice))
     }



}