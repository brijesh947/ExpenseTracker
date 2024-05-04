package com.example.splitwise.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.CategoryFilterListener
import com.example.splitwise.R
import com.example.splitwise.data.Data
import com.example.splitwise.databinding.ExpenseCategoryRecyclerItemBinding
import com.example.splitwise.ui.holder.ExpenseCategoryHolder

class CategoryAdapter(val filterListener: CategoryFilterListener<Int>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: ArrayList<Data> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ExpenseCategoryRecyclerItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.expense_category_recycler_item,
            parent,
            false
        )
        return ExpenseCategoryHolder(binding,parent.context)
    }

    fun setList(list: ArrayList<Data>){
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ExpenseCategoryHolder).setData(list[position],filterListener,position)

    }
}