package dev.robert.notekeeper.ui.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.notekeeper.data.repository.NoteRepositoryImpl
import dev.robert.notekeeper.model.Note
import dev.robert.notekeeper.utils.Resource
import javax.inject.Inject

@HiltViewModel
class NotesViewModel  @Inject constructor(
    private val repository : NoteRepositoryImpl
): ViewModel() {

    private val _notes = MutableLiveData<Resource<List<Note>>>()
    val note : LiveData<Resource<List<Note>>>
        get() = _notes

     suspend fun getNotes()  {
         _notes.value = Resource.Loading
         _notes.value = repository.getNotes()
    }

}