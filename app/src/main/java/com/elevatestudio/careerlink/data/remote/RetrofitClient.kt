package com.elevatestudio.careerlink.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    // Pastikan IP ini benar untuk Emulator
    private const val BASE_URL = "http://192.168.100.32:3000/"

    // Tambahkan settingan Timeout biar gak gampang putus saat upload file
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS) // Waktu tunggu connect
        .readTimeout(30, TimeUnit.SECONDS)    // Waktu tunggu respon server
        .writeTimeout(30, TimeUnit.SECONDS)   // Waktu tunggu upload file
        .build()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // <--- Pasang client di sini
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}