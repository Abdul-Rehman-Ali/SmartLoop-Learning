package com.smartloopLearn.learning.admin

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.smartloopLearn.learning.databinding.ActivityCategoryAddBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CategoryAddActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityCategoryAddBinding
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCategoryAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        // Initialize the progress dialog
        progressDialog = ProgressDialog(this).apply {
            setTitle("Please Wait")
            setCanceledOnTouchOutside(false)
        }
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnSubmit.setOnClickListener {
            validateData()
        }
    }
    private var category = ""
    private fun validateData(){
        category = binding.etCategory.text.toString().trim()

        if (category.isBlank()){
            Toast.makeText(this,"Please enter category...", Toast.LENGTH_SHORT).show()
        } else {
            addCategoryFirebase()
        }
    }

    private fun addCategoryFirebase(){
        progressDialog.show()

        val timestamp = System.currentTimeMillis()

        val hashMap = HashMap<String, Any>()
        hashMap["id"] = "$timestamp"
        hashMap["category"] = category
        hashMap["timestamp"] = timestamp
        hashMap["uid"] = "1"

        val ref = FirebaseDatabase.getInstance().getReference("Category")
        ref.child("$timestamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this,"Added Successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {e ->
                progressDialog.dismiss()
                Toast.makeText(this,"Failed to add due to ${e.message}", Toast.LENGTH_SHORT).show()

            }
    }

}
