package com.example.cryptovista.network

import com.example.cryptovista.network.model.CoinList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://api.coingecko.com/api/v3/"

interface ApiService {

    @GET("coins/markets")
    fun getCoinList(@Query("vs_currency") vsCurrency: String = "usd"): Call<CoinList>

    @GET("coins/markets")
    fun getTopCoins(
        @Query("vs_currency") vsCurrency: String = "usd",
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int = 10
    ): Call<CoinList>

}