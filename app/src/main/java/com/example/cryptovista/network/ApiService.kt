package com.example.cryptovista.network

import com.example.cryptovista.network.model.CoinList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://api.coingecko.com/api/v3/"

interface ApiService {

    @GET("coins/markets")
    fun getCoinList(@Query("vs_currency") vsCurrency: String = "usd"): Call<CoinList>

}