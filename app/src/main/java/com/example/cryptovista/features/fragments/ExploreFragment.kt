package com.example.cryptovista.features.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptovista.databinding.FragmentExploreBinding
import com.example.cryptovista.features.activities.FROM_MAIN_ACTIVITY
import com.example.cryptovista.features.adapters.ExploreRecyclerAdapter
import com.example.cryptovista.network.model.CoinList

class ExploreFragment : Fragment() {

    private lateinit var binding: FragmentExploreBinding
    private lateinit var coinList: List<CoinList.Coin>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        coinList = getDataFromMainActivity()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExploreBinding.inflate(layoutInflater, container, false)
        setRecyclerView(coinList)

        return binding.root
    }

    private fun getDataFromMainActivity(): List<CoinList.Coin> {
        val receivedData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelableArrayList(FROM_MAIN_ACTIVITY, CoinList.Coin::class.java)
        } else {
            requireArguments().getParcelableArrayList<CoinList.Coin>(FROM_MAIN_ACTIVITY)
        }

        val data: List<CoinList.Coin> = if (receivedData.isNullOrEmpty()) {
            listOf()
        } else {
            receivedData.toList()
        }
        return data
    }

    private fun setRecyclerView(list: List<CoinList.Coin>) {
        binding.recyclerExplore.adapter = ExploreRecyclerAdapter(this.requireContext(), coinList)
        binding.recyclerExplore.layoutManager = LinearLayoutManager(requireContext())
    }

}