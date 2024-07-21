package com.example.cryptovista.network

import com.example.cryptovista.network.model.CoinList
import com.example.cryptovista.network.model.News
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

const val BASE_URL_COIN_GECKO = "https://api.coingecko.com/api/v3/"
const val BASE_URL_CRYPTO_COMPARE = "https://min-api.cryptocompare.com/data/v2/"
const val API_KEY_CRYPTO_COMPARE = "authorization: Apikey 6abf52672912488df15e080dace72771e2b7f3cb6a5f6b96013401704fb4a925"

interface ApiService {

    @GET("coins/markets")
    fun getCoinList(@Query("vs_currency") vsCurrency: String = "usd"): Call<CoinList>

    @GET("coins/markets")
    fun getTopCoins(
        @Query("vs_currency") vsCurrency: String = "usd",
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int = 10
    ): Call<CoinList>

    @Headers(API_KEY_CRYPTO_COMPARE)
    @GET("news/")
    fun getNews(@Query("sortOrder") sortOrder: String = "popular"): Call<News>

}