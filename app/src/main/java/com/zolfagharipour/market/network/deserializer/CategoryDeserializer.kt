package com.zolfagharipour.market.network.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.zolfagharipour.market.data.room.entities.ProductCategory
import java.lang.reflect.Type

class CategoryDeserializer: JsonDeserializer<ArrayList<ProductCategory>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ArrayList<ProductCategory> {

        val jsonArray = json!!.asJsonArray
        val list = ArrayList<ProductCategory>()

        for (i in 0 until jsonArray.size()){
            val jsonObject = jsonArray[i].asJsonObject

            val id = jsonObject["id"].asInt
            val name = jsonObject["name"].asString
            val imageObject = jsonObject["image"].asJsonObject
            val image = imageObject["src"].asString

            list.add(ProductCategory(id, name, image))
        }
        return list
    }
}