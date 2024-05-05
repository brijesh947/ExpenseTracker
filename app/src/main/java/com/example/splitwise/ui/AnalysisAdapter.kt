package com.example.splitwise.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.R
import com.example.splitwise.data.Data
import com.example.splitwise.data.PieChartData
import com.example.splitwise.databinding.PieChartAnalysisBinding
import com.example.splitwise.ui.holder.AnalysisHolder

class AnalysisAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: ArrayList<Data> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<PieChartAnalysisBinding>(
            LayoutInflater.from(parent.context),
            R.layout.pie_chart_analysis,
            parent,
            false
        )
        return AnalysisHolder(binding, parent.context)
    }

    fun setList(list: ArrayList<Data>) {
        this.list = list
        notifyDataSetChanged()
    }
    fun clearList() {
        list.clear()
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as AnalysisHolder).setData(list[position])
    }
}