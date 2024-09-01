package com.example.splitwise.ui.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.data.Data
import com.example.splitwise.data.SimpleTextData
import com.example.splitwise.databinding.SimpleTextDescriptionLayoutBinding

class SimpleTextHolder(val binding: SimpleTextDescriptionLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    fun setData(data: Data) {

        val simpleTextData = data as SimpleTextData
        if (simpleTextData.addCreateCategory) {
            binding.textDetail.visibility = View.GONE
            binding.addNewCategory.visibility = View.VISIBLE
        } else {
            binding.addNewCategory.visibility = View.GONE
            binding.textDetail.visibility = View.VISIBLE
            binding.textDetail.text = (data as SimpleTextData).name
        }
    }
}