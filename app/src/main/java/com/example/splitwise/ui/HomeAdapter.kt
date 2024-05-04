package com.example.splitwise.ui

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.ExpenseDetailActivity
import com.example.splitwise.R
import com.example.splitwise.ShoppingDetailHolder
import com.example.splitwise.data.Data
import com.example.splitwise.data.GroupDetailData
import com.example.splitwise.databinding.DateLayoutBinding
import com.example.splitwise.databinding.ExpesnseFilterLayoutBinding
import com.example.splitwise.databinding.GroupDetailLayoutBinding
import com.example.splitwise.databinding.MonthFilterLayoutBinding
import com.example.splitwise.databinding.SpendDetailLayoutBinding
import com.example.splitwise.ui.util.GROUP_DATA
import com.example.splitwise.ui.util.MONTH
import com.example.splitwise.ui.util.MOVIE
import com.example.splitwise.ui.util.SHOPPING_DATA
import com.example.splitwise.ui.util.SHOPPING_FILTER_DATA

class HomeAdapter(private val context: Context, private val application: Application) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: List<Data> = ArrayList()

    private var expenseFilterCallback: ExpenseFilterListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {

            GROUP_DATA -> {
                val binding = DataBindingUtil.inflate<GroupDetailLayoutBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.group_detail_layout,
                    parent,
                    false
                )
                return GroupDetailHolder(binding)

            }

            SHOPPING_DATA -> {
                val binding = DataBindingUtil.inflate<SpendDetailLayoutBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.spend_detail_layout,
                    parent,
                    false
                )
                return ShoppingDetailHolder(binding)

            }

            MONTH -> {
                val binding = DataBindingUtil.inflate<MonthFilterLayoutBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.month_filter_layout,
                    parent,
                    false
                )
                return MonthHolder(binding)

            }

            SHOPPING_FILTER_DATA -> {
                val binding = DataBindingUtil.inflate<ExpesnseFilterLayoutBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.expesnse_filter_layout,
                    parent,
                    false
                )
                return ExpenseFilterHolder(binding)

            }

            else -> {
                val binding = DataBindingUtil.inflate<DateLayoutBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.date_layout,
                    parent,
                    false
                )
                return DateHolder(binding)

            }

        }


    }

    fun setFilterListener(callback: ExpenseFilterListener) {
        expenseFilterCallback = callback
    }

    fun setList(currentList: List<Data>) {
        list = currentList
        Log.d("BKD", "list's detail is ${list.toList().toString()}: ")
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].getType()
    }

    override fun getItemCount(): Int {
        Log.d("BKD", "getItemCount: ${list.size}")
        return list.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = list[position]
        if (list[position].getType() == GROUP_DATA) (holder as GroupDetailHolder).setData(data)
        else if (list[position].getType() == SHOPPING_DATA) (holder as ShoppingDetailHolder).setData(data)
        else if (list[position].getType() == MONTH) (holder as MonthHolder).setData(data)
        else if (list[position].getType() == SHOPPING_FILTER_DATA) (holder as ExpenseFilterHolder).setData(data,expenseFilterCallback!!)
        else (holder as DateHolder).setData(data)
    }

    private inner class GroupDetailHolder(val binding: GroupDetailLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(data: Data) {
            val newData = data as GroupDetailData
            binding.groupName.text = data.groupName
            if (data.totalExpense == "till now No Expenses")
                binding.totalExpense.text = data.totalExpense
            else
                binding.totalExpense.text = "This Month's Expenses ${data.totalExpense}"
            binding.root.setOnClickListener {
                val intent = Intent(context,ExpenseDetailActivity::class.java)
                intent.putExtra("name",data.groupName)
                intent.putExtra("expenses",data.totalExpense)
                intent.putExtra("id",data.id)
                context.startActivity(intent)
            }
        }
    }
}