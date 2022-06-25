package dev.robert.notekeeper.ui.fragments.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import dagger.hilt.android.AndroidEntryPoint
import dev.robert.notekeeper.adapter.NotesListAdapter
import dev.robert.notekeeper.databinding.FragmentNotesBinding
import dev.robert.notekeeper.model.Note
import dev.robert.notekeeper.utils.Resource


@AndroidEntryPoint
class NotesFragment : Fragment() {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: FirebaseFirestore
    private val adapter by lazy { NotesListAdapter() }
    private val list: ArrayList<Note> = ArrayList()
    private val viewModel : NotesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                RecyclerView.VERTICAL
            )
        )
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
        binding.recyclerView.setHasFixedSize(true)

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.getNotes()
        }
        viewModel.note.observe(viewLifecycleOwner){ state ->
            when (state){
                is Resource.Loading -> {
                    binding.progressCircular.isVisible = true
                }
                is Resource.Success -> {
                    binding.progressCircular.isVisible = false
                    adapter.submitList(state.data)
                }
                is Resource.Error -> {
                    binding.progressCircular.isVisible = false
                    Toast.makeText(requireContext(), "An error occurred", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun cacheNotes() {
        val instance = FirebaseFirestore.getInstance()
        instance.firestoreSettings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()

        db = instance
        instance.collection("notes").addSnapshotListener { value, error ->
            if (error != null) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                return@addSnapshotListener

            }
            for (change in value!!.documentChanges) {
                when (change.type) {
                    DocumentChange.Type.MODIFIED -> {
                        val note = change.document.toObject(Note::class.java)
                        list.remove(note)
                        list.add(note)
                        adapter.notifyDataSetChanged()
                    }
                    else -> {
                        //nothing
                    }
                }
            }
        }
    }

    private val itemTouchHelper = ItemTouchHelper(object :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            list.removeAt(position)
            adapter.notifyDataSetChanged()
        }
    })
}