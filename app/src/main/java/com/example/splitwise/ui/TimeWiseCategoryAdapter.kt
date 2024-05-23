package com.example.splitwise.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.R
import com.example.splitwise.data.Data
import com.example.splitwise.databinding.CategoryExpenseTimeWiseLayoutBinding
import com.example.splitwise.ui.holder.TimeWiseCategoryDataHolder

class TimeWiseCategoryAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: ArrayList<Data> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<CategoryExpenseTimeWiseLayoutBinding>(
            LayoutInflater.from(parent.context),
            R.layout.category_expense_time_wise_layout,
            parent,
            false
        )
        return TimeWiseCategoryDataHolder(binding)

    }

    fun setList(newList: ArrayList<Data>) {
        list.clear()
        list.addAll(newList)
        Log.d("fgwjedhasj", "size of the new list is  ${newList.size} ")
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TimeWiseCategoryDataHolder).setData(list[position])
    }
}