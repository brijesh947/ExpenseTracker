package com.self.expensetracker.splitwise.ui.holder

import android.annotation.SuppressLint
import android.content.Context
import android.os.Vibrator
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.self.expensetracker.splitwise.data.CategoryDescriptionData
import com.self.expensetracker.splitwise.data.Data
import com.self.expensetracker.splitwise.databinding.CategoryFragmentItemLayoutBinding
import com.self.expensetracker.splitwise.ui.util.hide
import com.self.expensetracker.splitwise.ui.util.show

class CategoryDescriptionHolder(val binding: CategoryFragmentItemLayoutBinding,val context:Context) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("ServiceCast")
    fun setData(data: Data ,isSecondLast :Boolean) {

        val categoryDescriptionData = data as CategoryDescriptionData

        if (isSecondLast)
            binding.seprator.hide()
        else
            binding.seprator.show()

        val drawableId = context.resources.getIdentifier(
            "shopping_${categoryDescriptionData.categoryDestype}",
            "drawable",
            context.packageName
        )

        binding.groupName.text = categoryDescriptionData.categoryName
        binding.groupLogo.setImageResource(drawableId)



        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        binding.threeDots.setOnClickListener {
            if (vibrator.hasVibrator()) {
                vibrator.vibrate(50)
            }
            Toast.makeText(context, "The list of categories is fixed and cannot be changed", Toast.LENGTH_SHORT).show()
        }

    }

}