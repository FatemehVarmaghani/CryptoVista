package com.example.cryptovista.features.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.cryptovista.FAILURE_ON_FETCHING_DATA
import com.example.cryptovista.FROM_MAIN_ACTIVITY
import com.example.cryptovista.NOT_AVAILABLE
import com.example.cryptovista.ONE_DAY
import com.example.cryptovista.ONE_MONTH
import com.example.cryptovista.ONE_WEEK
import com.example.cryptovista.ONE_YEAR
import com.example.cryptovista.R
import com.example.cryptovista.SOMETHING_WENT_WRONG
import com.example.cryptovista.THERE_IS_NO_DATA
import com.example.cryptovista.TWO_MONTHS
import com.example.cryptovista.TWO_WEEKS
import com.example.cryptovista.databinding.ActivityCoinDetailsBinding
import com.example.cryptovista.features.adapters.ChartAdapter
import com.example.cryptovista.network.ApiManager
import com.example.cryptovista.network.model.ChartData
import com.example.cryptovista.network.model.CoinDataForDisplay


class CoinDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCoinDetailsBinding
    private val apiManager = ApiManager()
    private var coinData = CoinDataForDisplay()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideNavigationBar()

        // setting actionbar & back icon
        setSupportActionBar(binding.toolbarDetails)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        getData()
        handleRadioButton()
        handleBackIconClick()
        handleLinkTouch()
    }

    private fun getData() {
        val coinId = getCoinIdFromIntent()

        // fetching data from server
        apiManager.getCoinData(coinId, object : ApiManager.ApiCallback<CoinDataForDisplay> {
            override fun onSuccess(data: CoinDataForDisplay) {
                coinData = data
                setUI()
            }

            override fun onFailure(message: String) {
                Log.v(FAILURE_ON_FETCHING_DATA, message)
                showToast(SOMETHING_WENT_WRONG)
            }

            override fun onNullOrEmpty() {
                showToast(THERE_IS_NO_DATA)
            }
        })
    }

    private fun setUI() {

        binding.toolbarDetails.title = coinData.name

        setChart()
        setStatistics()
        setAbout()

    }

    private fun setChart() {

        // setting textViews
        binding.chartLayout.txtDollarSign.text = "$"
        binding.chartLayout.txtPriceChart.text = handleNull(coinData.currentPrice)
        binding.chartLayout.txtChangeChart.text = handleNull(coinData.priceChange1D) + "%"
        binding.chartLayout.txtChangeItemChart.text = getRelatedIcon(coinData.priceChange1D)

        // setting colors
        val color = getRelatedColor(coinData.priceChange1D)
        binding.chartLayout.txtChangeChart.setTextColor(color)
        binding.chartLayout.txtChangeItemChart.setTextColor(color)
        binding.chartLayout.sparkViewChart.lineColor = color


        // init chart (sparkView)
        setChartView(coinData.id, ONE_DAY)

        // set scrub for chart
        binding.chartLayout.sparkViewChart.setScrubListener {
            if (it != null) {
                Log.v("checking", it.toString())
                val dataPoint: List<Float> = it as List<Float>
                binding.chartLayout.txtPriceChart.text = dataPoint[1].toString()
            } else {
                // show current price when there is no scrubbing
                binding.chartLayout.txtPriceChart.text = handleNull(coinData.currentPrice)
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setStatistics() {

        val algorithm = coinData.algorithm
        if (!algorithm.isNullOrEmpty()) {
            binding.statisticsLayout.txtAlgorithm.text = algorithm
        }

        binding.statisticsLayout.txtMarketCapRank.text = handleNull(coinData.marketCapRank)
        binding.statisticsLayout.txtTodayHigh.text = "$${formatLongNumbers(coinData.high24h)}"
        binding.statisticsLayout.txtTodayLow.text = "$${formatLongNumbers(coinData.low24h)}"
        binding.statisticsLayout.txtTodayChange.text = handleNull(coinData.priceChange1D) + "%"
        binding.statisticsLayout.txtMarketCapDetails.text =
            "$${formatLongNumbers(coinData.marketCap)}"
        binding.statisticsLayout.txtTotalVolume.text = "$${formatLongNumbers(coinData.totalVolume)}"
        binding.statisticsLayout.txtTotalSupply.text = "$${formatLongNumbers(coinData.totalSupply)}"

    }

    private fun setAbout() {

        binding.aboutLayout.txtWebsite.text = handleNull(coinData.website)
        binding.aboutLayout.txtFacebook.text = handleNull(coinData.facebook)
        binding.aboutLayout.txtReddit.text = handleNull(coinData.reddit)
        binding.aboutLayout.txtTwitter.text = handleNull(coinData.twitter)
        binding.aboutLayout.txtGithub.text = handleNull(coinData.githubRepo)
        binding.aboutLayout.txtDescription.text = handleNull(coinData.description)

    }

    private fun setChartView(id: String?, timeDuration: Long) {
        if (!id.isNullOrEmpty()) {

            val now = getCurrentTimeStamp()
            val startTime = now - timeDuration

            apiManager.getCoinChartData(id, startTime, now,
                object : ApiManager.ApiCallback<ChartData> {
                    override fun onSuccess(data: ChartData) {
                        if (!dataIsStraightLine(data.prices)) {
                            val chartAdapter = ChartAdapter(data.prices as List<List<Float>>)
                            // preventing the chart to show a straight line when data points are the same
                            binding.chartLayout.sparkViewChart.adapter = chartAdapter
                            binding.chartLayout.txtErrorChart.visibility = View.GONE
                        } else {
                            binding.chartLayout.txtErrorChart.visibility = View.VISIBLE
                        }
                    }

                    override fun onFailure(message: String) {
                        Log.v(FAILURE_ON_FETCHING_DATA, message)
                        showToast(SOMETHING_WENT_WRONG)
                    }

                    override fun onNullOrEmpty() {
                        showToast(THERE_IS_NO_DATA)
                    }
                })

        }
    }

    private fun dataIsStraightLine(data: List<List<Double>>): Boolean {
        val firstValue = data[0][1]
        return data.all { it[1] == firstValue }
    }

    private fun handleRadioButton() {
        binding.chartLayout.radioGroupChart.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_button_24h -> {
                    changeChartData(ONE_DAY, coinData.priceChange1D)
                }

                R.id.radio_button_1w -> {
                    changeChartData(ONE_WEEK, coinData.priceChange1W)
                }

                R.id.radio_button_2w -> {
                    changeChartData(TWO_WEEKS, coinData.priceChange2W)
                }

                R.id.radio_button_1m -> {
                    changeChartData(ONE_MONTH, coinData.priceChange1M)
                }

                R.id.radio_button_2m -> {
                    changeChartData(TWO_MONTHS, coinData.priceChange2M)
                }

                R.id.radio_button_1y -> {
                    changeChartData(ONE_YEAR, coinData.priceChange1Y)
                }
            }
        }
    }

    private fun changeChartData(period: Long, priceChange: Double?) {
        if (priceChange != null) {
            // clear sparkView
            binding.chartLayout.sparkViewChart.adapter = ChartAdapter(listOf())

            // set sparkView with new data
            setChartView(coinData.id, period)
            binding.chartLayout.sparkViewChart.lineColor = getRelatedColor(priceChange)

            // set priceChange textView
            binding.chartLayout.txtChangeChart.text = priceChange.toString() + "%"
            binding.chartLayout.txtChangeChart.setTextColor(getRelatedColor(priceChange))
            binding.chartLayout.txtChangeItemChart.text = getRelatedIcon(priceChange)
            binding.chartLayout.txtChangeItemChart.setTextColor(getRelatedColor(priceChange))
        }
    }

    private fun getCoinIdFromIntent(): String {
        return intent.getStringExtra(FROM_MAIN_ACTIVITY)!!
    }

    private fun <T> handleNull(data: T?): String {
        return if (data == null) {
            NOT_AVAILABLE
        } else {
            val string = data.toString()
            string.ifEmpty {
                NOT_AVAILABLE
            }
        }
    }

    private fun formatLongNumbers(number: Double?): String {
        return when {
            number == null -> NOT_AVAILABLE
            number in 1000.0..<1000000.0 -> String.format("%.2fK", number / 1000)
            number in 1000000.0..<1000000000.0 -> String.format("%.2fM", number / 1000000)
            number >= 1000000000 -> String.format("%.2fB", number / 1000000000)
            else -> number.toString()
        }
    }

    private fun getRelatedColor(data: Double?): Int {
        val colorCode: Int = when {
            data == null -> {
                ContextCompat.getColor(this, R.color.white)
            }

            data > 0 -> {
                ContextCompat.getColor(this, R.color.green)
            }

            data < 0 -> {
                ContextCompat.getColor(this, R.color.red)
            }

            else -> {
                ContextCompat.getColor(this, R.color.white)
            }
        }
        return colorCode
    }

    private fun getRelatedIcon(data: Double?): String {
        val icon: String = when {
            data == null -> {
                ""
            }

            data > 0 -> {
                "▲"
            }

            data < 0 -> {
                "▼"
            }

            else -> {
                ""
            }
        }
        return icon
    }

    private fun getCurrentTimeStamp(): Long {
        return Calendar.getInstance().timeInMillis / 1000
    }

    private fun handleBackIconClick() {
        binding.toolbarDetails.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun handleLinkTouch() {
        binding.aboutLayout.txtWebsite.setOnClickListener {
            openURL(coinData.website)
        }

        binding.aboutLayout.txtGithub.setOnClickListener {
            openURL(coinData.githubRepo)
        }

        binding.aboutLayout.txtReddit.setOnClickListener {
            openURL(coinData.reddit)
        }

        binding.aboutLayout.txtFacebook.setOnClickListener {
            openURL("https://www.facebook.com/${coinData.facebook}")
        }

        binding.aboutLayout.txtTwitter.setOnClickListener {
            openURL("https://twitter.com/${coinData.twitter}")
        }
    }

    private fun openURL(url: String?) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun AppCompatActivity.hideNavigationBar() {
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