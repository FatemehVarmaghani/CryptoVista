package com.example.cryptovista.features.activities

import android.content.Intent
import android.net.Uri
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

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        firstRun()
        handleBottomNavigation()

        binding.mainToolbar.setNavigationOnClickListener {
            //show drawer navigation
            showToast("navigation")
        }

        binding.mainToolbar.setOnMenuItemClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.coingecko.com/"))
            startActivity(intent)
            true
        }
    }

    private fun firstRun() {
        fragmentTransaction(R.id.main_container, HomeFragment())
    }

    private fun fragmentTransaction(id: Int, fragment: Fragment, data: List<Parcelable>? = null) {
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
                    fragmentTransaction(R.id.main_container, ExploreFragment())
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