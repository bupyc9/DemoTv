package ru.bupyc9.demotv.model

import android.os.Parcel
import android.os.Parcelable
import java.net.URI
import java.net.URISyntaxException

data class Movie(
        val id: Int,
        val title: String,
        val studio: String,
        val imageUrl: String,
        val description: String
) : Parcelable {
    fun getImageURI(): URI? {
        try {
            return URI(imageUrl)
        } catch (e: URISyntaxException) {
            return null
        }
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Movie> = object : Parcelable.Creator<Movie> {
            override fun createFromParcel(source: Parcel): Movie = Movie(source)
            override fun newArray(size: Int): Array<Movie?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(title)
        dest.writeString(studio)
        dest.writeString(imageUrl)
        dest.writeString(description)
    }
}