package com.example.cryptovista.features.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptovista.databinding.FragmentNewsBinding
import com.example.cryptovista.features.adapters.NewsRecyclerAdapter
import com.example.cryptovista.network.ApiManager
import com.example.cryptovista.network.model.News

class NewsFragment : Fragment() {

    private lateinit var binding: FragmentNewsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(layoutInflater, container, false)
        getData()
        return binding.root
    }

    private fun getData() {
        val apiManager = ApiManager()

        apiManager.getNewsList(object : ApiManager.ApiCallback<News> {
            override fun onSuccess(data: News) {
                val newsList = data.data
                Log.v("newsData", data.toString())
                Log.v("newsData", data.data.toString())
                setRecycler(newsList)
            }

            override fun onFailure(message: String) {
                showToast(message)
            }

            override fun onNullOrEmpty() {
                showToast("There is no data!")
            }
        })
    }

    private fun setRecycler(list: List<News.Data>) {
        if (context != null) {
            binding.recyclerNews.adapter = NewsRecyclerAdapter(requireContext(), list)
            binding.recyclerNews.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    private fun showToast(message: String) {
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}