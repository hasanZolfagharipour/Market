package com.zolfagharipour.market.data.room.entities

class CategoryModel(val id: String, val name: String, val image: String = "", private val count: String = "", val productModels: ArrayList<ProductModel> = ArrayList()) {

    fun getNameFormatted(): String = "$name >"
    fun getCountFormatted(): String = "$count کالا"
}