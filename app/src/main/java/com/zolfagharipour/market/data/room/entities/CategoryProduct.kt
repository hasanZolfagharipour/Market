package com.zolfagharipour.market.data.room.entities

class CategoryProduct(val id: String, val name: String, val image: String = "", val products: ArrayList<Product> = ArrayList()) {

    fun getNameFormatted(): String = "$name >"
}