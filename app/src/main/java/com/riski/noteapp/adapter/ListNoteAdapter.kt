package com.riski.noteapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.riski.noteapp.R
import com.riski.noteapp.data.DataItem
import com.riski.noteapp.databinding.ItemViewBinding
import com.riski.noteapp.ui.FormActivity

class ListNoteAdapter: RecyclerView.Adapter<ListNoteAdapter.ListViewHolder>() {
    val notes = ArrayList<DataItem>()

    fun setItems(items: List<DataItem>) {
        notes.clear()
        notes.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    override fun getItemCount(): Int = notes.size

    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val binding = ItemViewBinding.bind(itemView)
        fun bind(note: DataItem) {
            binding.apply {
                tvMessage.text = note.message
                tvDate.text = note.date

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, FormActivity::class.java)
                    intent.putExtra(FormActivity.NOTE_RESPONSE, "edit")
                    intent.putExtra(FormActivity.NOTE_ID, note.id)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}