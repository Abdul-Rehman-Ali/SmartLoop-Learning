package com.smartloopLearn.learning.student.Utils

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.smartloopLearn.learning.databinding.FragmentProfileBinding

object UserSharedPref {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private const val sharedPrefFile = "smartloopLearnPrefs"
    private const val sharedPrefNameKey = "user_name"
    private const val sharedPrefEmailKey = "user_email"
    private const val sharedPrefPhoneKey = "user_phone"

    fun loadUserData(context: Context) {
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            firestore.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val name = document.getString("name")
                        val email = document.getString("email")
                        val phone = document.getString("phone")

                        val sharedPreferences = context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString(sharedPrefNameKey, name)
                        editor.putString(sharedPrefEmailKey, email)
                        editor.putString(sharedPrefPhoneKey, phone)
                        editor.apply()

                    } else {
                        Toast.makeText(context, "No user data found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "Failed to fetch user data: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun updateUserData(context: Context, name: String, email: String, phone: String) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Update Firestore user data
            val userMap = mapOf(
                "name" to name,
                "email" to email,
                "phone" to phone
            )
            firestore.collection("users").document(currentUser.uid).update(userMap)
                .addOnSuccessListener {
                    val sharedPreferences = context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString(sharedPrefNameKey, name)
                    editor.putString(sharedPrefEmailKey, email)
                    editor.putString(sharedPrefPhoneKey, phone)
                    editor.apply()

                    Toast.makeText(context, "User data updated successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "Failed to update user data: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
        }
    }


    fun loadUserDetails(context: Context?, binding: FragmentProfileBinding) {
        val sharedPreferences = context?.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val userName = sharedPreferences?.getString(sharedPrefNameKey, "N/A")
        val userEmail = sharedPreferences?.getString(sharedPrefEmailKey, "N/A")
        val userPhone = sharedPreferences?.getString(sharedPrefPhoneKey, "N/A")

        binding.profileUsername.text = userName
        binding.profileEmail.text = userEmail
        binding.profilePhone.text = userPhone
    }

    fun clearUserData(context: Context) {
        val sharedPreferences = context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()  // Clears all data in SharedPreferences
        editor.apply()
        Toast.makeText(context, "Log Out Successfully", Toast.LENGTH_SHORT).show()
    }
}
