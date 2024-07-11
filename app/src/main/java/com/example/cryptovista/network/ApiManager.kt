package com.example.cryptovista.network

import com.example.cryptovista.network.model.CoinList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiManager {

    private val apiService: ApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    fun getCoinList(apiCallback: ApiCallback) {
        apiService.getCoinList().enqueue(object: Callback<CoinList> {
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

    fun getTopCoins(apiCallback: ApiCallback) {
        apiService.getTopCoins().enqueue(object: Callback<CoinList> {
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

    interface ApiCallback {
        fun onSuccess(data: CoinList)
        fun onFailure(message: String)
        fun onNullOrEmpty()
    }

}