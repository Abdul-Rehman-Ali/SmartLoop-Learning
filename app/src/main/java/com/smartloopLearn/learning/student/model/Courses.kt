package com.smartloopLearn.learning.student.model

//data class Courses(
//    val id: Int,
//    val courseImage: Int,
//    val courseTitle: String,
//    val tutorName: String,
//    val rating: Double
//)

//data class Courses(
//    val CourseId: String = "",
//    val CourseTitle: String = "",
//    val TeacherName: String = "",
//    val ImageURL: String = "",
//    val Rating: String = "",
//    val CoursePrice: String = "",
//    val CourseDescription: String = ""
//)


import java.io.Serializable

data class Courses(
    val CourseId: String = "",
    val CourseTitle: String = "",
    val TeacherName: String = "",
    val ImageURL: String = "",
    val Rating: String = "",
//    val Rating: Float = 0f,
    val CoursePrice: String = "",
    val CourseDescription: String = "",
    val CourseDuration: String = "",
    val Discount: String = "",
    val Certificate: String = "",
    val CourseLessons: String = ""
) : Serializable  // This will allow passing the object in a Bundle



