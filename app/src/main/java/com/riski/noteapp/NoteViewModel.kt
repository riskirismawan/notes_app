package com.riski.noteapp

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.riski.noteapp.api.ApiConfig
import com.riski.noteapp.data.DataItem
import com.riski.noteapp.data.Response
import retrofit2.Call
import retrofit2.Callback

class NoteViewModel: ViewModel() {
    fun getNotes(context: Context): LiveData<List<DataItem>> {
        val notes = MutableLiveData<List<DataItem>>()
        val client = ApiConfig.getApiService().getNotes()

        client.enqueue(object: Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                if (response.isSuccessful) {
                    notes.value = response.body()?.data as List<DataItem>
                } else {
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_LONG).show()
                }

            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                Log.e("NoteViewModel", "onFailure: ", t)
            }

        })

        return notes
    }

    fun getNoteById(context: Context, noteId: String): LiveData<DataItem> {
        val note = MutableLiveData<DataItem>()
        val client = ApiConfig.getApiService().getNoteById(id = noteId)

        client.enqueue(object: Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                if (response.isSuccessful) {
                    val data = response.body()?.data as List<DataItem>
                    note.value = data[0]
                } else {
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_LONG).show()
                }

            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                Log.e("NoteViewModel", "onFailure: ", t)
            }

        })

        return note
    }

    fun insertNote(context: Context, message: String, date: String) {
        val client = ApiConfig.getApiService().insertNote(message, date)

        client.enqueue(object: Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_LONG).show()
                }

            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                Log.e("NoteViewModel", "onFailure: ", t)
            }

        })
    }

    fun updateNote(context: Context, noteId: String, message: String, date: String) {
        val client = ApiConfig.getApiService().updateNote(message, date, id = noteId)

        client.enqueue(object: Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_LONG).show()
                }

            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                Log.e("NoteViewModel", "onFailure: ", t)
            }

        })
    }

    fun deleteNote(context: Context, noteId: String) {
        val client = ApiConfig.getApiService().deleteNoteById(id = noteId)

        client.enqueue(object: Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_LONG).show()
                }

            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                Log.e("NoteViewModel", "onFailure: ", t)
            }

        })
    }
}