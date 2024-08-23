package com.example.cryptovista.features.activities

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
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

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        hideNavigationBar()
        setSupportActionBar(binding.mainToolbar)

        //handling bottom navigation
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            handleBottomNavigation(item.itemId)
        }

        //show drawer navigation
        binding.mainToolbar.setNavigationOnClickListener {
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
                    true
                }

                else -> {
                    false
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        firstRun()
    }

    private fun firstRun() {
        handleBottomNavigation(binding.bottomNavigation.selectedItemId)
    }

    private fun fragmentTransaction(id: Int, fragment: Fragment, data: List<Parcelable>? = null) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(id, fragment)
        transaction.commit()
    }

    private fun handleBottomNavigation(selectedId: Int): Boolean {
        return when (selectedId) {
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

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        when {
            binding.bottomNavigation.menu.findItem(R.id.nav_home).isChecked -> {
                binding.mainToolbar.menu.clear()
                menuInflater.inflate(R.menu.menu_toolbar_home, menu)
                return true
            }

            binding.bottomNavigation.menu.findItem(R.id.nav_news).isChecked -> {
                binding.mainToolbar.menu.clear()
                return true
            }

            binding.bottomNavigation.menu.findItem(R.id.nav_explore).isChecked -> {

                //setting icons in toolbar
                binding.mainToolbar.menu.clear()
                menuInflater.inflate(R.menu.menu_toolbar_explore, menu)

                //setting searchView in toolbar
                val searchItem = menu?.findItem(R.id.item_search_explore)
                val searchView = searchItem?.actionView as SearchView

                //searchView style
                val hintTextView =
                    searchView.findViewById<AutoCompleteTextView>(androidx.appcompat.R.id.search_src_text)
                hintTextView.hint = "Type Coin Name"
                hintTextView.setHintTextColor(ContextCompat.getColor(this, R.color.white_shade))
                hintTextView.setTextColor(ContextCompat.getColor(this, R.color.white))

                val closeBtn =
                    searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
                closeBtn.setColorFilter(ContextCompat.getColor(this, R.color.white))

                //handling the search
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        query?.let {
                            val fragment =
                                supportFragmentManager.findFragmentById(R.id.main_container) as ExploreFragment
                            fragment.filterData(it)
                        }
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        newText?.let {
                            val fragment =
                                supportFragmentManager.findFragmentById(R.id.main_container) as ExploreFragment
                            fragment.filterData(it)
                        }
                        return false
                    }
                })

                return true
            }

            binding.bottomNavigation.menu.findItem(R.id.nav_profile).isChecked -> {
                binding.mainToolbar.menu.clear()
                return true
            }

            else -> {
                return false
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun AppCompatActivity.hideNavigationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.let { controller ->
                controller.hide(WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        }
    }

}