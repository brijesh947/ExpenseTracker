package com.self.expensetracker.splitwise.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.self.expensetracker.splitwise.R
import com.self.expensetracker.splitwise.databinding.MainLayoutBinding
import com.self.expensetracker.splitwise.ui.fragment.login.LoginFragment
import com.self.expensetracker.splitwise.ui.fragment.login.SignUpFragment
import com.google.firebase.auth.FirebaseAuth

class SplitWiseActivity : AppCompatActivity() {
    private lateinit var binding : MainLayoutBinding
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setWindowColor()
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            supportFragmentManager.beginTransaction().replace(
                binding.fragmentContainer.id, LoginFragment()
            ).commit()
        } else {
            openHomeActivity()
        }

    }

    private fun openHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    fun showSignUpFragment() {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, SignUpFragment())
            .addToBackStack(null)
            .commit()
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun setWindowColor() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        window.statusBarColor = resources.getColor(R.color.transparent)
    }
}

