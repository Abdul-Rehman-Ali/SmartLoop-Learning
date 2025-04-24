package com.smartloopLearn.learning.student.view.activities.coursedetails

import android.content.Context
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.smartloopLearn.learning.R
import com.smartloopLearn.learning.databinding.ActivityCourseDetailsBinding
import com.smartloopLearn.learning.student.adapter.recyclerview.OnVideoClickListener
import com.smartloopLearn.learning.student.model.CourseLessons
import com.smartloopLearn.learning.student.model.CourseReview
import com.smartloopLearn.learning.student.model.Courses
import com.smartloopLearn.learning.student.view.activities.coursedetails.frags.CourseLessonsFragment
import com.smartloopLearn.learning.student.view.activities.coursedetails.frags.CourseOverviewFragment
import com.smartloopLearn.learning.student.view.activities.coursedetails.frags.CourseReviewsFragment

class CourseDetailsActivity : AppCompatActivity(), OnVideoClickListener {
    private val binding by lazy { ActivityCourseDetailsBinding.inflate(layoutInflater) }
    private var courseData: Courses? = null
    private var courseURL: String = ""
    private var player: ExoPlayer? = null
    private lateinit var playerView: PlayerView
    private lateinit var fullscreenButton: ImageView
    private var isFullscreen = false
    private var reviews = mutableListOf<CourseReview>()
    private var lessons = mutableListOf<CourseLessons>()

    private val sharedPrefFile = "smartloopLearnPrefs"
    private val sharedPrefNameKey = "user_name"
    private val sharedPrefEmailKey = "user_email"
    private val sharedPrefPhoneKey = "user_phone"
    private var isEnrolled = false  // Flag to check if the user is enrolled

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Prevent screenshots and screen recording
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

        enableEdgeToEdge()
        setContentView(binding.root)

        // Handle window insets (edge-to-edge layout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        playerView = binding.courseVideo
        fullscreenButton = binding.courseVideo.findViewById(R.id.fullscreen_button)

        // Handle fullscreen button click
        fullscreenButton.setOnClickListener {
            toggleFullscreen()
        }

        val courseId = intent.getStringExtra("CourseId") ?: return
        fetchCourseDetails(courseId)

        // Initial fragment to display CourseOverviewFragment
        moveFrag(CourseOverviewFragment())

