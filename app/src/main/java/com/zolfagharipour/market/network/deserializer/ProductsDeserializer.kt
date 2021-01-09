package com.zolfagharipour.market.network.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.zolfagharipour.market.data.room.entities.ProductModel
import java.lang.reflect.Type


class ProductsDeserializer : JsonDeserializer<ArrayList<ProductModel>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ArrayList<ProductModel> {

        val products = ArrayList<ProductModel>()


        val productArray = json!!.asJsonArray

        for (i in 0 until productArray.size()) {
            val productObject = productArray[i].asJsonObject
            if (!productObject["purchasable"].asBoolean)
                continue
            val id = productObject["id"].asString
            val name = productObject["name"].asString
            val price = productObject["price"].asString
            val regularPrice = productObject["regular_price"].asString
            val imageObjects = productObject["images"].asJsonArray
            val rate = productObject["average_rating"].asString
            val images = ArrayList<String>()

            val imageObject = imageObjects[0].asJsonObject
            val imageUrl = imageObject["src"].asString
            images.add(imageUrl)

            products.add(ProductModel(id, name, price, regularPrice, images, rate = rate))
        }
        return products
    }
}