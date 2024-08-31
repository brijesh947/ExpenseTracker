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

        when (categoryDescriptionData.getType()) {
            MOVIE -> {
                binding.groupName.text = "Movie"
                binding.groupLogo.setImageResource(R.drawable.shopping_101)
            }

            HEALTH -> {
                binding.groupName.text = "Health"
                binding.groupLogo.setImageResource(R.drawable.shopping_105)
            }

            RENT -> {
                binding.groupName.text = "Rent"
                binding.groupLogo.setImageResource(R.drawable.shopping_106)
            }

            BEAUTY -> {
                binding.groupName.text = "Beauty"
                binding.groupLogo.setImageResource(R.drawable.shopping_103)
            }

            PETROL_PUMP -> {
                binding.groupName.text = "Petrol"
                binding.groupLogo.setImageResource(R.drawable.shopping_107)
            }

            FOOD -> {
                binding.groupName.text = "Food"
                binding.groupLogo.setImageResource(R.drawable.shopping_104)
            }

            CLOTHING -> {
                binding.groupName.text = "Clothing"
                binding.groupLogo.setImageResource(R.drawable.shopping_102)
            }

            BIKE -> {
                binding.groupName.text = "BIKE"
                binding.groupLogo.setImageResource(R.drawable.shopping_109)
            }

            TRANSPORT -> {
                binding.groupName.text = "Travel"
                binding.groupLogo.setImageResource(R.drawable.shopping_108)
            }

            DONATE -> {
                binding.groupName.text = "Donation"
                binding.groupLogo.setImageResource(R.drawable.shopping_110)
            }

            SPORTS -> {
                binding.groupName.text = "Sports"
                binding.groupLogo.setImageResource(R.drawable.shopping_111)
            }

            MOBILE -> {
                binding.groupName.text = "Mobile"
                binding.groupLogo.setImageResource(R.drawable.shopping_112)
            }

            else -> {
                binding.groupName.text = "Other"
                binding.groupLogo.setImageResource(R.drawable.shopping_113)
            }

        }

        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        binding.threeDots.setOnClickListener {
            if (vibrator.hasVibrator()) {
                vibrator.vibrate(50)
            }
            Toast.makeText(context, "The list of categories is fixed and cannot be changed", Toast.LENGTH_SHORT).show()
        }

    }

}