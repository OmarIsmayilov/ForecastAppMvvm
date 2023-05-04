package com.example.weatherappmvvm.data.service

import com.example.weatherappmvvm.utils.Constants.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {
    private var INSTANCE: Retrofit? = null
    fun getService(): Retrofit {
        if (INSTANCE == null) {

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val clientBuilder = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

            INSTANCE = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(clientBuilder)
                .build()

        }
        return INSTANCE!!
    }
}
