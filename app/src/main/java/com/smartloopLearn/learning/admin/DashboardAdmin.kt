package com.smartloopLearn.learning.admin

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.smartloopLearn.learning.databinding.ActivityDashboardAdminBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception

class DashboardAdmin : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityDashboardAdminBinding
    private lateinit var categoreyArrayList: ArrayList<ModelCategorey>
    private lateinit var adapterCategory: AdapterCategory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDashboardAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        loadCategories()

        // search
        binding.etSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    adapterCategory.filter.filter(s)
                } catch (e : Exception) {

                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        // Ensure the button ID matches the one in your XML layout file
        binding.btnAddCategory.setOnClickListener {
            val intent = Intent(this, CategoryAddActivity::class.java)
            startActivity(intent)
        }

        binding.addPdfFile.setOnClickListener {
            val intent = Intent(this, PdfAddActivity::class.java)
            startActivity(intent)
        }
    }
    private fun loadCategories(){
        categoreyArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Category")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                categoreyArrayList.clear()
                for (ds in snapshot.children){
                    val model = ds.getValue(ModelCategorey::class.java)
                    categoreyArrayList.add(model!!)
                }
                adapterCategory = AdapterCategory(this@DashboardAdmin, categoreyArrayList)
                binding.rvCategorey.adapter = adapterCategory
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

    }
}
