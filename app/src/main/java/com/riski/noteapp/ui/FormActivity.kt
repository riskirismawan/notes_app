package com.riski.noteapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.core.view.get
import androidx.core.view.size
import androidx.lifecycle.ViewModelProvider
import com.riski.noteapp.NoteViewModel
import com.riski.noteapp.R
import com.riski.noteapp.databinding.ActivityFormBinding
import java.text.SimpleDateFormat
import java.util.*

class FormActivity : AppCompatActivity() {
    companion object {
        const val NOTE_ID = "id"
        const val NOTE_RESPONSE = "response"
    }

    private lateinit var binding: ActivityFormBinding
    private lateinit var viewModel: NoteViewModel
    private var isEdit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(NoteViewModel::class.java)

        val response = intent.getStringExtra(NOTE_RESPONSE)
        val key = intent.getStringExtra(NOTE_ID)

        if (response == "edit") {
            isEdit = true
            if (key != null) {
                viewModel.getNoteById(this, key).observe(this, { note ->
                    binding.edtMessage.setText(note.message)
                })
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.form_menu, menu)
        if (isEdit) {
            if (menu != null) {
                for (i in 0 until menu.size) {
                    if (menu[i].toString() == "Delete") {
                        menu[i].isVisible = true
                    }
                }
            }
        }
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return super.onPrepareOptionsMenu(menu)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                val date = SimpleDateFormat("yyyy-MM-dd").format(Date())
                if (isEdit) {
                    val key = intent.getStringExtra(NOTE_ID)
                    if (key != null) {
                        viewModel.updateNote(this, key, binding.edtMessage.text.toString(), date)
                    }
                } else {
                    viewModel.insertNote(this, binding.edtMessage.text.toString(), date)
                }
                startActivity(Intent(this, MainActivity::class.java))
            }
            R.id.delete -> {
                val key = intent.getStringExtra(NOTE_ID)
                if (key != null) {
                    viewModel.deleteNote(this, key)
                    startActivity(Intent(this, MainActivity::class.java))
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}