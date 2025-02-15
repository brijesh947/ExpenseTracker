package com.self.expensetracker.splitwise.data

import com.self.expensetracker.splitwise.ui.util.PIE_CHART

data class PieChartData(val listMap: HashMap<String,List<Pair<Long,Double>>>, val total: String) : Data {
    override fun getType(): Int {
        return PIE_CHART
    }
}
