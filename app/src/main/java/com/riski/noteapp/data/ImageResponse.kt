package com.riski.noteapp.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class ImageResponse (

    @field:SerializedName("status")
    val status: Int? = null,

    @field:SerializedName("message")
    val message: String? = null,

) : Parcelable