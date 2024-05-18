package com.example.splitwise.ui.holder

import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.data.Data
import com.example.splitwise.data.SimpleTextData
import com.example.splitwise.databinding.SimpleTextDescriptionLayoutBinding

class SimpleTextHolder(val binding: SimpleTextDescriptionLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    fun setData(data: Data) {
        binding.textDetail.text = (data as SimpleTextData).name
    }
}