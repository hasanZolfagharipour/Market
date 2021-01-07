package com.zolfagharipour.market.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.zolfagharipour.market.data.room.entities.CategoryProduct
import com.zolfagharipour.market.data.room.entities.ProductRepository

class CategoryViewModel(application: Application) : AndroidViewModel(application) {
    var categoryProductList: MutableLiveData<ArrayList<CategoryProduct>> = MutableLiveData(ProductRepository.categoryProducts)
}