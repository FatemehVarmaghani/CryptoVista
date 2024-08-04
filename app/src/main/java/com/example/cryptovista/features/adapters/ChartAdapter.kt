package com.example.cryptovista.features.adapters

import android.util.Log
import com.robinhood.spark.SparkAdapter

class ChartAdapter(private val yData: List<List<Float>>): SparkAdapter() {

    override fun getCount(): Int {
        Log.v("dataPoints", yData.toString())
        return yData.size
    }

    override fun getItem(index: Int): Any {
        return yData[index]
    }

    override fun getY(index: Int): Float {
        return yData[index][1]
    }
}