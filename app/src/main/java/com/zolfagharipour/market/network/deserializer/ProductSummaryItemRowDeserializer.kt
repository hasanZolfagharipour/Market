package com.zolfagharipour.market.network.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.zolfagharipour.market.data.room.entities.ProductModel
import java.lang.reflect.Type


class ProductSummaryItemRowDeserializer : JsonDeserializer<ProductModel> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ProductModel? {


        val productObject = json!!.asJsonObject
        if (!productObject["purchasable"].asBoolean)
            return null

        val id = productObject["id"].asString
        val name = productObject["name"].asString
        val price = productObject["price"].asString
        val regularPrice = productObject["regular_price"].asString
        val imageObjects = productObject["images"].asJsonArray
        val images = ArrayList<String>()
        val imageObject = imageObjects[0].asJsonObject
        val imageUrl = imageObject["src"].asString
        images.add(imageUrl)

        return ProductModel(id, name, price, regularPrice, images)
    }
}