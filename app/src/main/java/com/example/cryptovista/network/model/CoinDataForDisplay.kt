package com.example.cryptovista.network.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CoinDataForDisplay(
    val id: String? = null,
    val name: String? = null,
    val marketCapRank: Int? = null,
    val currentPrice: Double? = null,
    val priceChange1D: Double? = null, // also today's change
    val priceChange1W: Double? = null,
    val priceChange2W: Double? = null,
    val priceChange1M: Double? = null,
    val priceChange2M: Double? = null,
    val priceChange1Y: Double? = null,
    val marketCap: Double? = null,
    val high24h: Double? = null,
    val low24h: Double? = null,
    val totalSupply: Double? = null,
    val totalVolume: Double? = null,
    val algorithm: String? = null,
    val description: String? = null,
    val website: String? = null,
    val twitter: String? = null,
    val facebook: String? = null,
    val reddit: String? = null,
    val githubRepo: String? = null
) : Parcelable
