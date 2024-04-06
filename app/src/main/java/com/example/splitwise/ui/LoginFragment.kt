package com.example.splitwise.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.splitwise.databinding.SignInLayoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {
    private lateinit var binding: SignInLayoutBinding
    private lateinit var auth: FirebaseAuth
    private var inputEmail: String = ""
    private var inputPassword: String = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = SignInLayoutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        binding.signUpButton.setOnClickListener {
            (requireActivity() as SplitWiseActivity).showSignUpFragment()
        }
        inputEmail = binding.email.text.toString().trim()
        inputPassword = binding.password.text.toString().trim()
        binding.sign.setOnClickListener {
            if (verifyInput()) {
                binding.progressBar.visibility = View.VISIBLE
                inputEmail = binding.email.text.toString().trim()
                inputPassword = binding.password.text.toString().trim()
                auth.signInWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener {
                    binding.progressBar.visibility = View.GONE
                    if(it.isSuccessful){
                       Log.d("Brijesh", "Sign in completed")
                       val intent = Intent(requireActivity(),HomeActivity::class.java)
                       requireContext().startActivity(intent)
                   }
                }.addOnFailureListener {
                    Log.d("Brijesh", "Sign in Failed due to $it")
                }

            }
        }
    }

    private fun verifyInput(): Boolean {
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

    override fun onResume() {
        super.onResume()
    }
}