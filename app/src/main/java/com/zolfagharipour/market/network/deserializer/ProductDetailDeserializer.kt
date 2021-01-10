package com.zolfagharipour.market.network.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.zolfagharipour.market.data.room.entities.CategoryModel
import com.zolfagharipour.market.data.room.entities.ProductModel
import java.lang.reflect.Type


class ProductDetailDeserializer : JsonDeserializer<ProductModel> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ProductModel {

        val productModel: ProductModel


        val productObject = json!!.asJsonObject

        val id = productObject["id"].asString
        val name = productObject["name"].asString
        val price = productObject["price"].asString
        val regularPrice = productObject["regular_price"].asString
        val imageObjects = productObject["images"].asJsonArray
        val description = productObject["description"].asString
        val totalSale = productObject["total_sales"].asString
        val rate = productObject["average_rating"].asString
        val relatedIds: ArrayList<String> = ArrayList()
        /*val relatedIdObjects = productObject["related_ids"].asJsonArray

        for (i in 0 until relatedIdObjects.size()){
            val ids = relatedIdObjects[i].asString
            relatedIds.add(ids)
        }*/
        val categoryObjects = productObject["categories"].asJsonArray
        val categories = ArrayList<CategoryModel>()
        for (i in 0 until categoryObjects.size()) {
            val categoryObject = categoryObjects[i].asJsonObject
            val ids = categoryObject["id"].asString
            val nameCategory = categoryObject["name"].asString
            categories.add(CategoryModel(ids, nameCategory))
        }
        val images = ArrayList<String>()
        for (i in 0 until imageObjects.size()) {
            val imageObject = imageObjects[i].asJsonObject
            val imageUrl = imageObject["src"].asString
            images.add(imageUrl)
        }

        productModel = ProductModel(id, name, price, regularPrice, images, description, totalSale, relatedIds, categories, rate)

        return productModel
    }
}