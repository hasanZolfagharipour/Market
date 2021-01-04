package com.zolfagharipour.market.network.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.zolfagharipour.market.data.room.entities.SliderModel
import java.lang.reflect.Type

class SliderDeserializer: JsonDeserializer<SliderModel> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): SliderModel {
        val sliderObject = json!!.asJsonObject

        val id = sliderObject["id"].asInt
        val name = sliderObject["name"].asString
        val imageObjects = sliderObject["images"].asJsonArray
        val images = ArrayList<String>()
        for (j in 0 until imageObjects.size()) {
            val imageObject = imageObjects[j].asJsonObject
            val imageUrl = imageObject["src"].asString
            images.add(imageUrl)
        }
        return SliderModel(id, name, images)

    }
}