package com.example.cryptovista.features.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptovista.databinding.ItemCryptoCurrencyBinding

class ExploreRecyclerAdapter(private val data: List<Int>): RecyclerView.Adapter<ExploreRecyclerAdapter.ExploreViewHolder>() {

    inner class ExploreViewHolder(private val binding: ItemCryptoCurrencyBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindData(position: Int) {}
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

}