package com.example.cryptovista.features.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.cryptovista.EXPLORE_TITLE
import com.example.cryptovista.HOME_TITLE
import com.example.cryptovista.NEWS_TITLE
import com.example.cryptovista.PROFILE_TITLE
import com.example.cryptovista.R
import com.example.cryptovista.databinding.ActivityMainBinding
import com.example.cryptovista.features.fragments.ExploreFragment
import com.example.cryptovista.features.fragments.HomeFragment
import com.example.cryptovista.features.fragments.NewsFragment
import com.example.cryptovista.features.fragments.ProfileFragment

const val FROM_MAIN_ACTIVITY = "from_main_activity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        firstRun()
        handleBottomNavigation()
        setSupportActionBar(binding.mainToolbar)

        binding.mainToolbar.setNavigationOnClickListener {
            //show drawer navigation
            showToast("navigation")
        }

        binding.mainToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.item_more_home, R.id.item_more_explore -> {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.coingecko.com/"))
                    startActivity(intent)
                    true
                }

                R.id.item_search_explore -> {
                    showToast("search:)")
                    true
                }

                else -> {
                    false
                }
            }
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
                    binding.mainToolbar.title = HOME_TITLE
                    fragmentTransaction(R.id.main_container, HomeFragment())
                    true
                }

                R.id.nav_news -> {
                    binding.mainToolbar.title = NEWS_TITLE
                    fragmentTransaction(R.id.main_container, NewsFragment())
                    true
                }

                R.id.nav_explore -> {
                    binding.mainToolbar.title = EXPLORE_TITLE
                    fragmentTransaction(R.id.main_container, ExploreFragment())
                    true
                }

                R.id.nav_profile -> {
                    binding.mainToolbar.title = PROFILE_TITLE
                    fragmentTransaction(R.id.main_container, ProfileFragment())
                    true
                }

                else -> {
                    false
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        when {
            binding.bottomNavigation.menu.findItem(R.id.nav_home).isChecked -> {
                binding.mainToolbar.menu.clear()
                menuInflater.inflate(R.menu.menu_toolbar_home, menu)
            }

            binding.bottomNavigation.menu.findItem(R.id.nav_news).isChecked -> {
                binding.mainToolbar.menu.clear()
            }

            binding.bottomNavigation.menu.findItem(R.id.nav_explore).isChecked -> {
                binding.mainToolbar.menu.clear()
                menuInflater.inflate(R.menu.menu_toolbar_explore, menu)
            }

            binding.bottomNavigation.menu.findItem(R.id.nav_profile).isChecked -> {
                binding.mainToolbar.menu.clear()
            }

            else -> {}
        }

        return true
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}