package com.example.splitwise.ui

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.splitwise.R
import com.example.splitwise.databinding.HomeLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: HomeLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setWindowColor()
        Log.d("Brijesh", "in Home Activity")
        binding.createGroup.setOnClickListener {
            openCreateGroupDialog()
        }

    }

    private fun openCreateGroupDialog() {
        val dialogView = layoutInflater.inflate(R.layout.create_group, null)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(dialogView)
        dialog.show()
    }

    private fun setWindowColor() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = resources.getColor(R.color.transparent)
    }
}