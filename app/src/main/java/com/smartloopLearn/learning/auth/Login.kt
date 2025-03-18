//package com.smartloopLearn.learning.auth
//
//import android.content.Context
//import android.content.Intent
//import android.content.SharedPreferences
//import android.os.Bundle
//import android.util.Patterns
//import android.widget.Toast
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount
//import com.google.android.gms.auth.api.signin.GoogleSignInClient
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions
//import com.google.android.gms.common.api.ApiException
//import com.smartloopLearn.learning.admin.DashboardAdmin
//import com.smartloopLearn.learning.databinding.ActivityLoginBinding
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.GoogleAuthProvider
//import com.google.firebase.auth.ktx.auth
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.ktx.Firebase
//import com.smartloopLearn.learning.student.view.activities.Home
//import com.smartloopLearn.learning.R
//import java.util.Calendar
//
//class Login : AppCompatActivity() {
//    private lateinit var auth: FirebaseAuth
//    private lateinit var binding: ActivityLoginBinding
//    private lateinit var mGoogleSignInClient: GoogleSignInClient
//    private lateinit var firestore: FirebaseFirestore
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityLoginBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        auth = Firebase.auth
//        // Initialize Firebase Auth
//        auth = FirebaseAuth.getInstance()
//
//        // Initialize Firestore
//        firestore = FirebaseFirestore.getInstance()
//
//        // Configure Google Sign-In
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
//
//        binding.tvForgetPassword.setOnClickListener {
//            val i = Intent(this, ForgetPassword::class.java)
//            startActivity(i)
//            finish()
//        }
//
//        binding.tvSignUp.setOnClickListener {
//            val i = Intent(this, SignUp::class.java)
//            startActivity(i)
//            finish()
//        }
//
//        binding.btnSignin.setOnClickListener {
//
//            val email = binding.etEmail.text.toString()
//            val password = binding.etPassword.text.toString()
//
//            if (checkAllField()) {
//
//                // For Admin Account
//                if (email == "admin@gmail.com" && password == "Csma1039") {
//                    val i = Intent(this, DashboardAdmin::class.java)
//                    startActivity(i)
//                    finish()
//                } else {
//                    // Sign in the user with Firebase Authentication
//                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            Toast.makeText(this, "Successfully Signed In", Toast.LENGTH_SHORT)
//                                .show()
//                            clearFields()
//
//                            // Update the password in Firestore
//                            val userId = auth.currentUser?.uid
//                            userId?.let {
//                                firestore.collection("users").document(it)
//                                    .update("password", password)
//                                    .addOnSuccessListener {
//                                        Toast.makeText(
//                                            this,
//                                            "Password updated",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//
//                                        val pref: SharedPreferences = getSharedPreferences("userLoginInfo", Context.MODE_PRIVATE)
//                                        val editor = pref.edit()
//                                        editor.putBoolean("flag", true) // Set the flag to true
//                                        editor.apply()
//
//
//                                        // Redirect to the Home Activity
//                                        val intent = Intent(this, Home::class.java)
//                                        startActivity(intent)
//                                        this.finish() // Close the current activity if necessary
//                                    }
//                                    .addOnFailureListener { e ->
//                                        Toast.makeText(
//                                            this,
//                                            "Failed to update password in Firestore: ${e.message}",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                    }
//                            }
//                        } else {
//                            Toast.makeText(this, "Email or password incorrect", Toast.LENGTH_SHORT)
//                                .show()
//                        }
//                    }
//                }
//            }
//
//        }
//
//        // Google Sign-In button click
//        binding.googleSignInButton.setOnClickListener {
//            signInWithGoogle()
//        }
//
//    }
//
//    private fun checkAllField(): Boolean {
//        val email = binding.etEmail.text.toString()
//        if (binding.etEmail.text.toString() == "") {
//            binding.etEmail.error = "This is required field"
//            binding.etEmail.requestFocus()
//            return false
//        }
//        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            binding.etEmail.error = "Check email format"
//            binding.etEmail.requestFocus()
//            return false
//        }
//        if (binding.etPassword.text.toString() == "") {
//            binding.etPassword.error = "This is required field"
//            binding.etPassword.requestFocus()
//            return false
//        }
//        if (binding.etPassword.length() < 8) {
//            binding.etPassword.error = "Password should be 8  characters"
//            binding.etPassword.requestFocus()
//            return false
//        }
//        return true
//    }
//
//    private fun clearFields() {
//
//        binding.etEmail.text?.clear()
//        binding.etPassword.text?.clear()
//
//        // Clear any errors that might have been set
//        binding.etEmail.error = null
//        binding.etPassword.error = null
//    }
//
//    private val googleSignInLauncher = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ) { result ->
//        if (result.resultCode == android.app.Activity.RESULT_OK) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
//            try {
//                val account = task.getResult(ApiException::class.java)
//                if (account != null) {
//                    firebaseAuthWithGoogle(account)
//                }
//            } catch (e: ApiException) {
//                Toast.makeText(this, "Google sign-in failed: ${e.message}", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        }
//    }
//
//    private fun signInWithGoogle() {
//        val signInIntent = mGoogleSignInClient.signInIntent
//        googleSignInLauncher.launch(signInIntent)
//    }
//
//    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
//        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
//        auth.signInWithCredential(credential).addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val user = auth.currentUser
//                if (user != null) {
//                    val userId = user.uid
//                    val userEmail = user.email
//                    val userName = user.displayName
//
//                    // Save user info to Firestore with phone and password fields
//                    val userMap = HashMap<String, Any>()
//                    userMap["user_id"] = userId
//                    userMap["email"] = userEmail ?: ""
//                    userMap["name"] = userName ?: ""
//                    userMap["phone"] = "" // Set phone to empty string by default
//                    userMap["password"] = "" // Set password to empty string by default
//                    userMap["date"] = Calendar.getInstance().time.toString()
//
//                    firestore.collection("users").document(userId).set(userMap)
//                        .addOnCompleteListener { databaseTask ->
//                            if (databaseTask.isSuccessful) {
//                                Toast.makeText(
//                                    this,
//                                    "Successfully Signed In with Google",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                                // Redirect to the Home Activity
//                                val intent = Intent(this, Home::class.java)
//                                startActivity(intent)
//                                this.finish()
//                            } else {
//                                Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT)
//                                    .show()
//                            }
//                        }
//                }
//            } else {
//                Toast.makeText(this, "Firebase Authentication failed", Toast.LENGTH_SHORT).show()
//            }
//
////    @Deprecated("Deprecated in Java")
////    override fun onBackPressed() {
////
////        finishAffinity()
////        super.onBackPressed()
////
////
////    }
//
//        }
//    }
//}

