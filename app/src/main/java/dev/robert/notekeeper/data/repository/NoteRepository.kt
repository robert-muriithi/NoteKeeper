package dev.robert.notekeeper.data.repository

import dev.robert.notekeeper.model.Note
import dev.robert.notekeeper.utils.Resource

interface NoteRepository {
    suspend fun getNotes(result: (Resource<List<Note>>) -> Unit)
    suspend fun addNote(note: Note, result: (Resource<List<Note>>) -> Unit)
    suspend fun archiveNote(note: Note, result: (Resource<List<Note>>) -> Unit)
    suspend fun deleteNote(note: Note, result: (Resource<List<Note>>) -> Unit)
    suspend fun getArchivedNotes(result: (Resource<List<Note>>) -> Unit)
    //suspend fun unArchiveNotes(result: (Resource<List<Note>>) -> Unit)
}