        // Tab layout listener to switch between fragments
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> moveFrag(CourseOverviewFragment())
                    1 -> {
                        if (isEnrolled) {
                            moveFrag(CourseLessonsFragment())
                        } else {
                            Toast.makeText(this@CourseDetailsActivity, "You need to enroll first to access lessons and refresh page", Toast.LENGTH_SHORT).show()
                            binding.tabLayout.getTabAt(1)?.select() // Keep the Lessons tab selected but show message
                        }
                    }
                    2 -> {
                        if (reviews.isEmpty()) {
                            fetchCourseReviews(courseId)
                        } else {
                            moveFrag(CourseReviewsFragment())
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        // Enroll button click listener
        binding.btnEnroll.setOnClickListener {
            if (!isEnrolled) {
                enrollStudent(courseId, courseData?.CourseTitle ?: "Unknown Course Name")
            } else {
                Toast.makeText(this, "Already enrolled in this course", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun fetchCourseDetails(courseId: String) {
        val db = FirebaseFirestore.getInstance()
        val courseRef = db.collection("Courses").document(courseId)

        courseRef.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                // Fetch course data
                courseURL = document.getString("CourseURL") ?: ""

                // Initialize skills list as empty for now
                val skillsList = mutableListOf<String>()

                // Fetch the first document from the 'Skills' subcollection
                val skillsRef = courseRef.collection("Skills")
                skillsRef.get().addOnSuccessListener { skillsSnapshot ->
                    if (!skillsSnapshot.isEmpty) {
                        val skillDoc = skillsSnapshot.documents[0] // Get the first document
                        val skillFields = skillDoc.data // This will give you all fields as a Map<String, Any>
                        if (skillFields != null) {
                            for ((key, value) in skillFields) {
                                // Assuming the fields represent skills as strings
                                if (value is String) {
                                    skillsList.add(value)
                                }
                            }
                        }
                    }

                    // Check if user is enrolled in the course
                    val studentId = FirebaseAuth.getInstance().currentUser?.uid
                    val enrollmentRef = db.collection("Enrollments").document(studentId ?: "Unknown ID")

                    enrollmentRef.get().addOnSuccessListener { enrollmentDoc ->
                        if (enrollmentDoc.exists()) {
                            val courses = enrollmentDoc.get("courses") as? List<Map<String, Any>> ?: emptyList()
                            isEnrolled = courses.any { it["courseId"] == courseId }
                            if (isEnrolled) {
//                                binding.btnEnroll.text = "Already Enrolled"
                                binding.btnEnroll.visibility = View.GONE
                            }
                        }
                        fetchLessons(courseRef.id)
                    }

                    // Creating the courseData object and passing the skillsList
                    courseData = Courses(
                        CourseId = courseId,
                        CourseTitle = document.getString("CourseTitle") ?: "",
                        TeacherName = document.getString("TeacherName") ?: "",
                        ImageURL = document.getString("ImageURL") ?: "",
                        Rating = document.getString("Rating") ?: "",
                        CoursePrice = document.getString("CoursePrice") ?: "",
                        CourseDescription = document.getString("CourseDescription") ?: "",
                        CourseDuration = document.getString("CourseDuration") ?: "",
                        Discount = document.getString("Discount") ?: "",
                        Certificate = document.getString("Certificate") ?: "",
                        CourseLessons = document.getString("CourseLessons") ?: "",
                        Skills = skillsList
                    )

                    // Initialize player after course data is ready
                    initializePlayer()
                }
            }
        }
    }


    private fun fetchLessons(courseId: String) {
        val db = FirebaseFirestore.getInstance()
        val lessonsRef = db.collection("Courses").document(courseId).collection("Lessons")

        lessonsRef.get().addOnSuccessListener { lessonsSnapshot ->
            val lessonsList = mutableListOf<CourseLessons>()
            for (lessonDoc in lessonsSnapshot) {
                val title = lessonDoc.getString("title") ?: ""
                val videoURL = lessonDoc.getString("videoURL") ?: ""
                val description = lessonDoc.getString("description") ?: ""

                val lesson = CourseLessons(
                    title = title,
                    videoURL = videoURL,
                    description = description,
                    isExpanded = false
                )

                lessonsList.add(lesson)
            }
            lessons = lessonsList

            fetchCourseReviews(courseId)
        }
    }

    private fun fetchCourseReviews(courseId: String) {
        val db = FirebaseFirestore.getInstance()
        val reviewsRef = db.collection("Courses").document(courseId).collection("Reviews")

        reviewsRef.get().addOnSuccessListener { reviewsSnapshot ->
            val reviewsList = mutableListOf<CourseReview>()
            for (reviewDoc in reviewsSnapshot) {
                val review = reviewDoc.toObject(CourseReview::class.java)
                reviewsList.add(review)
            }
            reviews = reviewsList

            moveFrag(CourseOverviewFragment())
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

    override fun onVideoClick(videoURL: String) {
        if (videoURL.isNotEmpty()) {
            val mediaItem = MediaItem.fromUri(Uri.parse(videoURL))
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


    private fun enrollStudent(courseId: String, courseName: String) {
        // Retrieve student details from SharedPreferences
        val sharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        // Fetch data using the correct SharedPreferences keys
        val studentName = sharedPreferences.getString(sharedPrefNameKey, "Unknown Name") ?: "Unknown Name"
        val studentEmail = sharedPreferences.getString(sharedPrefEmailKey, "Unknown Email") ?: "Unknown Email"
        val studentPhone = sharedPreferences.getString(sharedPrefPhoneKey, "Unknown Phone") ?: "Unknown Phone"

        // Ensure valid student details are fetched
        if (studentName == "Unknown Name" || studentEmail == "Unknown Email" || studentPhone == "Unknown Phone") {
            Toast.makeText(this, "Student data is missing. Please log in again.", Toast.LENGTH_SHORT).show()
            return
        }

        val studentId = FirebaseAuth.getInstance().currentUser?.uid ?: "Unknown ID"
        val enrollmentDate = System.currentTimeMillis()  // Timestamp for enrollment
        val enrollmentStatus = "Enrolled"  // Enrollment status

        if (studentId == "Unknown ID") {
            Toast.makeText(this, "Failed to retrieve student ID!", Toast.LENGTH_SHORT).show()
            return
        }

        val db = FirebaseFirestore.getInstance()
        val enrollmentRef = db.collection("Enrollments").document(studentId)

        enrollmentRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                // Student exists, update their course list with additional details
                val courses = document.get("courses") as? MutableList<HashMap<String, Any>> ?: mutableListOf()

                // Create course map with additional student details
                val courseMap = hashMapOf<String, Any>(
                    "courseId" to courseId,
                    "courseName" to courseName,
                    "studentName" to studentName,
                    "studentEmail" to studentEmail,
                    "studentPhone" to studentPhone,
                    "enrollmentDate" to enrollmentDate,
                    "status" to enrollmentStatus
                )

                // Check if the student is already enrolled in the course
                if (courses.none { it["courseId"] == courseId }) {
                    courses.add(courseMap)
                    enrollmentRef.update("courses", courses).addOnSuccessListener {
                        Toast.makeText(this, "Enrolled in $courseName successfully!", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Failed to enroll: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Already enrolled in $courseName!", Toast.LENGTH_SHORT).show()
                }
            } else {
                // New student, create a new document with course and student details
                val coursesList = listOf(
                    hashMapOf<String, Any>(
                        "courseId" to courseId,
                        "courseName" to courseName,
                        "studentName" to studentName,
                        "studentEmail" to studentEmail,
                        "studentPhone" to studentPhone,
                        "enrollmentDate" to enrollmentDate,
                        "status" to enrollmentStatus
                    )
                )

                val enrollmentData = hashMapOf(
                    "studentId" to studentId,
                    "courses" to coursesList
                )

                enrollmentRef.set(enrollmentData).addOnSuccessListener {
                    Toast.makeText(this, "Enrolled in $courseName successfully!", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to enroll: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to check enrollment: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onStop() {
        super.onStop()
        player?.release()
        player = null
    }

    private fun moveFrag(frag: Fragment) {
        when (frag) {
            is CourseOverviewFragment -> frag.arguments = Bundle().apply {
                putSerializable("course_data", courseData)
            }
            is CourseLessonsFragment -> frag.arguments = Bundle().apply {
                putSerializable("course_data", courseData)
                putParcelableArrayList("lessons", ArrayList(lessons))
            }
            is CourseReviewsFragment -> frag.arguments = Bundle().apply {
                putSerializable("course_data", courseData)
                putParcelableArrayList("reviews", ArrayList(reviews))
            }
        }
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flCourseDetails, frag)
            commit()
        }
    }
}