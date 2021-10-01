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
import com.riski.noteapp.databinding.ActivityLoginBinding
import com.riski.noteapp.viewmodel.UserViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var sharePref: SharedPreferences
    val USER_DATA = "user data"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharePref = getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)

        if (sharePref.getString(getString(R.string.id), null) != null) {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        userViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserViewModel::class.java)

        binding.apply {

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
            btnLogin.setOnClickListener {
                when {
                    username.text.isNullOrEmpty() -> {
                        edtUsername.isErrorEnabled = true
                        edtUsername.error = "Username tidak boleh kosong"
                        username.requestFocus()
                    }
                    password.text.isNullOrEmpty() -> {
                        edtPassword.isErrorEnabled = true
                        edtPassword.error = "Username tidak boleh kosong"
                        password.requestFocus()
                    }
                    else -> {
                        val name = username.text.toString().trim()
                        val pass = password.text.toString().trim()
                        userViewModel.getUserByLogin(this@LoginActivity, name, pass).observe(this@LoginActivity, {
                            if (!it.id.isNullOrEmpty()) {
                                val editor: SharedPreferences.Editor = sharePref.edit()
                                editor.putString(getString(R.string.id), it.id)
                                editor.putString(getString(R.string.username), it.username)
                                editor.putString(getString(R.string.password), it.password)
                                editor.putString(getString(R.string.profile_image), it.profileImage)
                                editor.apply()

                                val intent = Intent(this@LoginActivity, ProfileActivity::class.java)
                                intent.putExtra(ProfileActivity.DATA, it)
                                startActivity(intent)
                            }
                        })
                    }
                }
            }

            tvRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
    }
}