package com.example.splitwise.ui

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.splitwise.databinding.HomeLayoutBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: HomeLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("Brijesh", "in Home Activity")

    }
}