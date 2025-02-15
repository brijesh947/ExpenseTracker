package com.self.expensetracker.splitwise.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.self.expensetracker.splitwise.CategoryFilterListener
import com.self.expensetracker.splitwise.R
import com.self.expensetracker.splitwise.data.Data
import com.self.expensetracker.splitwise.data.ExpenseCategoryData
import com.self.expensetracker.splitwise.databinding.ExpenseCategoryRecyclerItemBinding
import com.self.expensetracker.splitwise.ui.holder.ExpenseCategoryHolder

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

    fun findPreviousCategoryPosition(): Int {
        return list.indexOfFirst { (it as ExpenseCategoryData).isSelected }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ExpenseCategoryHolder).setData(list[position],filterListener,position)

    }
}