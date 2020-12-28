package com.zolfagharipour.market.network

import com.zolfagharipour.market.data.room.entities.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiRequestService {
    @GET("products")
    suspend fun getLastProducts(@QueryMap options: Map<String, String>): Response<ArrayList<Product>>
}