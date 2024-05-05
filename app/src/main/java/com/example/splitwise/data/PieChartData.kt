package com.example.splitwise.data

import com.example.splitwise.ui.util.PIE_CHART

data class PieChartData(val listMap: HashMap<String,Double>, val total: String) : Data {
    override fun getType(): Int {
        return PIE_CHART
    }
}
