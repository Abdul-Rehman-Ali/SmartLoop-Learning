package com.smartloopLearn.learning.admin

import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.firebase.auth.FirebaseAuth
import com.smartloopLearn.learning.databinding.ActivityDashboardUserBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DashboardUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardUserBinding
    private lateinit var categoryArrayList: ArrayList<ModelCategorey>
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var  firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Prevent screenshots and screen recording
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        enableEdgeToEdge()

        // Inflate the layout using view binding
        binding = ActivityDashboardUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        setupWithViewpager(binding.viewPager)
        binding.tablayout.setupWithViewPager(binding.viewPager)
    }

    private fun setupWithViewpager(viewPager: ViewPager) {
        viewPagerAdapter = ViewPagerAdapter(
            supportFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
            this
        )
        categoryArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Category")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryArrayList.clear()
                val modelAll = ModelCategorey("01", "All", 1, "")
//                val modelMostViewed = ModelCategorey("02", "Most viewed", 1, "")
//                val modelMostDownloaded = ModelCategorey("03", "Most Downloaded", 1, "")

                categoryArrayList.add(modelAll)
//                categoryArrayList.add(modelMostViewed)
//                categoryArrayList.add(modelMostDownloaded)

                viewPagerAdapter.addFragment(
                    BooksUserFragment.newInstance(
                        "${modelAll.id}",
                        "${modelAll.category}",
                        "${modelAll.uid}"
                    ), modelAll.category
                )

//                viewPagerAdapter.addFragment(
//                    BooksUserFragment.newInstance(
//                        "${modelMostViewed.id}",
//                        "${modelMostViewed.category}",
//                        "${modelMostViewed.uid}"
//                    ), modelMostViewed.category
//                )

//                viewPagerAdapter.addFragment(
//                    BooksUserFragment.newInstance(
//                        "${modelMostDownloaded.id}",
//                        "${modelMostDownloaded.category}",
//                        "${modelMostDownloaded.uid}"
//                    ), modelMostDownloaded.category
//                )

                viewPagerAdapter.notifyDataSetChanged()

                for (ds in snapshot.children) {
                    val model = ds.getValue(ModelCategorey::class.java)
                    categoryArrayList.add(model!!)

                    viewPagerAdapter.addFragment(
                        BooksUserFragment.newInstance(
                            "${model.id}",
                            "${model.category}",
                            "${model.uid}"
                        ), model.category
                    )

                    viewPagerAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

        viewPager.adapter = viewPagerAdapter
    }

    class ViewPagerAdapter(fm: FragmentManager, behavior: Int,  context: Context) : FragmentPagerAdapter(fm, behavior) {
        private val fragmentList: ArrayList<BooksUserFragment> = ArrayList()
        private val fragmentTitleList: ArrayList<String> = ArrayList()
        private val context : Context

        init {
            this.context = context
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getPageTitle(position: Int): CharSequence {
            return fragmentTitleList[position]
        }

        fun addFragment(fragment: BooksUserFragment, title: String) {
            fragmentList.add(fragment)
            fragmentTitleList.add(title)
        }
    }
}
