package com.example.cryptovista.network

import com.example.cryptovista.network.model.CoinList
import com.example.cryptovista.network.model.News
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiManager {

    private val apiServiceCoins: ApiService
    private val apiServiceNews: ApiService

    init {
        val retrofitCoins = Retrofit.Builder()
            .baseUrl(BASE_URL_COIN_GECKO)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitNews = Retrofit.Builder()
            .baseUrl(BASE_URL_CRYPTO_COMPARE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiServiceCoins = retrofitCoins.create(ApiService::class.java)
        apiServiceNews = retrofitNews.create(ApiService::class.java)

    }

    fun getCoinList(apiCallback: ApiCallback<CoinList>) {
        apiServiceCoins.getCoinList().enqueue(object: Callback<CoinList> {
            override fun onResponse(p0: Call<CoinList>, p1: Response<CoinList>) {
                val dataReceived = p1.body()

                if (dataReceived.isNullOrEmpty()) {
                    apiCallback.onNullOrEmpty()
                } else {
                    apiCallback.onSuccess(dataReceived)
                }
            }

            override fun onFailure(p0: Call<CoinList>, p1: Throwable) {
                apiCallback.onFailure(p1.message!!)
            }
        })
    }

    fun getTopCoins(apiCallback: ApiCallback<CoinList>) {
        apiServiceCoins.getTopCoins().enqueue(object: Callback<CoinList> {
            override fun onResponse(p0: Call<CoinList>, p1: Response<CoinList>) {
                val dataReceived = p1.body()

                if (dataReceived.isNullOrEmpty()) {
                    apiCallback.onNullOrEmpty()
                } else {
                    apiCallback.onSuccess(dataReceived)
                }
            }

            override fun onFailure(p0: Call<CoinList>, p1: Throwable) {
                apiCallback.onFailure(p1.message!!)
            }
        })
    }

    fun getNewsList(apiCallback: ApiCallback<News>) {
        apiServiceNews.getNews().enqueue(object: Callback<News> {
            override fun onResponse(p0: Call<News>, p1: Response<News>) {
                val receivedData = p1.body()
                if (receivedData != null) {
                    apiCallback.onSuccess(receivedData)
                } else {
                    apiCallback.onNullOrEmpty()
                }
            }

            override fun onFailure(p0: Call<News>, p1: Throwable) {
                apiCallback.onFailure(p1.message!!)
            }
        })
    }

    interface ApiCallback<T> {
        fun onSuccess(data: T)
        fun onFailure(message: String)
        fun onNullOrEmpty()
    }

}