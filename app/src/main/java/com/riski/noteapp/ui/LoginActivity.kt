package com.riski.noteapp.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.riski.noteapp.R
import com.riski.noteapp.databinding.ActivityLoginBinding
import com.riski.noteapp.databinding.BottomSheetDialogBinding
import com.riski.noteapp.viewmodel.UserViewModel
import java.util.concurrent.Executor

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var dialogBinding: BottomSheetDialogBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var sharePref: SharedPreferences
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private var deviceId: String = ""
    val USER_DATA = "user data"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor, object: BiometricPrompt.AuthenticationCallback(){

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)

//                Toast.makeText(this@LoginActivity, "Authentication Success...!", Toast.LENGTH_SHORT).show()

                val username = binding.username.text.toString().trim()
                userViewModel.getUserByUsername(this@LoginActivity, username).observe(this@LoginActivity, {
                    if (!it.userId.isNullOrEmpty()) {
                        val editor: SharedPreferences.Editor = sharePref.edit()
                        editor.putString(getString(R.string.id), it.userId)
                        editor.putString(getString(R.string.username), it.username)
                        editor.putString(getString(R.string.password), it.password)
                        editor.putString(getString(R.string.profile_image), it.profileImage)
                        editor.putString(getString(R.string.device_id), deviceId)
                        editor.apply()

                        userViewModel.updateUserDevice(this@LoginActivity, it.userId, deviceId)

                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    }
                })
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(this@LoginActivity, "Authentication Failed...!", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(this@LoginActivity, "Authentication Error: $errString", Toast.LENGTH_SHORT).show()
            }
        })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Login menggunakan Sidik Jari")
            .setNegativeButtonText("Batal")
            .build()


        deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

        sharePref = getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)

        if (sharePref.getString(getString(R.string.id), null) != null) {
            startActivity(Intent(this, MainActivity::class.java))
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
            btnLogin.setOnClickListener {
                when {
                    username.text.isNullOrEmpty() -> {
                        edtUsername.isErrorEnabled = true
                        edtUsername.error = "Username tidak boleh kosong"
                        username.requestFocus()
                    }
                    else -> {
                        val dialog = BottomSheetDialog(this@LoginActivity)
                        dialogBinding = BottomSheetDialogBinding.inflate(LayoutInflater.from(this@LoginActivity))
                        dialog.setContentView(dialogBinding.root)

                        dialogBinding.pass.addTextChangedListener {
                            if (it.isNullOrEmpty()) {
                                dialogBinding.edtPass.isErrorEnabled = true
                                dialogBinding.edtPass.error = "Password tidak boleh kosong"
                            } else {
                                dialogBinding.edtPass.isErrorEnabled = false
                            }
                        }

                        dialogBinding.btnCancel.setOnClickListener { dialog.dismiss() }

                        dialogBinding.btnConfirm.setOnClickListener {
                            when {
                                dialogBinding.pass.text.isNullOrEmpty() -> {
                                    dialogBinding.edtPass.isErrorEnabled = true
                                    dialogBinding.edtPass.error = "Password tidak boleh kosong"
                                    dialogBinding.pass.requestFocus()
                                }
                                else -> {
                                    val name = username.text.toString().trim()
                                    val pass = dialogBinding.pass.text.toString().trim()
                                    userViewModel.getUserByLogin(this@LoginActivity, name, pass).observe(this@LoginActivity, {
                                        if (!it.userId.isNullOrEmpty()) {
                                            val editor: SharedPreferences.Editor = sharePref.edit()
                                            editor.putString(getString(R.string.id), it.userId)
                                            editor.putString(getString(R.string.username), it.username)
                                            editor.putString(getString(R.string.password), it.password)
                                            editor.putString(getString(R.string.profile_image), it.profileImage)
                                            editor.putString(getString(R.string.device_id), deviceId)
                                            editor.apply()

                                            userViewModel.updateUserDevice(this@LoginActivity, it.userId, deviceId)

                                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                        }
                                    })

                                }
                            }
                        }

                        dialogBinding.btnLoginSidikJari.setOnClickListener { biometricPrompt.authenticate(promptInfo) }

                        dialog.setCancelable(false)
                        dialog.show()
                    }
                }
            }

            tvRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
    }
}