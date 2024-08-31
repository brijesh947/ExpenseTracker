package com.example.splitwise.ui.holder

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.R
import com.example.splitwise.data.Data
import com.example.splitwise.data.PieChartData
import com.example.splitwise.databinding.PieChartAnalysisBinding
import com.example.splitwise.ui.CustomMarkerView
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry


class AnalysisHolder(val binding: PieChartAnalysisBinding, val context: Context) :
    RecyclerView.ViewHolder(binding.root) {

    fun setData(data: Data) {

        try {
            val pieData = data as PieChartData
            binding.pieChart.clear()
            var spending: ArrayList<PieEntry> = ArrayList()

            for ((key, value) in pieData.listMap) {
                var totalSum = 0.0
                for (item in value) {
                    totalSum += item.second
                }
                spending.add(PieEntry(totalSum.toFloat(), key.substringAfter("_")))
            }

            val colors = intArrayOf(
                ContextCompat.getColor(context, R.color.purple_200),
                ContextCompat.getColor(context, R.color.purple_500),
                ContextCompat.getColor(context, R.color.teal_700),
                ContextCompat.getColor(context, R.color.ce_highlight_ac2_light),
                ContextCompat.getColor(context, R.color.ce_highlight_khayi_light),
                ContextCompat.getColor(context, R.color.csk_color),
                ContextCompat.getColor(context, R.color.colorAccent),
                ContextCompat.getColor(context, R.color.upcomingColor),
                ContextCompat.getColor(context, R.color.pausedColor),
                ContextCompat.getColor(context, R.color.commentColor),
                ContextCompat.getColor(context, R.color.ce_score_highlight_light),
                ContextCompat.getColor(context, R.color.card_delete),
                ContextCompat.getColor(context, R.color.liveTabBackColor)
            )



            val pieDataSet = PieDataSet(spending, "")
            pieDataSet.setColors(*colors)
          //  pieDataSet.getColor(0)
            pieDataSet.valueTextColor = context.resources.getColor(R.color.ce_primary_fg_light)
            pieDataSet.valueTextSize = 0f



            val pieDataFinal = PieData(pieDataSet)

            val legend = binding.pieChart.legend
            legend.yEntrySpace = 5f
            legend.xEntrySpace = 10f
            legend.textColor = context.resources.getColor(R.color.primary_txt)
            legend.textSize = 10f
            //legend.isEnabled = false

            val mv = CustomMarkerView(context, R.layout.custom_marker_view_layout)
            binding.pieChart.marker = mv

            binding.pieChart.data = pieDataFinal
            binding.pieChart.setDrawEntryLabels(false)
            binding.pieChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            binding.pieChart.legend.isWordWrapEnabled = true
            binding.pieChart.description.isEnabled = false
            binding.pieChart.centerText = "Spending"
            binding.pieChart.animateXY(500,500)
            binding.pieChart.animate()

        } catch (e: Exception) {
            Log.d("sdfvfr", "setData: ${e.message}")
        }

    }
}