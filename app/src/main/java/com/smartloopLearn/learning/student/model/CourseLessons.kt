package com.smartloopLearn.learning.student.model

import android.os.Parcel
import android.os.Parcelable

data class CourseLessons(
    val title: String,
    val videoURL: String,
    val description: String,
    var isExpanded: Boolean = false // Expansion state
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<CourseLessons> {
        override fun createFromParcel(parcel: Parcel): CourseLessons {
            return CourseLessons(parcel)
        }

        override fun newArray(size: Int): Array<CourseLessons?> {
            return arrayOfNulls(size)
        }
    }
}

