package com.self.expensetracker.splitwise

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.self.expensetracker.splitwise.data.Data
import com.self.expensetracker.splitwise.databinding.MonthWiseBalanceCardviewBinding
import com.self.expensetracker.splitwise.ui.holder.MonthWiseProgressHolder

class FeaturedMonthDataAdapter(val list:ArrayList<Data>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<MonthWiseBalanceCardviewBinding>(
            LayoutInflater.from(parent.context),
            R.layout.month_wise_balance_cardview,
            parent,
            false
        )
        return MonthWiseProgressHolder(binding,parent.context)
    }



    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MonthWiseProgressHolder).setData(list[position], position)
    }
}