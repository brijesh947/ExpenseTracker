package com.example.splitwise.ui.holder

import android.annotation.SuppressLint
import android.content.Context
import android.os.Vibrator
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.R
import com.example.splitwise.data.CategoryDescriptionData
import com.example.splitwise.data.Data
import com.example.splitwise.databinding.CategoryFragmentItemLayoutBinding
import com.example.splitwise.ui.util.BEAUTY
import com.example.splitwise.ui.util.BIKE
import com.example.splitwise.ui.util.CLOTHING
import com.example.splitwise.ui.util.DONATE
import com.example.splitwise.ui.util.FOOD
import com.example.splitwise.ui.util.HEALTH
import com.example.splitwise.ui.util.MOBILE
import com.example.splitwise.ui.util.MOVIE
import com.example.splitwise.ui.util.PETROL_PUMP
import com.example.splitwise.ui.util.RENT
import com.example.splitwise.ui.util.SPORTS
import com.example.splitwise.ui.util.TRANSPORT
import com.example.splitwise.ui.util.hide
import com.example.splitwise.ui.util.show

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