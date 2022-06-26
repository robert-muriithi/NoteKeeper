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
    val note : LiveData<Resource<List<Note>>> = _notes

     suspend fun getNotes()  {
         _notes.value = Resource.Loading
         repository.getNotes { note->
             _notes.value = note
         }
    }

    private val _addNote = MutableLiveData<Resource<List<Note>>>()
    val addNote : LiveData<Resource<List<Note>>> = _addNote

    suspend fun addNote(note : Note){
        _addNote.value = Resource.Loading
        repository.addNote(note){ note ->
            _addNote.value = note
        }

    }

    private val _archiveNote = MutableLiveData<Resource<List<Note>>>()
    val archiveNote : LiveData<Resource<List<Note>>> = _archiveNote

    suspend fun archiveNote(note : Note){
        _archiveNote.value = Resource.Loading
        repository.archiveNote(note){ note ->
            _archiveNote.value = note
        }
    }

    private val _deleteNote = MutableLiveData<Resource<List<Note>>>()
    val deleteNote : LiveData<Resource<List<Note>>> = _deleteNote

    suspend fun deleteNote(note : Note){
        _deleteNote.value = Resource.Loading
        repository.deleteNote(note){ note ->
            _deleteNote.value = note
        }
    }

    private val _archivedNotes = MutableLiveData<Resource<List<Note>>>()
    val archivedNotes : LiveData<Resource<List<Note>>> = _archivedNotes

    suspend fun getArchivedNotes(){
        _archivedNotes.value = Resource.Loading
        repository.getArchivedNotes{ note ->
            _archivedNotes.value = note
        }
    }

}