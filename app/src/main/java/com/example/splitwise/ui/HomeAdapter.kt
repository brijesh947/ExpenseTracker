package com.example.splitwise.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.R
import com.example.splitwise.data.GroupDetailData
import com.example.splitwise.databinding.GroupDetailLayoutBinding

class HomeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: List<GroupDetailData> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<GroupDetailLayoutBinding>(
            LayoutInflater.from(parent.context),
            R.layout.group_detail_layout,
            parent,
            false
        )
        return GroupDetailHolder(binding)
    }

    fun setList(currentList: List<GroupDetailData>) {
        list = currentList
        Log.d("BKD", "list's detail is ${list.toList().toString()}: ")
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        Log.d("BKD", "getItemCount: ${list.size}")
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = list[position]
        Log.d("BKD", "data name is ${data.groupName} and position is $position")
        (holder as GroupDetailHolder).setData(data)
    }

    private inner class GroupDetailHolder(val binding: GroupDetailLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setData(data: GroupDetailData) {
            binding.groupName.text = data.groupName
            binding.totalExpense.text = data.totalExpense
        }
    }
}