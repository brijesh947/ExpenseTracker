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
import com.example.splitwise.databinding.GroupDetailLayoutBinding
import com.example.splitwise.databinding.SpendDetailLayoutBinding

class HomeAdapter(private val context: Context, private val application: Application) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: List<Data> = ArrayList()
    private val GROUP_TYPE =1
    private val SHOPPING_TYPE =2

    private var viewType = GROUP_TYPE
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (this.viewType == GROUP_TYPE) {
            val binding = DataBindingUtil.inflate<GroupDetailLayoutBinding>(
                LayoutInflater.from(parent.context),
                R.layout.group_detail_layout,
                parent,
                false
            )
            return GroupDetailHolder(binding)

        }
        val binding = DataBindingUtil.inflate<SpendDetailLayoutBinding>(
            LayoutInflater.from(parent.context),
            R.layout.spend_detail_layout,
            parent,
            false
        )
        return ShoppingDetailHolder(binding)
    }

    fun setList(currentList: List<Data>) {
        list = currentList
        Log.d("BKD", "list's detail is ${list.toList().toString()}: ")
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        Log.d("BKD", "getItemCount: ${list.size}")
        return list.size
    }

    fun setViewType(type: Int) {
        viewType = if (type == 1)
            GROUP_TYPE
        else
            SHOPPING_TYPE
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = list[position]
        if (this.viewType == GROUP_TYPE)
            (holder as GroupDetailHolder).setData(data)
        else
            (holder as ShoppingDetailHolder).setData(data)
    }

    private inner class GroupDetailHolder(val binding: GroupDetailLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(data: Data) {
            val newData = data as GroupDetailData
            binding.groupName.text = data.groupName
            binding.totalExpense.text = data.totalExpense
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