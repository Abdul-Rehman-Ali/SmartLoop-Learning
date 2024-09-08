package com.smartloopLearn.learning

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.smartloopLearn.learning.databinding.ActivityCreateNewPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CreateNewPassword : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityCreateNewPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNewPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.btnDone.setOnClickListener {
            val user = auth.currentUser
            val password = binding.etPassword.text.toString()
            if (checkAllField()) {
                user?.updatePassword(password)?.addOnCompleteListener {task->
                    if (task.isSuccessful) {

                        Toast.makeText(this, "Update Successfully",
                            Toast.LENGTH_SHORT).show()
                        clearFields()
                        val i = Intent(this, Login::class.java)
                        finish()
                        startActivity(i)
                    } else {
                        Toast.makeText(baseContext, "Password not updated",
                            Toast.LENGTH_SHORT).show()
                    }
                }
                }

//            val i = Intent(this, PasswordChanged::class.java)
//            finish()
//            startActivity(i)
            }
        }

    private fun checkAllField(): Boolean {
//        val email = binding.etEmail.text.toString()

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

private fun clearFields() {
    binding.etPassword.text?.clear()
    binding.etConfirmPassword.text?.clear()

    // Clear any errors that might have been set
    binding.etPassword.error = null
    binding.etConfirmPassword.error = null
}

}