package com.riski.noteapp.api

import com.riski.noteapp.data.Response
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("phprestapi.php")
    fun getNotes(
        @Query("function") function: String = "get_notes"
    ): Call<Response>

    @GET("phprestapi.php")
    fun getNoteById(
        @Query("function") function: String = "get_note_id",
        @Query("id") id: String
    ): Call<Response>

    @FormUrlEncoded
    @POST("phprestapi.php")
    fun insertNote(
        @Field("message") message: String,
        @Field("date") date: String,
        @Query("function") function: String = "insert_note"
    ): Call<Response>

    @FormUrlEncoded
    @POST("phprestapi.php")
    fun updateNote(
        @Field("message") message: String,
        @Field("date") date: String,
        @Query("function") function: String = "update_note",
        @Query("id") id: String
    ): Call<Response>

    @DELETE("phprestapi.php")
    fun deleteNoteById(
        @Query("function") function: String = "delete_note",
        @Query("id") id: String
    ): Call<Response>
}