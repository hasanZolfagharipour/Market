package com.zolfagharipour.market.network.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.zolfagharipour.market.data.room.entities.CategoryModel
import java.lang.reflect.Type

class CategoriesDeserializer: JsonDeserializer<ArrayList<CategoryModel>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ArrayList<CategoryModel> {

        val jsonArray = json!!.asJsonArray
        val list = ArrayList<CategoryModel>()

        for (i in 0 until jsonArray.size()){
            val jsonObject = jsonArray[i].asJsonObject

            val id = jsonObject["id"].asString
            val name = jsonObject["name"].asString
            val count = jsonObject["count"].asString
            val imageObject = jsonObject["image"].asJsonObject
            val image = imageObject["src"].asString


            list.add(CategoryModel(id, name, image, count))
        }
        return list
    }
}