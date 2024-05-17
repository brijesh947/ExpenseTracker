package com.example.splitwise.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.BudgetBuilder
import com.example.splitwise.R
import com.example.splitwise.data.Data
import com.example.splitwise.databinding.CategoryWiseBudgetLayoutBinding
import com.example.splitwise.databinding.PercentageWiseCategoryAnalysisBinding
import com.example.splitwise.databinding.PieChartAnalysisBinding
import com.example.splitwise.databinding.RecentTransactionLayoutBinding
import com.example.splitwise.ui.holder.AnalysisHolder
import com.example.splitwise.ui.holder.CWBudgetHolder
import com.example.splitwise.ui.holder.CategoryWiseAnalysisHolder
import com.example.splitwise.ui.holder.RecentTransactionHolder
import com.example.splitwise.ui.util.CURR_BUDGET
import com.example.splitwise.ui.util.PERCENTAGE_WISE_ANALYSIS
import com.example.splitwise.ui.util.RECENT_TRANSACTION

class AnalysisAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: ArrayList<Data> = ArrayList()

    private lateinit var listener : BudgetBuilder


    fun setListener(listener: BudgetBuilder) {
        this.listener = listener
    }

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

            RECENT_TRANSACTION -> {
                val binding = DataBindingUtil.inflate<RecentTransactionLayoutBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.recent_transaction_layout,
                    parent,
                    false
                )
                return RecentTransactionHolder(binding)

            }

            CURR_BUDGET -> {
                val binding = DataBindingUtil.inflate<CategoryWiseBudgetLayoutBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.category_wise_budget_layout,
                    parent,
                    false
                )
                return CWBudgetHolder(binding, parent.context,listener)

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
        val isLastPosition = position == list.size - 1

        if (list[position].getType() == PERCENTAGE_WISE_ANALYSIS)
            (holder as CategoryWiseAnalysisHolder).setData(list[position], isLastPosition)

        else if (list[position].getType() == RECENT_TRANSACTION)
            (holder as RecentTransactionHolder).setData(list[position])

        else if (list[position].getType() == CURR_BUDGET)
            (holder as CWBudgetHolder).setData(list[position], isLastPosition,position)

        else
            (holder as AnalysisHolder).setData(list[position])
    }
}