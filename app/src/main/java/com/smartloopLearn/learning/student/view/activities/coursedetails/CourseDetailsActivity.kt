package com.smartloopLearn.learning.student.view.activities.coursedetails

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.smartloopLearn.learning.R
import com.smartloopLearn.learning.databinding.ActivityCourseDetailsBinding
import com.smartloopLearn.learning.student.model.Courses
import com.smartloopLearn.learning.student.view.activities.coursedetails.frags.CourseLessonsFragment
import com.smartloopLearn.learning.student.view.activities.coursedetails.frags.CourseOverviewFragment
import com.smartloopLearn.learning.student.view.activities.coursedetails.frags.CourseReviewsFragment

class CourseDetailsActivity : AppCompatActivity() {
    private val binding by lazy { ActivityCourseDetailsBinding.inflate(layoutInflater) }
    private var courseData: Courses? = null
    private var courseURL: String = ""
    private var player: ExoPlayer? = null
    private lateinit var playerView: PlayerView
    private lateinit var fullscreenButton: ImageView
    private var isFullscreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        playerView = binding.courseVideo
        fullscreenButton = binding.courseVideo.findViewById(R.id.fullscreen_button)

        fullscreenButton.setOnClickListener {
            toggleFullscreen()
        }

        val courseId = intent.getStringExtra("CourseId") ?: return
        fetchCourseDetails(courseId)

        moveFrag(CourseOverviewFragment())

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> moveFrag(CourseOverviewFragment())
                    1 -> moveFrag(CourseLessonsFragment())
                    2 -> moveFrag(CourseReviewsFragment())
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun fetchCourseDetails(courseId: String) {
        val db = FirebaseFirestore.getInstance()
        val courseRef = db.collection("Courses").document(courseId)

        courseRef.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val courseTitle = document.getString("CourseTitle") ?: ""
                val teacherName = document.getString("TeacherName") ?: ""
                val coursePrice = document.getString("CoursePrice") ?: ""
                val rating = document.getString("Rating") ?: ""
                val imageUrl = document.getString("ImageURL") ?: ""
                val courseDescription = document.getString("CourseDescription") ?: ""
                val courseDuration = document.getString("CourseDuration") ?: ""
                val discount = document.getString("Discount") ?: ""
                val certificate = document.getString("Certificate") ?: ""
                val courseLesson = document.getString("CourseLessons") ?: ""
                courseURL = document.getString("CourseURL") ?: ""

                val skillsList = mutableListOf<String>()
                courseRef.collection("Skills").get().addOnSuccessListener { skillsSnapshot ->
                    for (skillDoc in skillsSnapshot) {
                        val skillFields = skillDoc.data
                        skillFields.forEach { (_, value) ->
                            if (value is String) skillsList.add(value)
                        }
                    }

                    courseData = Courses(
                        CourseId = courseId,
                        CourseTitle = courseTitle,
                        TeacherName = teacherName,
                        ImageURL = imageUrl,
                        Rating = rating,
                        CoursePrice = coursePrice,
                        CourseDescription = courseDescription,
                        CourseDuration = courseDuration,
                        Discount = discount,
                        Certificate = certificate,
                        CourseLessons = courseLesson,
                        Skills = skillsList
                    )

                    initializePlayer()
                    moveFrag(CourseOverviewFragment())
                }
            }
        }
    }

    private fun initializePlayer() {
        if (courseURL.isNotEmpty()) {
            player = ExoPlayer.Builder(this).build()
            playerView.player = player
            val mediaItem = MediaItem.fromUri(Uri.parse(courseURL))
            player?.setMediaItem(mediaItem)
            player?.prepare()
            player?.playWhenReady = true
        }
    }

    private fun toggleFullscreen() {
        if (isFullscreen) {
            supportActionBar?.show()
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            playerView.layoutParams.height = resources.getDimensionPixelSize(R.dimen.player_default_height)
            isFullscreen = false
            fullscreenButton.setImageResource(R.drawable.baseline_fullscreen_24)
        } else {
            supportActionBar?.hide()
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            playerView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            isFullscreen = true
            fullscreenButton.setImageResource(R.drawable.baseline_fullscreen_exit_24)
        }
    }

    override fun onStop() {
        super.onStop()
        player?.release()
        player = null
    }

    private fun moveFrag(frag: Fragment) {
        if (frag is CourseOverviewFragment) {
            frag.arguments = Bundle().apply {
                putSerializable("course_data", courseData)
            }
        }
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flCourseDetails, frag)
            commit()
        }
    }
}
