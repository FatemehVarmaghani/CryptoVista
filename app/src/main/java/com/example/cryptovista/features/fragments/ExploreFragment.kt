package com.example.cryptovista.features.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptovista.databinding.FragmentExploreBinding
import com.example.cryptovista.features.adapters.ExploreRecyclerAdapter
import com.example.cryptovista.network.ApiManager
import com.example.cryptovista.network.model.CoinList

class ExploreFragment : Fragment() {

    private lateinit var binding: FragmentExploreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExploreBinding.inflate(layoutInflater, container, false)
        getData()
        return binding.root
    }

    private fun getData() {
        val apiManager = ApiManager()

        apiManager.getCoinList(object : ApiManager.ApiCallback {
            override fun onSuccess(data: CoinList) {
                setRecyclerView(data)
            }

            override fun onFailure(message: String) {
                showToast("Failed to get data!")
            }

            override fun onNullOrEmpty() {
                showToast("There is no data!")
            }
        })
    }

    private fun setRecyclerView(list: List<CoinList.Coin>) {
        if (context != null) {
            binding.recyclerExplore.adapter = ExploreRecyclerAdapter(requireContext(), list)
            binding.recyclerExplore.layoutManager = LinearLayoutManager(context)
        }
    }

    private fun showToast(message: String) {
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

}