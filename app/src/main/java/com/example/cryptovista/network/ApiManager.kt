package com.example.cryptovista.network

import android.util.Log
import com.example.cryptovista.network.model.ChartData
import com.example.cryptovista.network.model.CoinData
import com.example.cryptovista.network.model.CoinDataForDisplay
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

    // for explore fragment
    fun getCoinList(apiCallback: ApiCallback<CoinList>) {
        apiServiceCoins.getCoinList().enqueue(object : Callback<CoinList> {
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

    // for home fragment
    fun getTopCoins(apiCallback: ApiCallback<CoinList>) {
        apiServiceCoins.getTopCoins().enqueue(object : Callback<CoinList> {
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

    // for news fragment
    fun getNewsList(apiCallback: ApiCallback<News>) {
        apiServiceNews.getNews().enqueue(object : Callback<News> {
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

    // for details activity (all data except for chart)
    fun getCoinData(coinId: String, apiCallback: ApiCallback<CoinDataForDisplay>) {
        apiServiceCoins.getCoinData(coinId).enqueue(object : Callback<CoinData> {
            override fun onResponse(p0: Call<CoinData>, p1: Response<CoinData>) {
                val receivedData = p1.body()
                Log.v("originalData", receivedData.toString())
                if (receivedData != null) {
                    val displayData = changeDataToDisplay(receivedData)
                    apiCallback.onSuccess(displayData)
                } else {
                    apiCallback.onNullOrEmpty()
                }
            }

            override fun onFailure(p0: Call<CoinData>, p1: Throwable) {
                Log.v("originalData", p1.message!!)
                apiCallback.onFailure(p1.message!!)
            }
        })
    }

    // for details activity (chart data)
    fun getCoinChartData(
        coinId: String,
        from: Long,
        to: Long,
        apiCallback: ApiCallback<ChartData>
    ) {
        apiServiceCoins.getChartData(id = coinId, from = from, to = to)
            .enqueue(object : Callback<ChartData> {
                override fun onResponse(p0: Call<ChartData>, p1: Response<ChartData>) {
                    val receivedData = p1.body()
                    if (receivedData != null) {
                        apiCallback.onSuccess(receivedData)
                    } else {
                        apiCallback.onNullOrEmpty()
                    }
                }

                override fun onFailure(p0: Call<ChartData>, p1: Throwable) {
                    apiCallback.onFailure(p1.message!!)
                }
            })
    }

    // changing CoinData to CoinDataForDisplay
    private fun changeDataToDisplay(data: CoinData): CoinDataForDisplay {

        var coinData = CoinDataForDisplay()

        try {
            coinData = CoinDataForDisplay(
                id = tryFetch { data.id },
                name = tryFetch { data.name },
                marketCapRank = tryFetch { data.marketCapRank },
                currentPrice = tryFetch { data.marketData?.currentPrice?.usd },
                priceChange1D = tryFetch { data.marketData?.priceChangePercentage24h },
                priceChange1W = tryFetch { data.marketData?.priceChangePercentage7d },
                priceChange2W = tryFetch { data.marketData?.priceChangePercentage14d },
                priceChange1M = tryFetch { data.marketData?.priceChangePercentage30d },
                priceChange2M = tryFetch { data.marketData?.priceChangePercentage60d },
                priceChange1Y = tryFetch { data.marketData?.priceChangePercentage1y },
                marketCap = tryFetch { data.marketData?.marketCap?.usd },
                high24h = tryFetch { data.marketData?.high24h?.usd },
                low24h = tryFetch { data.marketData?.low24h?.usd },
                totalSupply = tryFetch { data.marketData?.totalSupply },
                totalVolume = tryFetch { data.marketData?.totalVolume?.usd },
                algorithm = tryFetch { data.hashingAlgorithm },
                website = tryFetch { data.links?.homepage?.get(0) },
                twitter = tryFetch { data.links?.twitterScreenName },
                facebook = tryFetch { data.links?.facebookUsername },
                reddit = tryFetch { data.links?.subredditUrl },
                githubRepo = tryFetch { data.links?.reposUrl?.github?.get(0) },
                description = tryFetch { data.description?.en }
            )
        } catch (e: Exception) {
            Log.v("apiManager", "Data Couldn't be fetched!")
        }
        Log.v("apiManager", coinData.toString())
        return coinData
    }

    private inline fun <T> tryFetch(block: () -> T?): T? {
        return try {
            block()
        } catch (e: Exception) {
            null
        }
    }

    interface ApiCallback<T> {
        fun onSuccess(data: T)
        fun onFailure(message: String)
        fun onNullOrEmpty()
    }

}