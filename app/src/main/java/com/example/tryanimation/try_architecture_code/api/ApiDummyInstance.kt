package com.example.tryanimation.try_architecture_code.api

import com.example.tryanimation.BuildConfig
import com.example.tryanimation.try_architecture_code.utils.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


object ApiDummyInstance {

    private val retrofitBuilt by lazy {
        val okHttpClient = setOkhttpClient()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    private fun setOkhttpClient(): OkHttpClient {
        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(90, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .addInterceptor(ApiDummyInterceptor())

        if (BuildConfig.DEBUG) {
            val interceptors = HttpLoggingInterceptor()
            interceptors.level = HttpLoggingInterceptor.Level.BODY
            okHttpClient.addInterceptor(interceptors)
        }
        return okHttpClient.build()
    }

    val apiDummy: InterfaceApiDummy by lazy {
        retrofitBuilt.create(InterfaceApiDummy::class.java)
    }

}