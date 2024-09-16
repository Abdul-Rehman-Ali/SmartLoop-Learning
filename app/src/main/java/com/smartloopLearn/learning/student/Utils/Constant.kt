package com.smartloopLearn.learning.student.Utils

import com.smartloopLearn.learning.R
import com.smartloopLearn.learning.student.model.AllCourses
import com.smartloopLearn.learning.student.model.RVModel
import com.smartloopLearn.learning.student.model.ContinueCourse
import com.smartloopLearn.learning.student.model.Courses
import com.smartloopLearn.learning.student.model.WeProvided

object Constant {

    fun getData() : ArrayList<RVModel> {
        return arrayListOf(
            (RVModel(R.drawable.web, "Web Development")),
            (RVModel(R.drawable.python, "Python")),
                (RVModel(R.drawable.ai, "Artificial Intelligence")),
            (RVModel(R.drawable.digitalmarketing, "Digital Marketing")),
                    (RVModel(R.drawable.appdevelopment, "App Development")),
                    (RVModel(R.drawable.seo, "SEO"))

        )

    }

    fun getDataContinueCourses(): ArrayList<ContinueCourse> {
        return arrayListOf(
            ContinueCourse(0, R.drawable.web, "Web Developement", "Jamal The Expert", 4.5, 60),
            ContinueCourse(1, R.drawable.python, "Python", "Mudassar the pythonist", 4.2, 30),
            ContinueCourse(1, R.drawable.appdevelopment, "App Development", "Muhammad Jamil", 3.1, 100)
        )
    }

    fun getDataWeProvided(): ArrayList<WeProvided> {
        return arrayListOf(
            WeProvided(0, R.drawable.learning1, "Courses"),
            WeProvided(1, R.drawable.learning2, "Exams"),
            WeProvided(1, R.drawable.learning3, "Lectures"),
            WeProvided(1, R.drawable.learning4, "Notes"),
            WeProvided(1, R.drawable.learning5, "MCQs")
        )
    }

    fun getDataCourses(): ArrayList<Courses> {
        return arrayListOf(
            Courses(0, R.drawable.webdevelopmentslider, "Web Developement", "Jamal The Expert", 4.5),
            Courses(1, R.drawable.pythonslider, "Python", "Mudassar the pythonist", 4.2),
            Courses(2, R.drawable.aislider, "AI", "Ali Sher Kashif", 4.2),
            Courses(3, R.drawable.digitalmarketingslider, "Marketing", "Ahmad Shaf", 5.0),
            Courses(4, R.drawable.appdevelopmentslider, "App Development", "Muhammad Jamil", 3.1),
            Courses(5, R.drawable.seoslider, "SEO", "Zafar Roy", 3.0),
        )
    }

    fun getDataAllCourses(): ArrayList<AllCourses> {
        return arrayListOf(
            AllCourses(0, R.drawable.webdevelopmentslider, "Web Developement", "Jamal The Expert", 4.5, 5000),
            AllCourses(1, R.drawable.pythonslider, "Python", "Mudassar the pythonist", 4.2, 7000),
            AllCourses(2, R.drawable.aislider, "AI", "Ali Sher Kashif", 4.2, 15000),
            AllCourses(3, R.drawable.digitalmarketingslider, "Marketing", "Ahmad Shaf", 5.0, 9000),
            AllCourses(4, R.drawable.appdevelopmentslider, "App Development", "Muhammad Jamil", 3.1, 10000),
            AllCourses(5, R.drawable.seoslider, "SEO", "Zafar Roy", 3.0, 12000),
        )
    }
}