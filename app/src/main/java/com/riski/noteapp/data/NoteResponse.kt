package com.riski.noteapp.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NoteResponse(

	@field:SerializedName("data")
	val data: List<NoteItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
) : Parcelable

@Parcelize
data class NoteItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("note_id")
	val noteId: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable


