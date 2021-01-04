package com.zolfagharipour.market.network.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.zolfagharipour.market.data.room.entities.Product
import java.lang.reflect.Type


class ProductDeserializer : JsonDeserializer<ArrayList<Product>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ArrayList<Product> {

        val products = ArrayList<Product>()


        val productArray = json!!.asJsonArray

        for (i in 0 until productArray.size()) {
            val productObject = productArray[i].asJsonObject
            if (!productObject["purchasable"].asBoolean)
                continue
            val id = productObject["id"].asInt
            val name = productObject["name"].asString
            val price = productObject["price"].asString
            val regularPrice = productObject["regular_price"].asString
            val imageObjects = productObject["images"].asJsonArray
            val images = ArrayList<String>()
            for (j in 0 until imageObjects.size()) {
                val imageObject = imageObjects[j].asJsonObject
                val imageUrl = imageObject["src"].asString
                images.add(imageUrl)
            }
            products.add(Product(id, name, price, regularPrice, images))
        }
        return products
    }
}