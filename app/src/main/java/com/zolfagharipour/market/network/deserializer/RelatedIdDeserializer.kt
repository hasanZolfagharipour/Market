package com.zolfagharipour.market.network.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.zolfagharipour.market.data.room.entities.ProductModel
import java.lang.reflect.Type

class RelatedIdDeserializer : JsonDeserializer<ProductModel> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ProductModel {


        val productObject = json?.asJsonObject
        val relatedIds = ArrayList<String>()

        val relatedIdObjects = productObject!!["related_ids"].asJsonArray

        for (i in 0 until relatedIdObjects.size()) {
            val ids = relatedIdObjects[i].asString
            relatedIds.add(ids)
        }
        return ProductModel("", "", "", "", ArrayList(), relatedIds = relatedIds)
    }
}