package com.example.cryptovista.features.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptovista.R
import com.example.cryptovista.databinding.ItemCryptoCurrencyBinding
import com.example.cryptovista.network.model.CoinList

class ExploreRecyclerAdapter(private val context: Context, private val data: List<CoinList.Coin>): RecyclerView.Adapter<ExploreRecyclerAdapter.ExploreViewHolder>() {

    inner class ExploreViewHolder(private val binding: ItemCryptoCurrencyBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindData(position: Int) {
            binding.txtNameItem.text = handleNullableData(data[position].name).toString()
            binding.txtSymbolItem.text = handleNullableData(data[position].symbol).toString()

            Glide.with(context)
                .load(data[position].image)
                .into(binding.imgItem)

            binding.txtPriceItem.text = "$${data[position].currentPrice.toString()}"
            binding.txt24hChangeItem.text = "${handleNullableData(data[position].priceChangePercentage24h).toString()}%"
            val change = data[position].priceChange24h
            setChangeIconItem(change)

            binding.txtMarketCapItem.text = "$${data[position].marketCap.toString()}"
            binding.txt24hVolumeItem.text = "$${data[position].totalVolume.toString()}"
        }

        private fun setChangeIconItem(change: Double?) {
            if (change == null || change == 0.0) {
                binding.txtChangeIconItem.text = ""
            } else if (change > 0) {
                binding.txtChangeIconItem.text = "▲"
                binding.txtChangeIconItem.setTextColor(ContextCompat.getColor(context, R.color.green))
                binding.txt24hChangeItem.setTextColor(ContextCompat.getColor(context, R.color.green))
            } else {
                binding.txtChangeIconItem.text = "▼"
                binding.txtChangeIconItem.setTextColor(ContextCompat.getColor(context, R.color.red))
                binding.txt24hChangeItem.setTextColor(ContextCompat.getColor(context, R.color.red))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreViewHolder {
        val binding = ItemCryptoCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExploreViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ExploreViewHolder, position: Int) {
        holder.bindData(position)
    }

    private fun handleNullableData(data: Any?): Any {
        return data ?: "N/A"
    }

}