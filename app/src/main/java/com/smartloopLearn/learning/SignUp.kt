package com.smartloopLearn.learning

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.smartloopLearn.learning.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.*

class SignUp : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()

        binding.btnSignUp.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (checkAllField()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            user?.let {
                                // Save user data to Firestore
                                val userData = hashMapOf(
                                    "name" to binding.etUsername.text.toString(),
                                    "email" to email,
                                    "phone" to binding.etPhone.text.toString(),
                                    "password" to password,
                                    "user_id" to user.uid,
                                    "date" to Calendar.getInstance().time.toString()
                                )
                                firestore.collection("users").document(user.uid)
                                    .set(userData)
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            this, "Successfully Account Created",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        clearFields()
                                        val i = Intent(this, Login::class.java)
                                        startActivity(i)
                                        finish()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(
                                            this, "Error: $e", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        } else {
                            Toast.makeText(
                                baseContext, "Authentication failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }

    // This part of code is shown my validation of sign up page
    private fun checkAllField(): Boolean {
        val email = binding.etEmail.text.toString()
        if(binding.etUsername.text.toString() == ""){
            binding.etUsername.error = "This is required field"
            binding.etUsername.requestFocus()
            return false
        }
        if(binding.etEmail.text.toString() == ""){
            binding.etEmail.error = "This is required field"
            binding.etEmail.requestFocus()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.etEmail.error = "Check email format"
            binding.etEmail.requestFocus()
            return false
        }
        if (binding.etPhone.text.toString() == ""){
            binding.etPhone.error = "This is required field"
            binding.etPhone.requestFocus()
            return false
        }
        if ((binding.etPhone.length() < 11) ){
            binding.etPhone.error = "Enter correct  number"
            binding.etPhone.requestFocus()
            return false
        }
        if ((binding.etPhone.length() > 11) ){
            binding.etPhone.error = "Enter correct  number"
            binding.etPhone.requestFocus()
            return false
        }
        if (binding.etPassword.text.toString() == ""){
            binding.etPassword.error = "This is required field"
            binding.etPassword.requestFocus()
            return false
        }
        if (binding.etPassword.length() < 8){
            binding.etPassword.error = "Password should be 8  characters"
            binding.etPassword.requestFocus()
            return false
        }
        if (binding.etConfirmPassword.text.toString() == ""){
            binding.etConfirmPassword.error = "This is required field"
            binding.etConfirmPassword.requestFocus()
            return false
        }
        if (binding.etPassword.text.toString() != binding.etConfirmPassword.text.toString()){
            binding.etConfirmPassword.error = "Password do not match"
            binding.etConfirmPassword.requestFocus()
            return false
        }

        return true
    }

    // This is used to clear all the data that users entered in the input fields
    private fun clearFields() {
        binding.etUsername.text?.clear()
        binding.etEmail.text?.clear()
        binding.etPhone.text?.clear()
        binding.etPassword.text?.clear()
        binding.etConfirmPassword.text?.clear()

        // Clear any errors that come in the fields
        binding.etUsername.error = null
        binding.etEmail.error = null
        binding.etPhone.error = null
        binding.etPassword.error = null
        binding.etConfirmPassword.error = null
    }
}
