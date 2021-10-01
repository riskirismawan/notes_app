package com.riski.noteapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.riski.noteapp.R
import com.riski.noteapp.data.UserItem
import com.riski.noteapp.databinding.ActivityProfileBinding
import com.riski.noteapp.viewmodel.UserViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

class ProfileActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    companion object {
        const val DATA = "data"
    }

    private lateinit var binding: ActivityProfileBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var sharePref: SharedPreferences
    private lateinit var userData: UserItem
    val USER_DATA = "user data"
    val REQUEST_IMAGE = 100
    val BASE_URL = "http://192.168.1.9/notesapi/php/images/"
    private var imageUri: Uri? = null
    private var imagePath: String = ""

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharePref = getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)

        userData = UserItem(
            sharePref.getString(getString(R.string.id), null),
            sharePref.getString(getString(R.string.username), null),
            sharePref.getString(getString(R.string.password), null),
            sharePref.getString(getString(R.string.profile_image), null)
        )

        Log.e("ProfileActivity", "onCreate: $userData", )

        userViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserViewModel::class.java)

        val user = intent.getParcelableExtra<UserItem>(DATA)

        binding.apply {
            Glide.with(this@ProfileActivity)
                .load(BASE_URL+userData.profileImage)
                .error(getDrawable(R.drawable.user_image))
                .into(imgProfile)

            tvUsername.text = userData.username

            addPhoto.setOnClickListener {
                if (EasyPermissions.hasPermissions(
                        this@ProfileActivity,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    val openGalleryIntent = Intent(Intent.ACTION_PICK)
                    openGalleryIntent.type = "image/*"
                    startActivityForResult(openGalleryIntent, REQUEST_IMAGE)
                } else {
                    EasyPermissions.requestPermissions(
                        this@ProfileActivity,
                        "Izinkan Aplikasi Mengakses Storage?",
                        REQUEST_IMAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                }
            }

            tvEditProfile.setOnClickListener {
                startActivity(Intent(this@ProfileActivity, EditActivity::class.java))
            }

            tvDeleteProfile.setOnClickListener {
                userData.id?.let { id -> userViewModel.deleteUser(this@ProfileActivity, id) }
                sharePref.edit().clear().apply()
                finish()
            }

            tvLogout.setOnClickListener {
                sharePref.edit().clear().apply()
                startActivity(Intent(this@ProfileActivity, LoginActivity::class.java))
            }
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

            Glide.with(this)
                .load(imageUri)
                .error(R.drawable.user_image)
                .into(binding.imgProfile)

            uploadFile()
        }
    }

    fun uploadFile() {
        if (imageUri != null) {
            val filePath = getRealPathFromURIPath(imageUri!!, this@ProfileActivity)
            val file = File(filePath)
            Log.e("RegisterActivity", "File: ${file.name} ; ${file.path}")

            imagePath = BASE_URL+file.name
            Log.e("RegisterActivity", "File path: $imagePath")

            //membungkus file ke dalam request body
            val mFile: RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)

            // membuat formdata multipart berisi request body
            val body: MultipartBody.Part = MultipartBody.Part.createFormData("file", file.name, mFile)

            userData.id?.let { id ->
                userData.username?.let { username ->
                    userData.password?.let { password ->
                        userViewModel.uploadImage(this, /*file.name.toString(),*/ body,
                            id, username, password, imagePath)
                    }
                }
            }
            val editor: SharedPreferences.Editor = sharePref.edit()
            editor.putString(getString(R.string.profile_image), imagePath)
            editor.apply()
        }

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
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if(requestCode == REQUEST_IMAGE){
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(requestCode == REQUEST_IMAGE){
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }
}