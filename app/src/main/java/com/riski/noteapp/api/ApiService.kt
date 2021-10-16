package com.riski.noteapp.api

import com.riski.noteapp.data.ImageResponse
import com.riski.noteapp.data.NoteResponse
import com.riski.noteapp.data.UserResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.POST
import retrofit2.http.Multipart




interface ApiService {
    @GET("phprestapi.php")
    fun getNotes(
        @Query("function") function: String = "get_notes",
        @Query("user_id") user_id: String
    ): Call<NoteResponse>

    @GET("phprestapi.php")
    fun getNoteById(
        @Query("function") function: String = "get_note_id",
        @Query("note_id") note_id: String
    ): Call<NoteResponse>

    @FormUrlEncoded
    @POST("phprestapi.php")
    fun insertNote(
        @Field("user_id") user_id: String,
        @Field("message") message: String,
        @Field("date") date: String,
        @Query("function") function: String = "insert_note"
    ): Call<NoteResponse>

    @FormUrlEncoded
    @POST("phprestapi.php")
    fun updateNote(
        @Field("message") message: String,
        @Field("date") date: String,
        @Query("function") function: String = "update_note",
        @Query("note_id") note_id: String
    ): Call<NoteResponse>

    @DELETE("phprestapi.php")
    fun deleteNoteById(
        @Query("function") function: String = "delete_note",
        @Query("note_id") note_id: String
    ): Call<NoteResponse>

    @GET("phprestapi.php")
    fun getUserByLogin(
        @Query("function") function: String = "get_user_login",
        @Query("username") username: String,
        @Query("password") password: String
    ): Call<UserResponse>

    @GET("phprestapi.php")
    fun getUserByUsername(
        @Query("function") function: String = "get_user_by_username",
        @Query("username") username: String,
        @Query("device_id") deviceId: String
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("phprestapi.php")
    fun insertUser(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("profileImage") imageName: String? = null,
        @Query("function") function: String = "insert_user",
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("phprestapi.php")
    fun updateUser(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("profileImage") imageName: String? = null,
        @Query("function") function: String = "update_user",
        @Query("id") id: String
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("phprestapi.php")
    fun updateUserDeviceId(
        @Field("device_id") deviceId: String? = null,
        @Query("function") function: String = "update_user_device",
        @Query("id") id: String
    ): Call<UserResponse>

    @Multipart
//    @FormUrlEncoded
    @POST("phprestapi.php")
    fun uploadGambar(
        @Query("function") function: String = "upload",
//        @Field("imageName") imagePath: String,
        @Part image: MultipartBody.Part
    ): Call<ImageResponse>

    @DELETE("phprestapi.php")
    fun deleteUserById(
        @Query("function") function: String = "delete_user",
        @Query("id") id: String
    ): Call<UserResponse>
}