package com.smartloopLearn.learning

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.smartloopLearn.learning.admin.Constants
import com.smartloopLearn.learning.databinding.ActivityPdfViewBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class PdfViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPdfViewBinding

    var bookId = ""
    private companion object {
        const val  TAG = "PDF_VIEW_TAG"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize view binding
        binding = ActivityPdfViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookId = intent.getStringExtra("bookId")!!

        loadBookDetail()

    }

    private fun loadBookDetail() {
        Log.d(TAG, "loadBookDetail: Get PDF Url from DB...")

        val ref = FirebaseDatabase.getInstance().getReference("Book")
        ref.child(bookId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val pdfUrl = snapshot.child("url").value
                    Log.d(TAG, "onDataChange: $pdfUrl")

                    loadBookFromUrl("$pdfUrl")
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

    }

    private fun loadBookFromUrl(pdfURL: String) {
        Log.d(TAG, "loadBookFromUrl: GetPdf from Firebase storage through URL...")
        val ref = FirebaseStorage.getInstance().getReferenceFromUrl(pdfURL)
        ref.getBytes(Constants.Max_Byte_PDF)
            .addOnSuccessListener {bytes ->

                Log.d(TAG, "loadBookFromUrl: Pdf git from URL")
                binding.pdfview.fromBytes(bytes)
                    .onPageChange{page, pageCount ->
                    val currentpage = page
                        binding.tvSubtitle.text = "$currentpage/$pageCount"
                        Log.d(TAG, "loadBookFromUrl: $currentpage/$pageCount")
            }
                    .onError{e->
                        Log.d(TAG, "loadBookFromUrl: ${e.message}")
                    }
                    .load()
                binding.progressBar.visibility = View.GONE
            }
            .addOnFailureListener {e ->
                Log.d(TAG, "loadBookFromUrl: Failed to get Url due to ${e.message}")
                binding.progressBar.visibility = View.GONE
            }
    }
}