package com.smartloopLearn.learning.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.smartloopLearn.learning.admin.DashboardAdmin
import com.smartloopLearn.learning.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.smartloopLearn.learning.student.view.activities.Home
import com.smartloopLearn.learning.R
import java.util.Calendar

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.tvForgetPassword.setOnClickListener {
            startActivity(Intent(this, ForgetPassword::class.java))
            finish()
        }

        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
            finish()
        }

        binding.btnSignin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (checkAllField()) {
                if (email == "admin@gmail.com" && password == "Csma1039") {
                    startActivity(Intent(this, DashboardAdmin::class.java))
                    finish()
                } else {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            if (user != null && user.isEmailVerified) {
                                Toast.makeText(this, "Successfully Signed In", Toast.LENGTH_SHORT).show()
                                saveLoginState()
                                updatePasswordInFirestore(user.uid, password)
                            } else {
                                Toast.makeText(this, "Please verify your email first!", Toast.LENGTH_LONG).show()
                                user?.sendEmailVerification()?.addOnCompleteListener { verificationTask ->
                                    if (verificationTask.isSuccessful) {
                                        Toast.makeText(this, "Verification email sent!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(this, "Failed to send verification email", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(this, "Email or password incorrect", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        binding.googleSignInButton.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun checkAllField(): Boolean {
        val email = binding.etEmail.text.toString()
        if (email.isEmpty()) {
            binding.etEmail.error = "This is a required field"
            binding.etEmail.requestFocus()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Check email format"
            binding.etEmail.requestFocus()
            return false
        }
        if (binding.etPassword.text.toString().isEmpty()) {
            binding.etPassword.error = "This is a required field"
            binding.etPassword.requestFocus()
            return false
        }
        if (binding.etPassword.length() < 8) {
            binding.etPassword.error = "Password should be at least 8 characters"
            binding.etPassword.requestFocus()
            return false
        }
        return true
    }

    private fun clearFields() {
        binding.etEmail.text?.clear()
        binding.etPassword.text?.clear()
        binding.etEmail.error = null
        binding.etPassword.error = null
    }

    private fun saveLoginState() {
        val pref: SharedPreferences = getSharedPreferences("userLoginInfo", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean("flag", true)
        editor.apply()
    }

    private fun updatePasswordInFirestore(userId: String, password: String) {
        firestore.collection("users").document(userId)
            .update("password", password)
            .addOnSuccessListener {
                Toast.makeText(this, "Password updated", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, Home::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to update password in Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    firebaseAuthWithGoogle(account)
                }
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign-in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = mGoogleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                if (user != null) {
                    val userId = user.uid
                    val userEmail = user.email
                    val userName = user.displayName

                    val userMap = hashMapOf(
                        "user_id" to userId,
                        "email" to (userEmail ?: ""),
                        "name" to (userName ?: ""),
                        "phone" to "",
                        "password" to "",
                        "date" to Calendar.getInstance().time.toString()
                    )

                    firestore.collection("users").document(userId).set(userMap)
                        .addOnCompleteListener { databaseTask ->
                            if (databaseTask.isSuccessful) {
                                Toast.makeText(this, "Successfully Signed In with Google", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, Home::class.java))
                                finish()
                            } else {
                                Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            } else {
                Toast.makeText(this, "Firebase Authentication failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
