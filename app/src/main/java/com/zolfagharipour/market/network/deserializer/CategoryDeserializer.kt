package com.zolfagharipour.market.network.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.zolfagharipour.market.data.room.entities.CategoryModel
import java.lang.reflect.Type

class CategoryDeserializer : JsonDeserializer<CategoryModel> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): CategoryModel {

        val jsonObject = json!!.asJsonObject

        val id = jsonObject["id"].asString
        val name = jsonObject["name"].asString
        val imageObject = jsonObject["image"].asJsonObject
        val image = imageObject["src"].asString
        val count = jsonObject["count"].asString

        return CategoryModel(id, name, image, count)

    }
}