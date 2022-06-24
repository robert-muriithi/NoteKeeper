package dev.robert.notekeeper.ui.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.notekeeper.data.repository.NoteRepository
import dev.robert.notekeeper.data.repository.NoteRepositoryImpl
import dev.robert.notekeeper.model.Note
import dev.robert.notekeeper.utils.Resource
import javax.inject.Inject

@HiltViewModel
class NotesViewModel  @Inject constructor(
    private val repository : NoteRepository
): ViewModel() {

    private val _notes = MutableLiveData<Resource<List<Note>>>()
    val note : LiveData<Resource<List<Note>>>
        get() = _notes

     suspend fun getNotes()  {
         _notes.value = Resource.Loading
         repository.getNotes { note->
             _notes.value = note
         }
    }

    private val _addNote = MutableLiveData<Resource<List<Note>>>()
    val addNote : LiveData<Resource<List<Note>>>
        get() = _addNote

    suspend fun addNote(note : Note){
        _addNote.value = Resource.Loading
        repository.addNote(note){ note ->
            _addNote.value = note
        }

    }


}