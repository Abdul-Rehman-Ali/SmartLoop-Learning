package com.smartloopLearn.learning.admin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.smartloopLearn.learning.databinding.FragmentBooksUserBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BooksUserFragment : Fragment {

    private var _binding: FragmentBooksUserBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val TAG = "Book_USER_TAG"

        public fun newInstance(categoryId:String, category: String, uid:String ): BooksUserFragment{
            val fragment =  BooksUserFragment()
            val args = Bundle()
            args.putString("categoryId",categoryId)
            args.putString("category",category)
            args.putString("uid",uid)
            fragment.arguments = args
            return fragment
        }
    }

    private var categoryId = ""
    private var category = ""
    private var uid = ""

    private lateinit var pdfArrayList: ArrayList<ModelPdf>
    private lateinit var adapterPdfUser: AdapterPdfUser

    constructor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = arguments
        if(args != null){
            categoryId = args.getString("categoryId")!!
            category = args.getString("category")!!
            uid = args.getString("uid")!!
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using view binding
        _binding = FragmentBooksUserBinding.inflate(inflater, container, false)

        Log.d(TAG, "onCreateView: $category")
        if (category == "All"){
            loadsAllBook()
        } else if (category == "Most Viewed") {
            loadMostViewDownloadBooks("viewCount")

        } else if (category == "Most Downloaded"){
            loadMostViewDownloadBooks("downloadCount")

        } else {
            loadCategorizedBooks()
        }

//        binding.etSearch.addTextChangedListener {
//            object  : TextWatcher {
//                override fun beforeTextChanged(
//                    s: CharSequence?, start: Int, count: Int, after: Int
//                ) {
//
//                }
//                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                    try {
//                        adapterPdfUser.filter.filter(s)
//                    } catch (e:Exception){
//                        Log.d(TAG, "onTextChanged: Search Exception ${e.message}")
//                    }
//                }
//
//                override fun afterTextChanged(s: Editable?) {
//
//                }
//            }
//        }

        return binding.root

    }

    private fun loadsAllBook() {
        pdfArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Book")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                pdfArrayList.clear()
                for(ds in snapshot.children){
                    val model = ds.getValue(ModelPdf::class.java)
                    pdfArrayList.add(model!!)
                }
                adapterPdfUser = AdapterPdfUser(context!!, pdfArrayList)
                binding.rvBooks.adapter = adapterPdfUser
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun loadMostViewDownloadBooks(orderBy: String) {
        pdfArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Book")
        ref.orderByChild(orderBy).limitToLast(10)
            .addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                pdfArrayList.clear()
                for(ds in snapshot.children){
                    val model = ds.getValue(ModelPdf::class.java)
                    pdfArrayList.add(model!!)
                }
                adapterPdfUser = AdapterPdfUser(context!!, pdfArrayList)
                binding.rvBooks.adapter = adapterPdfUser
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }



    private fun loadCategorizedBooks() {
        pdfArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Book")
        ref.orderByChild("Category").equalTo(categoryId)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    pdfArrayList.clear()
                    for(ds in snapshot.children){
                        val model = ds.getValue(ModelPdf::class.java)
                        pdfArrayList.add(model!!)
                    }
                    adapterPdfUser = AdapterPdfUser(context!!, pdfArrayList)
                    binding.rvBooks.adapter = adapterPdfUser
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
