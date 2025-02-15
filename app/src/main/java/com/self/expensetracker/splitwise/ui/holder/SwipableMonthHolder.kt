package com.self.expensetracker.splitwise.ui.holder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.self.expensetracker.splitwise.FeaturedMonthDataAdapter
import com.self.expensetracker.splitwise.data.Data
import com.self.expensetracker.splitwise.databinding.SwipableMonthDetailLayoutBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class SwipableMonthHolder(val binding: SwipableMonthDetailLayoutBinding, val context: Context) : RecyclerView.ViewHolder(binding.root) {

    fun setData(data: Data) {
        val list: ArrayList<Data> = ArrayList()
        list.add(data)
        list.add(data)
        val adapter = FeaturedMonthDataAdapter(list)
        binding.featuredMonthRecycler.adapter = adapter
        TabLayoutMediator(
            binding.tabLayout, binding.featuredMonthRecycler
        ) { tab: TabLayout.Tab?, position: Int -> }.attach()

        adapter.notifyDataSetChanged()

    }
}