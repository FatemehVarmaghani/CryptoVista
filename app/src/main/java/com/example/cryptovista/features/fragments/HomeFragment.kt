package com.example.cryptovista.features.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptovista.databinding.FragmentHomeBinding
import com.example.cryptovista.features.adapters.HomeRecyclerAdapter
import com.example.cryptovista.network.ApiManager
import com.example.cryptovista.network.model.CoinList

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        getData()
        return binding.root
    }

    private fun getData() {
        val apiManager = ApiManager()

        apiManager.getTopCoins(object: ApiManager.ApiCallback {
            override fun onSuccess(data: CoinList) {
                setRecycler(data)
            }

            override fun onFailure(message: String) {
                showToast(message)
            }

            override fun onNullOrEmpty() {
                showToast("There is no data!")
            }
        })
    }

    private fun setRecycler(list: List<CoinList.Coin>) {
        if (context != null) {
            binding.recyclerHome.adapter = HomeRecyclerAdapter(requireContext(), list)
            binding.recyclerHome.layoutManager = LinearLayoutManager(context)
        }
    }

    private fun showToast(message: String) {
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

}