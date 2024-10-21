package com.fhanafi.mybottomnavigation.data.remote.retrofit

import com.fhanafi.mybottomnavigation.data.remote.response.DetailEventResponse
import com.fhanafi.mybottomnavigation.data.remote.response.ListEventResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // API call without the query parameter
    @GET("events")
    fun getEvents(@Query("active") active: String): Call<ListEventResponse>

    @GET("events/{id}")
    fun getDetailEvent(@Path("id") id: String): Call<DetailEventResponse>

    @GET("events")
    suspend fun searchEvents(
        @Query("q") query: String,
        @Query("active") active: Int = -1
    ): Response<ListEventResponse>
}
