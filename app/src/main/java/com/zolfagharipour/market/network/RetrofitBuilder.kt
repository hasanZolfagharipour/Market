package com.zolfagharipour.market.network

import com.google.gson.GsonBuilder
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

object RetrofitBuilder {


    fun getInstance(type: Type?, typeAdapter: Any?): Retrofit {
        return Retrofit.Builder()
            .baseUrl(NetworkParams.BASE_URL)
            .addConverterFactory(createGsonConverter(type, typeAdapter))
            .build()
    }

    private fun createGsonConverter(type: Type?, typeAdapter: Any?): Converter.Factory {
        val builder = GsonBuilder().registerTypeAdapter(type, typeAdapter)
        val gSon = builder.create()
        return GsonConverterFactory.create(gSon)
    }

}