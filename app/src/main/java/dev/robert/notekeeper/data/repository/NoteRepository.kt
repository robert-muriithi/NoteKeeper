package dev.robert.notekeeper.data.repository

import dev.robert.notekeeper.model.Note
import dev.robert.notekeeper.utils.Resource

interface NoteRepository {
    suspend fun getNotes() : Resource<List<Note>>
}