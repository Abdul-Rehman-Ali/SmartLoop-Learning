package com.smartloopLearn.learning.admin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.smartloopLearn.learning.databinding.ActivityPdfListAdminBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PdfListAdminActivity : AppCompatActivity() {

    private companion object {
        const val TAG = "PDF_LIST_ADMIN_TAG"
    }

    private lateinit var binding: ActivityPdfListAdminBinding
    private  var categoryId = ""
    private  var categorey = ""

    private lateinit var pdfArrayList: ArrayList<ModelPdf>
    private lateinit var adapterPdfAdmin: AdapterPdfAdmin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityPdfListAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        categoryId = intent.getStringExtra("categoryId")!!
        categorey = intent.getStringExtra("category")!!

        binding.tvSubtitle.text = categorey

        loadPdfList()

        binding.etSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                try {
                    adapterPdfAdmin.filter!!.filter(s)
                }
                catch (e: Exception){
                    Log.d(TAG, "onTextChanged: ${e.message}")
                }
            }
            override fun afterTextChanged(s: Editable?) {

            }
        })
    }
    private fun loadPdfList() {
        pdfArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Book")
        ref.orderByChild("categoryId").equalTo(categoryId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    pdfArrayList.clear()
                    for (ds in snapshot.children) {
                        try {
                            val model = ds.getValue(ModelPdf::class.java)
                            if (model != null) {
                                pdfArrayList.add(model)
                                Log.d(TAG, "onDataChange: Added ${model.title} with categoryId ${model.categoryId}")
                            } else {
                                Log.d(TAG, "onDataChange: Model is null")
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "onDataChange: Error parsing data: ${ds.value}", e)
                        }
                    }
                    adapterPdfAdmin = AdapterPdfAdmin(this@PdfListAdminActivity, pdfArrayList)
                    binding.rvBooks.adapter = adapterPdfAdmin
                    Log.d(TAG, "onDataChange: Adapter set with item count: ${adapterPdfAdmin.itemCount}")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "onCancelled: Database error: ${error.message}")
                }
            })
    }

}
