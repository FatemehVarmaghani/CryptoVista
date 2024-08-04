package com.example.cryptovista.features.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptovista.FROM_MAIN_ACTIVITY
import com.example.cryptovista.databinding.FragmentExploreBinding
import com.example.cryptovista.features.activities.CoinDetailsActivity
import com.example.cryptovista.features.adapters.ExploreRecyclerAdapter
import com.example.cryptovista.network.ApiManager
import com.example.cryptovista.network.model.CoinList

class ExploreFragment : Fragment() {

    private lateinit var binding: FragmentExploreBinding
    private var coinList: List<CoinList.Coin> = listOf()

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

        apiManager.getCoinList(object : ApiManager.ApiCallback<CoinList> {
            override fun onSuccess(data: CoinList) {
                coinList = data
                setRecyclerView(coinList)
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
            binding.recyclerExplore.adapter = ExploreRecyclerAdapter(requireContext(), list, object : ExploreRecyclerAdapter.ExploreItemEvent {
                override fun onItemClicked(itemId: String?) {
                    val intent = Intent(context, CoinDetailsActivity::class.java)

                    if (itemId != null) {
                        intent.putExtra(FROM_MAIN_ACTIVITY, itemId)
                        startActivity(intent)
                    } else {
                        showToast("Data no found!")
                    }
                }
            })
            binding.recyclerExplore.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    private fun showToast(message: String) {
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun filterData(query: String) {
        val regex = Regex(query, RegexOption.IGNORE_CASE)
        setRecyclerView(coinList.filter { it.name!!.contains(regex) })
    }

}