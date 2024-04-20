package com.example.splitwise

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.splitwise.databinding.ExpensesDetailLayoutBinding

class ExpenseDetailActivity :AppCompatActivity(){
    private lateinit var binding: ExpensesDetailLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ExpensesDetailLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setWindowColor()
        binding.bottomNavigation.itemIconTintList = null
    }

    private fun setWindowColor() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = resources.getColor(R.color.transparent)
    }
}