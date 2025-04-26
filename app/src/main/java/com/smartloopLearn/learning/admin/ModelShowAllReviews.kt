package com.smartloopLearn.learning.admin

data class ModelShowAllReviews(
    var StudentName: String? = "",
    var Comment: String? = "",
    var Rating: String? = "",
    var Approved: Boolean? = false,
    var CourseId: String? = "",   // This is set manually
    var ReviewerId: String? = ""  // This is set manually
) {
    // Empty constructor required for Firebase
    constructor() : this(null, null, null, null, null)
}
