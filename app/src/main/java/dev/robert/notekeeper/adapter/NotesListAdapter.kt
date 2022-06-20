package dev.robert.notekeeper.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.robert.notekeeper.databinding.NoteListItemBinding
import dev.robert.notekeeper.model.Note

class NotesListAdapter : ListAdapter<Note, NotesListAdapter.NoteViewHolder>(NoteDiffCallback) {

    inner class NoteViewHolder(private val binding: NoteListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Note?) {
            binding.noteTitle.text = item?.title
            binding.noteContent.text = item?.content
            binding.noteDate.text = item?.date.toString()
            binding.cardView.setOnLongClickListener {
                binding.cardView.isChecked = !binding.cardView.isChecked
                true
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            NoteListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object NoteDiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }

}