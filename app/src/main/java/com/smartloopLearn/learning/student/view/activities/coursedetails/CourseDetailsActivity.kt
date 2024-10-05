package com.smartloopLearn.learning.student.view.activities.coursedetails

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.smartloopLearn.learning.R
import com.smartloopLearn.learning.databinding.ActivityCourseDetailsBinding
import com.smartloopLearn.learning.student.view.activities.coursedetails.frags.CourseLessonsFragment
import com.smartloopLearn.learning.student.view.activities.coursedetails.frags.CourseOverviewFragment
import com.smartloopLearn.learning.student.view.activities.coursedetails.frags.CourseReviewsFragment

class CourseDetailsActivity : AppCompatActivity() {
    private val binding by lazy { ActivityCourseDetailsBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        moveFrag(CourseOverviewFragment())

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                            moveFrag(CourseOverviewFragment())
                    }
                    1 -> {
                        moveFrag(CourseLessonsFragment())
                    }
                    2 -> {
                        moveFrag(CourseReviewsFragment())
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Optional, handle when a tab is unselected
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Optional, handle when a tab is reselected
            }
        })
    }

    fun moveFrag(frag : Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flCourseDetails, frag)
            commit()
        }
    }
}