package com.example.cryptovista.features.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptovista.R
import com.example.cryptovista.databinding.ItemCoinHomeBinding
import com.example.cryptovista.network.model.CoinList
import java.text.DecimalFormat

class HomeRecyclerAdapter(private val context: Context, private val data: List<CoinList.Coin>): RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {

    inner class HomeViewHolder(private val binding: ItemCoinHomeBinding): RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindData(position: Int) {

            binding.txtName.text = data[position].name
            binding.txtSymbol.text = data[position].symbol

            Glide.with(binding.root)
                .load(data[position].image)
                .into(binding.img)

            binding.txtPrice.text = "$${data[position].currentPrice.toString()}"

            val percentage = data[position].priceChangePercentage24h
            setChangeItem(percentage)

            val change = data[position].priceChange24h
            setChangeIconItem(change)

            binding.txt24hVolume.text = "$${data[position].totalVolume}"
            binding.txtMarketCap.text = "$${data[position].marketCap}"

        }

        @SuppressLint("SetTextI18n")
        private fun setChangeItem(percentage: Double?) {
            if (percentage != null) {
                val formattedPercentage = DecimalFormat("#.#").format(percentage)
                binding.txt24hChange.text = "$formattedPercentage%"
            } else {
                binding.txt24hChange.text = "N/A"
            }
        }

        private fun setChangeIconItem(change: Double?) {
            if (change == null || change == 0.0) {
                binding.txtChangeIcon.text = ""
            } else if (change > 0) {
                binding.txtChangeIcon.text = "▲"
                binding.txtChangeIcon.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.green
                    )
                )
                binding.txt24hChange.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.green
                    )
                )
            } else {
                binding.txtChangeIcon.text = "▼"
                binding.txtChangeIcon.setTextColor(ContextCompat.getColor(context, R.color.red))
                binding.txt24hChange.setTextColor(ContextCompat.getColor(context, R.color.red))
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = ItemCoinHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bindData(position)
    }

}