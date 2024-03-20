package com.example.mobproj

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

class Album(val id:String?, val image:String?, val title: String?, val artist: String?, val year: Long) : Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong()
    ) {
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(image)
        parcel.writeString(title)
        parcel.writeString(artist)
        parcel.writeLong(year)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Album> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): Album {
            return Album(parcel)
        }

        override fun newArray(size: Int): Array<Album?> {
            return arrayOfNulls(size)
        }
    }

}