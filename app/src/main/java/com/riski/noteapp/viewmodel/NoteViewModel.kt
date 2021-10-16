package com.riski.noteapp.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.riski.noteapp.api.ApiConfig
import com.riski.noteapp.data.NoteItem
import com.riski.noteapp.data.NoteResponse
import retrofit2.Call
import retrofit2.Callback

class NoteViewModel: ViewModel() {
    fun getNotes(context: Context, userId: String): LiveData<List<NoteItem>> {
        val notes = MutableLiveData<List<NoteItem>>()
        val client = ApiConfig.getApiService().getNotes(user_id = userId)

        client.enqueue(object: Callback<NoteResponse> {
            override fun onResponse(call: Call<NoteResponse>, noteResponse: retrofit2.Response<NoteResponse>) {
                if (noteResponse.isSuccessful) {
                    if (!noteResponse.body()?.data.isNullOrEmpty()) {
                        notes.value = noteResponse.body()?.data as List<NoteItem>
                    }
                } else {
                    Toast.makeText(context, noteResponse.body()?.message, Toast.LENGTH_LONG).show()
                }

            }

            override fun onFailure(call: Call<NoteResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                Log.e("NoteViewModel", "onFailure: ", t)
            }

        })

        return notes
    }

    fun getNoteById(context: Context, noteId: String): LiveData<NoteItem> {
        val note = MutableLiveData<NoteItem>()
        val client = ApiConfig.getApiService().getNoteById(note_id = noteId)

        client.enqueue(object: Callback<NoteResponse> {
            override fun onResponse(call: Call<NoteResponse>, noteResponse: retrofit2.Response<NoteResponse>) {
                if (noteResponse.isSuccessful) {
                    val data = noteResponse.body()?.data as List<NoteItem>
                    note.value = data[0]
                } else {
                    Toast.makeText(context, noteResponse.body()?.message, Toast.LENGTH_LONG).show()
                }

            }

            override fun onFailure(call: Call<NoteResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                Log.e("NoteViewModel", "onFailure: ", t)
            }

        })

        return note
    }

    fun insertNote(context: Context, userId: String, message: String, date: String) {
        val client = ApiConfig.getApiService().insertNote(userId, message, date)

        client.enqueue(object: Callback<NoteResponse> {
            override fun onResponse(call: Call<NoteResponse>, noteResponse: retrofit2.Response<NoteResponse>) {
                if (noteResponse.isSuccessful) {
                    Toast.makeText(context, noteResponse.body()?.message, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, noteResponse.body()?.message, Toast.LENGTH_LONG).show()
                }

            }

            override fun onFailure(call: Call<NoteResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                Log.e("NoteViewModel", "onFailure: ", t)
            }

        })
    }

    fun updateNote(context: Context, noteId: String, message: String, date: String) {
        val client = ApiConfig.getApiService().updateNote(message, date, note_id = noteId)

        client.enqueue(object: Callback<NoteResponse> {
            override fun onResponse(call: Call<NoteResponse>, noteResponse: retrofit2.Response<NoteResponse>) {
                if (noteResponse.isSuccessful) {
                    Toast.makeText(context, noteResponse.body()?.message, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, noteResponse.body()?.message, Toast.LENGTH_LONG).show()
                }

            }

            override fun onFailure(call: Call<NoteResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                Log.e("NoteViewModel", "onFailure: ", t)
            }

        })
    }

    fun deleteNote(context: Context, noteId: String) {
        val client = ApiConfig.getApiService().deleteNoteById(note_id = noteId)

        client.enqueue(object: Callback<NoteResponse> {
            override fun onResponse(call: Call<NoteResponse>, noteResponse: retrofit2.Response<NoteResponse>) {
                if (noteResponse.isSuccessful) {
                    Toast.makeText(context, noteResponse.body()?.message, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, noteResponse.body()?.message, Toast.LENGTH_LONG).show()
                }

            }

            override fun onFailure(call: Call<NoteResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                Log.e("NoteViewModel", "onFailure: ", t)
            }

        })
    }
}