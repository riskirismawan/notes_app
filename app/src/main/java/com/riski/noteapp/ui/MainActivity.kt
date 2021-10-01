package com.riski.noteapp.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.riski.noteapp.R
import com.riski.noteapp.viewmodel.NoteViewModel
import com.riski.noteapp.adapter.ListNoteAdapter
import com.riski.noteapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ListNoteAdapter
    private lateinit var viewModel: NoteViewModel
    private lateinit var sharePref: SharedPreferences
    private val USER_DATA = "user data"
    private var isLogin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharePref = getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)

        if (sharePref.getString(getString(R.string.id), null) != null) {
            isLogin = true
        }

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(NoteViewModel::class.java)

        showRecyclerView()

        binding.fab.setOnClickListener {
            val intent = Intent(this, FormActivity::class.java)
            intent.putExtra(FormActivity.NOTE_RESPONSE, "add")
            startActivity(intent)
        }
    }

    private fun showRecyclerView() {
        adapter = ListNoteAdapter()
        binding.rvNotes.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvNotes.adapter = adapter

        viewModel.getNotes(this).observe(this, { notes ->
            if (!notes.isNullOrEmpty()) {
                adapter.setItems(notes)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        if (isLogin) {
            menu?.getItem(0)?.isVisible = false
            menu?.getItem(1)?.isVisible = false
            menu?.getItem(2)?.isVisible = true
        } else {
            menu?.getItem(0)?.isVisible = true
            menu?.getItem(1)?.isVisible = true
            menu?.getItem(2)?.isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.login -> startActivity(Intent(this, LoginActivity::class.java))
            R.id.register -> startActivity(Intent(this, RegisterActivity::class.java))
            R.id.profile -> startActivity(Intent(this, ProfileActivity::class.java))
        }
        return true
    }
}