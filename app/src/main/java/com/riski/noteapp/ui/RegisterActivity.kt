package com.riski.noteapp.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.riski.noteapp.R
import com.riski.noteapp.api.ApiService
import com.riski.noteapp.databinding.ActivityRegisterBinding
import com.riski.noteapp.viewmodel.UserViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


class RegisterActivity : AppCompatActivity()/*, EasyPermissions.PermissionCallbacks*/ {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var sharePref: SharedPreferences
    val USER_DATA = "user data"
    private var imageUri: Uri? = null
    private var imageName: String = ""
    val REQUEST_IMAGE = 100
    val BASE_URL = "http://192.168.1.9/notesapi/php/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharePref = getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)

        if (sharePref.getString(getString(R.string.id), null) != null) {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        userViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserViewModel::class.java)

        binding.apply {

//            btnAddImage.setOnClickListener {
//                if (EasyPermissions.hasPermissions(
//                        this@RegisterActivity,
//                        Manifest.permission.READ_EXTERNAL_STORAGE
//                    )
//                ) {
//                    val openGalleryIntent = Intent(Intent.ACTION_PICK)
//                    openGalleryIntent.type = "image/*"
//                    startActivityForResult(openGalleryIntent, REQUEST_IMAGE)
//                } else {
//                    EasyPermissions.requestPermissions(
//                        this@RegisterActivity,
//                        "Izinkan Aplikasi Mengakses Storage?",
//                        REQUEST_IMAGE,
//                        Manifest.permission.READ_EXTERNAL_STORAGE
//                    )
//                }
//            }

            username.addTextChangedListener {
                if (it.isNullOrEmpty()) {
                    edtUsername.isErrorEnabled = true
                    edtUsername.error = "Username tidak boleh kosong"
                } else {
                    edtUsername.isErrorEnabled = false
                }
            }
            password.addTextChangedListener {
                if (it.isNullOrEmpty()) {
                    edtPassword.isErrorEnabled = true
                    edtPassword.error = "Password tidak boleh kosong"
                } else {
                    edtPassword.isErrorEnabled = false
                }
            }
            passwordKonfirm.addTextChangedListener {
                if (it.isNullOrEmpty()) {
                    edtPasswordKonfirm.isErrorEnabled = true
                    edtPasswordKonfirm.error = "Konfirmasi Password tidak boleh kosong"
                } else {
                    edtPasswordKonfirm.isErrorEnabled = false
                }
            }
            btnRegister.setOnClickListener {
                when {
                    username.text.isNullOrEmpty() -> {
                        edtUsername.isErrorEnabled = true
                        edtUsername.error = "Username tidak boleh kosong"
                        username.requestFocus()
                    }
                    password.text.isNullOrEmpty() -> {
                        edtPassword.isErrorEnabled = true
                        edtPassword.error = "Password tidak boleh kosong"
                        password.requestFocus()
                    }
                    passwordKonfirm.text.isNullOrEmpty() -> {
                        edtPasswordKonfirm.isErrorEnabled = true
                        edtPasswordKonfirm.error = "Konfirmasi Password tidak boleh kosong"
                        passwordKonfirm.requestFocus()
                    }
                    password.text.toString() != passwordKonfirm.text.toString() -> {
                        edtPasswordKonfirm.isErrorEnabled = true
                        edtPasswordKonfirm.error = "Password dan Konfirmasi Password tidak sama"
                        passwordKonfirm.requestFocus()
                    }
                    else -> {
                        userViewModel.insertUser(this@RegisterActivity, binding.username.text.toString(), binding.password.text.toString())
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    }
                }
            }

            tvLogin.setOnClickListener { startActivity(Intent(this@RegisterActivity, LoginActivity::class.java)) }
        }
    }

    private fun getRealPathFromURIPath(contentURI: Uri, activity: Activity): String? {
        val cursor: Cursor? = activity.contentResolver.query(contentURI, null, null, null, null)
        return if (cursor == null) {
            contentURI.path
        } else {
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            cursor.getString(idx)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_IMAGE && resultCode == RESULT_OK){
            imageUri = data?.data!!

//            uploadFile(imageUri)
        }
    }

//    fun uploadFile() {
//        if (imageUri != null) {
//            val filePath = getRealPathFromURIPath(imageUri!!, this@RegisterActivity)
//            val file = File(filePath)
//            Log.e("RegisterActivity", "File: ${file.name} ; ${file.path}")
//
//            imageName = file.name
//
//            //membungkus file ke dalam request body
//            val mFile: RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
//
//            // membuat formdata multipart berisi request body
//            val body: MultipartBody.Part = MultipartBody.Part.createFormData(imageName, file.name, mFile)
//
//            userViewModel.uploadImage(this, file.path, body)
//        }

//        userViewModel.insertUser(this, binding.username.text.toString(), binding.password.text.toString())

//        val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
//        val client = OkHttpClient.Builder()
//            .addInterceptor(loggingInterceptor)
//            .build()
//        val retrofit: Retrofit = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(client)
//            .build()
//        val service = retrofit.create(ApiService::class.java)
//        val uploadGambar: Call<RequestBody> = service.uploadGambar(body = body)
//        uploadGambar.enqueue(object : Callback<RequestBody?> {
//            override fun onResponse(call: Call<RequestBody?>, response: Response<RequestBody?>) {
//                Log.e("RegisterActivity", "onResponse: $response", )
//            }
//
//            override fun onFailure(call: Call<RequestBody?>, t: Throwable) {
//                Log.e("RegisterActivity", "onFailure: ", t)
//            }
//        })
//    }

//    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
//        if(requestCode == REQUEST_IMAGE){
//            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
//        if(requestCode == REQUEST_IMAGE){
//            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
//        }
//    }
}