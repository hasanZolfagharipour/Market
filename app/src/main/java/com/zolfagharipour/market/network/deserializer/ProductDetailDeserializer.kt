package com.zolfagharipour.market.network.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.zolfagharipour.market.data.room.entities.CategoryProduct
import com.zolfagharipour.market.data.room.entities.Product
import java.lang.reflect.Type


class ProductDetailDeserializer : JsonDeserializer<Product> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Product {

        val product: Product


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
        val relatedIdObjects = productObject["related_ids"].asJsonArray

        for (i in 0 until relatedIdObjects.size()){
            val ids = relatedIdObjects[i].asString
            relatedIds.add(ids)
        }
        val categoryObjects = productObject["categories"].asJsonArray
        val categories = ArrayList<CategoryProduct>()
        for (i in 0 until categoryObjects.size())
        {
            val categoryObject = categoryObjects[i].asJsonObject
            val ids = categoryObject["id"].asString
            val name = categoryObject["name"].asString
            categories.add(CategoryProduct(ids, name))
        }
        val images = ArrayList<String>()
        for (j in 0 until imageObjects.size()) {
            val imageObject = imageObjects[j].asJsonObject
            val imageUrl = imageObject["src"].asString
            images.add(imageUrl)
        }
        product = Product(id, name, price, regularPrice, images, description, totalSale, relatedIds, categories, rate)

        return product
    }
}