package com.manoffocus.mfdistricts.network

import com.manoffocus.mfdistricts.models.MFDistrictRequest
import retrofit2.http.GET
import retrofit2.http.Path

interface PlacesAPI {
    @GET("districts/{id}")
    suspend fun getDistrictsById(@Path("id") id: Int) : MFDistrictRequest
}