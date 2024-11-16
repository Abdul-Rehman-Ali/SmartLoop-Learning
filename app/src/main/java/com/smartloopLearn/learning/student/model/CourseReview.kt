package com.smartloopLearn.learning.student.model

//data class CourseReview(
//    val reviewerName: String = "",
//    val rating: Float = 0.0f,
//    val role: String = "Student",  // Default role is "Student"
//    val reviewDescription: String = ""
//)
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CourseReview(
    val StudentName: String = "",
    val Rating: String = "",
//    val role: String = "Student",  // Default role is "Student"
    val Comment: String = ""
) : Parcelable