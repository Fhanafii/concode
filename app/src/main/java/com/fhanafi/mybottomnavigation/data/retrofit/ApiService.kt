package com.fhanafi.mybottomnavigation.data.retrofit

import com.fhanafi.mybottomnavigation.data.response.DetailEventResponse
import com.fhanafi.mybottomnavigation.data.response.ListEventResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("events")
    fun getEventsWithQuery(
        @Query("active") active: String,
        @Query("q") query: String
    ): Call<ListEventResponse>


    @GET("events/{id}")
    fun getDetailEvent(@Path("id") id: String): Call<DetailEventResponse>


}
