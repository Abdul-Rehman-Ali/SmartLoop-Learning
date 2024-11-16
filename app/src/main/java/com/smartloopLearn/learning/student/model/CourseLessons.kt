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
    )

    override fun describeContents(): Int {
        return 0 // No special file descriptors
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(videoURL)
        parcel.writeString(description)
        parcel.writeByte(if (isExpanded) 1 else 0)
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
