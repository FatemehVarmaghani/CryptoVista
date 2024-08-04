package com.example.cryptovista.network

import com.example.cryptovista.network.model.ChartData
import com.example.cryptovista.network.model.CoinData
import com.example.cryptovista.network.model.CoinList
import com.example.cryptovista.network.model.News
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

const val BASE_URL_COIN_GECKO = "https://api.coingecko.com/api/v3/"
const val BASE_URL_CRYPTO_COMPARE = "https://min-api.cryptocompare.com/data/v2/"
const val API_KEY_CRYPTO_COMPARE =
    "authorization: Apikey 6abf52672912488df15e080dace72771e2b7f3cb6a5f6b96013401704fb4a925"

interface ApiService {

    // coins list for explore fragment
    @GET("coins/markets")
    fun getCoinList(@Query("vs_currency") vsCurrency: String = "usd"): Call<CoinList>

    // top coins list for home fragment
    @GET("coins/markets")
    fun getTopCoins(
        @Query("vs_currency") vsCurrency: String = "usd",
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int = 10
    ): Call<CoinList>

    // coin data for details activity (textViews)
    @Headers("x-cg-demo-api-key: CG-GsUaXB9451kxftAT8wBFHHgN")
    @GET("coins/{id}")
    fun getCoinData(
        @Path("id") id: String,
        @Query("localization") localization: Boolean = false
    ): Call<CoinData>

    // coin chart data for details activity
    @Headers("x-cg-demo-api-key: CG-GsUaXB9451kxftAT8wBFHHgN")
    @GET("coins/{id}/market_chart/range")
    fun getChartData(
        @Path("id") id: String,
        @Query("vs_currency") vsCurrency: String = "usd",
        @Query("from") from: Long,
        @Query("to") to: Long,
        @Query("precision") precision: Int = 6
    ): Call<ChartData>

    //news list (from cryptoCompare)
    @Headers(API_KEY_CRYPTO_COMPARE)
    @GET("news/")
    fun getNews(@Query("sortOrder") sortOrder: String = "popular"): Call<News>

}