package com.example.cryptovista.features.activities

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.cryptovista.R
import com.example.cryptovista.databinding.ActivityMainBinding
import com.example.cryptovista.features.fragments.ExploreFragment
import com.example.cryptovista.features.fragments.HomeFragment
import com.example.cryptovista.features.fragments.NewsFragment
import com.example.cryptovista.features.fragments.ProfileFragment
import com.example.cryptovista.network.ApiManager
import com.example.cryptovista.network.model.CoinList

const val FROM_MAIN_ACTIVITY = "from_main_activity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var coinList: List<CoinList.Coin> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.mainToolbar)

        getData()
        firstRun()
        handleBottomNavigation()
    }

    private fun getData() {
        //call get functions based on bottom navigation state with when
        getCoinList()
    }

    private fun getCoinList() {
        val apiManager = ApiManager()

        apiManager.getCoinList(object : ApiManager.ApiCallback {
            override fun onSuccess(data: CoinList) {
                coinList = data
                Log.v("castingCoinList", coinList.toString())
            }

            override fun onFailure(message: String) {
                showToast(message)
            }

            override fun onNullOrEmpty() {
                showToast("There is no data!")
            }

        })
    }

    private fun firstRun() {
        fragmentTransaction(R.id.main_container, HomeFragment())
    }

    private fun fragmentTransaction(id: Int, fragment: Fragment, data: List<Parcelable>? = null) {
        if (data != null) {
            val bundle = Bundle()
            bundle.putParcelableArrayList(FROM_MAIN_ACTIVITY, data as ArrayList<CoinList.Coin>)

            fragment.arguments = bundle
        }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(id, fragment)
        transaction.commit()
    }

    private fun handleBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    fragmentTransaction(R.id.main_container, HomeFragment())
                    true
                }

                R.id.nav_news -> {
                    fragmentTransaction(R.id.main_container, NewsFragment())
                    true
                }

                R.id.nav_explore -> {
                    fragmentTransaction(R.id.main_container, ExploreFragment(), coinList)
                    true
                }

                R.id.nav_profile -> {
                    fragmentTransaction(R.id.main_container, ProfileFragment())
                    true
                }

                else -> {
                    false
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}