package com.riski.noteapp.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserResponse(

    @field:SerializedName("status")
    val status: Int? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: List<UserItem?>? = null

) : Parcelable

@Parcelize
data class UserItem(

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("username")
    val username: String? = null,

    @field:SerializedName("password")
    val password: String? = null,

    @field:SerializedName("profileImage")
    val profileImage: String? = null
) : Parcelable