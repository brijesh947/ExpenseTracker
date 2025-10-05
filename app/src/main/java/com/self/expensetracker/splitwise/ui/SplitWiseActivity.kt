package com.self.expensetracker.splitwise.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updatePadding
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
            supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, LoginFragment()).commit()
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
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.transparent)
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = true
        insetsController.isAppearanceLightNavigationBars = true
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
                        or WindowInsetsCompat.Type.displayCutout()
            )
            v.updatePadding(
                left = bars.left,
                top = bars.top,
                right = bars.right,
                bottom = bars.bottom,
            )
            WindowInsetsCompat.CONSUMED
        }
    }
}

