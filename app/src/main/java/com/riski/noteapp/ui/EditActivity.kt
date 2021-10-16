package com.riski.noteapp.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.riski.noteapp.R
import com.riski.noteapp.data.UserItem
import com.riski.noteapp.databinding.ActivityEditBinding
import com.riski.noteapp.viewmodel.UserViewModel

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var sharePref: SharedPreferences
    private lateinit var userData: UserItem
    val USER_DATA = "user data"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharePref = getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)

        userData = UserItem(
            sharePref.getString(getString(R.string.id), null),
            sharePref.getString(getString(R.string.username), null),
            sharePref.getString(getString(R.string.password), null),
            sharePref.getString(getString(R.string.profile_image), null)
        )

        Log.e("EditActivity", "onCreate: $userData", )

        userViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserViewModel::class.java)

        binding.apply {
            username.setText(userData.username)
            password.setText(userData.password)
            passwordKonfirm.setText(userData.password)

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
            btnSaveUsername.setOnClickListener {
                when {
                    username.text.isNullOrEmpty() -> {
                        edtUsername.isErrorEnabled = true
                        edtUsername.error = "Username tidak boleh kosong"
                        username.requestFocus()
                    }
                    else -> {
                        userData.userId?.let { id ->
                            userData.password?.let { password ->
                                userViewModel.updateUser(this@EditActivity,
                                    id, binding.username.text.toString(), password, userData.profileImage)
                            }
                        }
                        val editor: SharedPreferences.Editor = sharePref.edit()
                        editor.putString(getString(R.string.username), username.text.toString())
                        editor.apply()
                        startActivity(Intent(this@EditActivity, ProfileActivity::class.java))
                    }
                }
            }
            btnSavePassword.setOnClickListener {
                when {
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
                        userData.userId?.let { id ->
                            userData.username?.let { username ->
                                userViewModel.updateUser(this@EditActivity,
                                    id, username, password.text.toString(), userData.profileImage)
                            }
                        }
                        val editor: SharedPreferences.Editor = sharePref.edit()
                        editor.putString(getString(R.string.password), password.text.toString())
                        editor.apply()
                        startActivity(Intent(this@EditActivity, ProfileActivity::class.java))
                    }
                }
            }
        }
    }
}