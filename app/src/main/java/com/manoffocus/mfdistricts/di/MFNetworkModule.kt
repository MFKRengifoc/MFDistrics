package com.manoffocus.mfdistricts.di

import com.manoffocus.mfdistricts.network.PlacesAPI
import com.manoffocus.mfdistricts.repository.MFDistrictRepository
import com.manoffocus.mfdistricts.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MFNetworkModule {
    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS) // connect timeout
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }
    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit): PlacesAPI {
        return retrofit.create(PlacesAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideMfDistrictRepository(placesAPI: PlacesAPI) = MFDistrictRepository(placesAPI)
}