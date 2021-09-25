package com.riski.noteapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.riski.noteapp.NoteViewModel
import com.riski.noteapp.R
import com.riski.noteapp.adapter.ListNoteAdapter
import com.riski.noteapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ListNoteAdapter
    private lateinit var viewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
}