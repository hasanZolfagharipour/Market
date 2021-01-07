package com.zolfagharipour.market.network

import com.zolfagharipour.market.data.room.entities.Product
import com.zolfagharipour.market.data.room.entities.CategoryProduct
import com.zolfagharipour.market.data.room.entities.SliderModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface ApiRequestService {
    @GET("products")
    suspend fun products(@QueryMap options: Map<String, String>): Response<ArrayList<Product>>

    @GET("products/{id}")
    suspend fun sliderItems(@Path("id" ) id: String, @QueryMap options: Map<String, String>): Response<SliderModel>

    @GET("products/{id}")
    suspend fun product(@Path("id" ) id: String, @QueryMap options: Map<String, String>): Response<Product>

    @GET("products/categories")
    suspend fun categories(@QueryMap options: Map<String, String>): Response<ArrayList<CategoryProduct>>
}