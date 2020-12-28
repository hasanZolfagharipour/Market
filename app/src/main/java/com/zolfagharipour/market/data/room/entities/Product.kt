package com.zolfagharipour.market.data.room.entities


data class Product(val id: Int, val name: String, val price: String, val regularPrice:String, val images: ArrayList<String>) {
}