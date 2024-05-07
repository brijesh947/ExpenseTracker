package com.example.splitwise.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.R
import com.example.splitwise.data.Data
import com.example.splitwise.data.PieChartData
import com.example.splitwise.databinding.PercentageWiseCategoryAnalysisBinding
import com.example.splitwise.databinding.PieChartAnalysisBinding
import com.example.splitwise.ui.holder.AnalysisHolder
import com.example.splitwise.ui.holder.CategoryWiseAnalysisHolder
import com.example.splitwise.ui.util.PERCENTAGE_WISE_ANALYSIS

class AnalysisAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: ArrayList<Data> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            PERCENTAGE_WISE_ANALYSIS -> {
                val binding = DataBindingUtil.inflate<PercentageWiseCategoryAnalysisBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.percentage_wise_category_analysis,
                    parent,
                    false
                )
                return CategoryWiseAnalysisHolder(binding, parent.context)
            }

            else -> {
                val binding = DataBindingUtil.inflate<PieChartAnalysisBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.pie_chart_analysis,
                    parent,
                    false
                )
                return AnalysisHolder(binding, parent.context)
            }
        }

    }

    fun setList(list: ArrayList<Data>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun clearList() {
        list.clear()
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].getType()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (list[position].getType() == PERCENTAGE_WISE_ANALYSIS) {
            if (position == list.size - 1)
                (holder as CategoryWiseAnalysisHolder).setData(list[position], true)
            else
                (holder as CategoryWiseAnalysisHolder).setData(list[position], false)
        }
        else
            (holder as AnalysisHolder).setData(list[position])
    }
}