package com.smartloopLearn.learning

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.smartloopLearn.learning.admin.MyApplication
import com.smartloopLearn.learning.databinding.ActivityPdfDetailBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.FileOutputStream

class PdfDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPdfDetailBinding
    private companion object {
        const val  TAG = "Book_DETAIL_TAG"
    }

    private var bookId = ""
    private var bookTitle = ""
    private var bookUrl = ""

    private lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize view binding
        binding = ActivityPdfDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookId = intent.getStringExtra("bookId")!!

        progressDialog =  ProgressDialog(this)
        progressDialog.setTitle("Please wait...")
        progressDialog.setCanceledOnTouchOutside(false)

        MyApplication.incrementBookViewCount(bookId)

        loadBookDetail()

        binding.btnReadBook.setOnClickListener {
           val  intent = Intent(this, PdfViewActivity::class.java)
            intent.putExtra("bookId", bookId)
            startActivity(intent)
        }
        binding.btnDownloadBook.setOnClickListener {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                Log.d(TAG, "onCreate: Storage Permission is already granted")
                downloadBook()
            }
            else {
                Log.d(TAG, "onCreate: Storage Permission was not granted, Lets request it")
                requestStoragePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }

    private val requestStoragePermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted : Boolean ->
        if  (isGranted){
            Log.d(TAG, "onCreate: Storage Permission is granted")
        } else {
            Log.d(TAG, "onCreate: Storage Permission is denied.")
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    private  fun downloadBook(){

        Log.d(TAG, "downloadBook: Downloading Book")
        progressDialog.setMessage("Downloading Book")
        progressDialog.show()

        val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(bookUrl)
        storageRef.getBytes(com.smartloopLearn.learning.admin.Constants.Max_Byte_PDF)
            .addOnSuccessListener {bytes->
                Log.d(TAG, "downloadBook: Book Downloaded...")
                saveToDownloadFolder(bytes)
            }
            .addOnFailureListener {e->
                progressDialog.dismiss()
                Log.d(TAG, "downloadBook: Failed to download book due to ${e.message} ")
                Toast.makeText(this, "Failed to download book due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveToDownloadFolder(bytes: ByteArray?) {
        Log.d(TAG, "saveToDownloadFolder: Saving Downloaded Book")
        val  nameWithExtension = "$bookTitle.pdf"

        try {
            val downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            downloadFolder.mkdirs()

            val filePath = downloadFolder.path + "/" + nameWithExtension

            val out = FileOutputStream(filePath)
            out.write(bytes)
            out.close()

            Toast.makeText(this, "Saved to downloads folder", Toast.LENGTH_SHORT).show()
            progressDialog.dismiss()
            incrementDownloadCount()

        }
        catch (e: Exception){

        }
    }

    private fun incrementDownloadCount() {
        Log.d(TAG, "incrementDownloadCount: ")
        val ref = FirebaseDatabase.getInstance().getReference("Book")
        ref.child(bookId)
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val downloadCount = "${snapshot.child("downloadCount").value}"
                    Log.d(TAG, "onDataChange: Current Download Count : $downloadCount")

                    if (downloadCount == "" || downloadCount == "null"){
                        downloadCount == "0"
                    }

                    val newDownloadCount : Long = downloadCount.toLong() + 1
                    Log.d(TAG, "onDataChange: New Current Download Count : $newDownloadCount")

                    val hashMap: HashMap<String, Any> = HashMap()
                    hashMap["downloadCount"] = newDownloadCount

                    val dbRef = FirebaseDatabase.getInstance().getReference("Book")
                    dbRef.child(bookId)
                        .updateChildren(hashMap)
                        .addOnSuccessListener {
                            Log.d(TAG, "onDataChange: Downloads  Count Incremented")
                        }
                        .addOnFailureListener {e->
                            Log.d(TAG, "onDataChange: Failed to increment due to ${e.message}")
                        }

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    private fun loadBookDetail() {
        val ref = FirebaseDatabase.getInstance().getReference("Book")
        ref.child(bookId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val categoryId = snapshot.child("categoryId").getValue(String::class.java) ?: ""
                    val description = snapshot.child("description").getValue(String::class.java) ?: ""
                    val downloadCount = snapshot.child("downloadCount").getValue(Long::class.java) ?: 0L
                    val timestamp = snapshot.child("timestamp").getValue(Long::class.java) ?: 0L
                    bookTitle = snapshot.child("title").getValue(String::class.java) ?: ""
                    val uid = snapshot.child("uid").getValue(String::class.java) ?: ""
                    bookUrl = snapshot.child("url").getValue(String::class.java) ?: ""
                    val viewCount = snapshot.child("viewCount").getValue(Long::class.java) ?: 0L

                    val date = MyApplication.formatTimeStamp(timestamp)

                    MyApplication.loadCategory(categoryId, binding.tvCategory)
                    MyApplication.loadPdfFromUrlSinglePage(bookUrl, bookTitle, binding.pdfview, binding.progressBar, binding.tvPages)
                    MyApplication.loadPdfSize(bookUrl, bookTitle, binding.tvSize)

                    binding.tvTitle.text = bookTitle
                    binding.tvDescription.text = description
                    binding.tvViews.text = viewCount.toString()
                    binding.tvDownloads.text = downloadCount.toString()
                    binding.tvDate.text = date
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                }
            })
    }
}
