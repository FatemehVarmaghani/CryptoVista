package com.example.cryptovista.features.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.cryptovista.databinding.ItemNewsBinding
import com.example.cryptovista.network.model.News
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

class NewsRecyclerAdapter(private val context: Context, private val data: List<News.Data>) :
    RecyclerView.Adapter<NewsRecyclerAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(position: Int) {

            val newsItem = data[position]

            Glide.with(context)
                .load(newsItem.imageUrl)
                .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(12, 0)))
                .into(binding.imgItemNews)

            binding.txtTitleItemNews.text = newsItem.title
            binding.txtResourceItemNews.text = newsItem.source

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bindData(position)
    }

}