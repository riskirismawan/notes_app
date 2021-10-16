package com.riski.noteapp.viewmodel

import android.content.Context
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.riski.noteapp.api.ApiConfig
import com.riski.noteapp.data.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {

    fun getUserByLogin(context: Context, username: String, password: String): LiveData<UserItem> {
        val user = MutableLiveData<UserItem>()
        val client = ApiConfig.getApiService().getUserByLogin(username = username, password = password)
        val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

        client.enqueue(object: Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    if (!response.body()?.data.isNullOrEmpty()) {
                        val data = response.body()?.data as List<UserItem>
                        if (!data[0].deviceId.isNullOrEmpty()) {
                            if (data[0].deviceId == deviceId) {
                                user.value = data[0]
                                Toast.makeText(context, response.body()?.message, Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(context, "Akun sudah Masuk pada perangkat lain", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            user.value = data[0]
                            Toast.makeText(context, response.body()?.message, Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_LONG).show()
                }

            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                Log.e("UserViewModel", "onFailure: ", t)
            }

        })

        return user
    }

    fun getUserByUsername(context: Context, username: String): LiveData<UserItem> {
        val user = MutableLiveData<UserItem>()
        val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        val client = ApiConfig.getApiService().getUserByUsername(username = username, deviceId = deviceId)

        client.enqueue(object: Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    if (!response.body()?.data.isNullOrEmpty()) {
                        val data = response.body()?.data as List<UserItem>
                        if (!data[0].deviceId.isNullOrEmpty()) {
                            if (data[0].deviceId == deviceId) {
                                user.value = data[0]
                                Toast.makeText(context, response.body()?.message, Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(context, "Akun sudah Masuk pada perangkat lain", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            Toast.makeText(context, response.body()?.message, Toast.LENGTH_LONG).show()
                            user.value = data[0]
                        }
                    }
                } else {
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_LONG).show()
                }
                Toast.makeText(context, response.body()?.message, Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                Log.e("UserViewModel", "onFailure: ", t)
            }

        })

        return user
    }

    fun insertUser(context: Context, username: String, password: String) {
        val client = ApiConfig.getApiService().insertUser(username = username, password = password)

        client.enqueue(object: Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_LONG).show()
                    Log.e("UserViewModel", "onResponse: ${response.body()?.message}")
                } else {
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_LONG).show()
                    Log.e("UserViewModel", "onResponse: ${response.body()?.message}")
                }

            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                Log.e("UserViewModel", "onFailure: ", t)
            }

        })
    }

    fun updateUser(context: Context, userId: String, username: String, password: String, imagePath: String?) {
        val client = ApiConfig.getApiService().updateUser(id = userId, username = username, password = password, imageName = imagePath)

        client.enqueue(object: Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_LONG).show()
                }

            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                Log.e("NoteViewModel", "onFailure: ", t)
            }

        })
    }

    fun updateUserDevice(context: Context, userId: String, deviceId: String?) {
        val client = ApiConfig.getApiService().updateUserDeviceId(id = userId, deviceId = deviceId)

        client.enqueue(object: Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
//                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_LONG).show()
                    Log.d("UserViewModel", "onResponse: ${response.body()?.message}")
                } else {
//                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_LONG).show()
                    Log.e("UserViewModel", "onResponse: ${response.body()?.message}")
                }

            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                Log.e("NoteViewModel", "onFailure: ", t)
            }

        })
    }

    fun deleteUser(context: Context, userId: String) {
        val client = ApiConfig.getApiService().deleteUserById(id = userId)

        client.enqueue(object: Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_LONG).show()
                }

            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                Log.e("NoteViewModel", "onFailure: ", t)
            }

        })
    }

    fun uploadImage(context: Context, /*imagePath: String, */profileImage: MultipartBody.Part, userId: String, username: String, password: String, imagePath: String?) {
        val client = ApiConfig.getApiService().uploadGambar(/*imagePath = imagePath, */image = profileImage)

        client.enqueue(object: Callback<ImageResponse> {
            override fun onResponse(call: Call<ImageResponse>, response: Response<ImageResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_LONG).show()
                    updateUser(context, userId, username, password, imagePath)
                    Log.e("UserViewModel", "onResponse: ${response.body()?.message}")
                } else {
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_LONG).show()
                    Log.e("UserViewModel", "onResponse: ${response.body()?.message}")
                }
            }

            override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                Log.e("UserViewModel", "onFailure: ", t)
            }

        })
    }
}