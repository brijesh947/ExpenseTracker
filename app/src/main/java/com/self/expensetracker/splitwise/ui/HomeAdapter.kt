package com.self.expensetracker.splitwise.ui

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.self.expensetracker.splitwise.R
import com.self.expensetracker.splitwise.UpdateRecordsListener
import com.self.expensetracker.splitwise.ui.holder.ShoppingDetailHolder
import com.self.expensetracker.splitwise.data.Data
import com.self.expensetracker.splitwise.data.GroupDetailData
import com.self.expensetracker.splitwise.databinding.DateLayoutBinding
import com.self.expensetracker.splitwise.databinding.ExpesnseFilterLayoutBinding
import com.self.expensetracker.splitwise.databinding.GroupDetailLayoutBinding
import com.self.expensetracker.splitwise.databinding.RecentTransactionLayoutBinding
import com.self.expensetracker.splitwise.databinding.SpendDetailLayoutBinding
import com.self.expensetracker.splitwise.databinding.SwipableMonthDetailLayoutBinding
import com.self.expensetracker.splitwise.ui.holder.DateHolder
import com.self.expensetracker.splitwise.ui.holder.ExpenseFilterHolder
import com.self.expensetracker.splitwise.ui.holder.RecentTransactionHolder
import com.self.expensetracker.splitwise.ui.holder.SwipableMonthHolder
import com.self.expensetracker.splitwise.ui.util.GROUP_DATA
import com.self.expensetracker.splitwise.ui.util.RECENT_TRANSACTION
import com.self.expensetracker.splitwise.ui.util.SHOPPING_DATA
import com.self.expensetracker.splitwise.ui.util.SHOPPING_FILTER_DATA
import com.self.expensetracker.splitwise.ui.util.TOTAL_SPENDING_CARD
import com.self.expensetracker.splitwise.ui.util.showRupeeString

class HomeAdapter(private val context: Context, private val application: Application) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: List<Data> = ArrayList()

    private var expenseFilterCallback: ExpenseFilterListener? = null

    lateinit var updateRecordsListener: UpdateRecordsListener


    fun setListener(listener: UpdateRecordsListener){
        updateRecordsListener = listener
    }
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
                return ShoppingDetailHolder(binding,updateRecordsListener,parent.context)

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

            SHOPPING_FILTER_DATA -> {
                val binding = DataBindingUtil.inflate<ExpesnseFilterLayoutBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.expesnse_filter_layout,
                    parent,
                    false
                )
                return ExpenseFilterHolder(binding)

            }

            TOTAL_SPENDING_CARD -> {
                val binding = DataBindingUtil.inflate<SwipableMonthDetailLayoutBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.swipable_month_detail_layout,
                    parent,
                    false
                )
                return SwipableMonthHolder(binding,parent.context)
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
        else if (list[position].getType() == SHOPPING_DATA) (holder as ShoppingDetailHolder).setData(data, position == list.size - 1)
        else if (list[position].getType() == RECENT_TRANSACTION) (holder as RecentTransactionHolder).setData(data)
        else if (list[position].getType() == TOTAL_SPENDING_CARD) (holder as SwipableMonthHolder).setData(data)
        else if (list[position].getType() == SHOPPING_FILTER_DATA) (holder as ExpenseFilterHolder).setData(data,expenseFilterCallback!!)
        else (holder as DateHolder).setData(data)
    }

    private inner class GroupDetailHolder(val binding: GroupDetailLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(data: Data) {
            val newData = data as GroupDetailData
            binding.groupName.text = data.groupName

            when (newData.groupType) {
                "Flatmates" -> {
                    binding.groupLogo.setImageResource(R.drawable.flatemates)
                }

                "Home" -> {
                    binding.groupLogo.setImageResource(R.drawable.shopping_106)
                }

                "Couple" -> {
                    binding.groupLogo.setImageResource(R.drawable.couple_group_logo)
                }

                else -> {
                    binding.groupLogo.setImageResource(R.drawable.other_group_logo)
                }

            }
            if (data.totalExpense == "Add Expense to display here.")
                binding.totalExpense.text = data.totalExpense
            else
                binding.totalExpense.text = "Total expenses: " + showRupeeString(data.totalExpense)
            binding.root.setOnClickListener {
                val intent = Intent(context, ExpenseDetailActivity::class.java)
                intent.putExtra("name",data.groupName)
                intent.putExtra("expenses",data.totalExpense)
                intent.putExtra("id",data.id)
                context.startActivity(intent)
            }
        }
    }
}