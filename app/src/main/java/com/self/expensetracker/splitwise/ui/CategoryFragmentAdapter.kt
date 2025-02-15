package com.self.expensetracker.splitwise.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.self.expensetracker.splitwise.R
import com.self.expensetracker.splitwise.data.Data
import com.self.expensetracker.splitwise.databinding.CategoryFragmentItemLayoutBinding
import com.self.expensetracker.splitwise.databinding.RecentTransactionLayoutBinding
import com.self.expensetracker.splitwise.databinding.SimpleTextDescriptionLayoutBinding
import com.self.expensetracker.splitwise.ui.holder.CategoryDescriptionHolder
import com.self.expensetracker.splitwise.ui.holder.RecentTransactionHolder
import com.self.expensetracker.splitwise.ui.holder.SimpleTextHolder
import com.self.expensetracker.splitwise.ui.util.CategoryCreator
import com.self.expensetracker.splitwise.ui.util.RECENT_TRANSACTION
import com.self.expensetracker.splitwise.ui.util.SIMPLE_TEXT

class CategoryFragmentAdapter(val listener:CategoryCreator) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var list:List<Data> = ArrayList()


    fun setList(list: List<Data>) {
        this.list = list
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            SIMPLE_TEXT -> {
                val binding = DataBindingUtil.inflate<SimpleTextDescriptionLayoutBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.simple_text_description_layout,
                    parent,
                    false
                )
                return SimpleTextHolder(binding)
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

            else -> {
                val binding = DataBindingUtil.inflate<CategoryFragmentItemLayoutBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.category_fragment_item_layout,
                    parent,
                    false
                )
                return CategoryDescriptionHolder(binding, parent.context)
            }
        }

    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun getItemViewType(position: Int): Int {
      return list[position].getType()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (list[position].getType()) {

            SIMPLE_TEXT -> {
                (holder as SimpleTextHolder).setData(list[position])
                holder.binding.root.setOnClickListener {
                    listener.createCategory()
                }
            }

            RECENT_TRANSACTION -> {
                (holder as RecentTransactionHolder).setData(list[position])
            }

            else -> {
                val lastPosition = if (list.last().getType() == SIMPLE_TEXT)
                    list.size - 2
                 else
                    list.size - 1
                (holder as CategoryDescriptionHolder).setData(data = list[position] ,position == lastPosition)
            }
        }
    }
}