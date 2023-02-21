package com.example.tryanimation.try_architecture_code.api

import com.example.tryanimation.try_architecture_code.utils.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiDummyInstance {

    private val retrofitBuilt by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiDummy: InterfaceApiDummy by lazy {
        retrofitBuilt.create(InterfaceApiDummy::class.java)
    }

}