package com.example.splitwise.ui

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.splitwise.databinding.SignUpLayoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpFragment : Fragment() {

    private lateinit var binding: SignUpLayoutBinding
    private lateinit var auth: FirebaseAuth
    private var inputEmail: String = ""
    private var inputPassword: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        super.onCreateView(inflater, container, savedInstanceState)
        binding = SignUpLayoutBinding.inflate(layoutInflater)
        auth = Firebase.auth

        binding.sign.setOnClickListener {
            if (verifyInput() && auth.currentUser!=null) {
                inputEmail = binding.email.text.toString().trim()
                inputPassword = binding.password.text.toString().trim()
                Log.d("Brijesh", "inputEmail is  $inputEmail and input pasword $inputPassword")
                auth.createUserWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d("Brijesh", "login is succesFull")
                    }
                }.addOnFailureListener {
                    Log.d("Brijesh", "Authentication Failed Reason is : ${it}")
                }

            }
        }
        return binding.root
    }

    private fun verifyInput(): Boolean {
        if (binding.name.text.isEmpty()) {
            binding.name.error = "Name can't be Empty"
            binding.name.requestFocus()
            return false
        }

        if (binding.password.text.length <= 6) {
            binding.password.error = "Password's length must be greater than 6"
            binding.password.requestFocus()
            return false
        }
        val email = binding.email.text.toString().trim()

        if (email.isEmpty()) {
            binding.email.error = "Email can't be empty"
            binding.email.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.email.error = "Please enter a valid Email"
            binding.email.requestFocus()
            return false
        }

        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
    }
}