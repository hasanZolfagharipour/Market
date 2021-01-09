package com.zolfagharipour.market.data.room.entities

import java.io.Serializable

class CategoryModel(val id: String, val name: String, val image: String = "", private val count: String = "", var products: ArrayList<ProductModel> = ArrayList()):Serializable {

    fun getNameFormatted(): String = "$name >"
    fun getCountFormatted(): String = "$count کالا"
}