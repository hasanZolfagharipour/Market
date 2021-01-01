package com.zolfagharipour.market.network

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.zolfagharipour.market.data.room.entities.Product
import com.zolfagharipour.market.data.room.entities.ProductRepository
import com.zolfagharipour.market.data.room.entities.SliderModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
            if (!productObject["purchasable"].asBoolean && productObject["id"].asString == "608") {
                CoroutineScope(Dispatchers.IO).launch { getSlideImages(productObject) }

                continue
            }
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

    private fun getSlideImages(productObject: JsonObject) {

        val id = productObject["id"].asInt
        val name = productObject["name"].asString
        val imageObjects = productObject["images"].asJsonArray
        val images = ArrayList<String>()
        for (j in 0 until imageObjects.size()) {
            val imageObject = imageObjects[j].asJsonObject
            val imageUrl = imageObject["src"].asString
            images.add(imageUrl)
        }
        ProductRepository.sliderBanner = SliderModel(id, name, images)
    }


}