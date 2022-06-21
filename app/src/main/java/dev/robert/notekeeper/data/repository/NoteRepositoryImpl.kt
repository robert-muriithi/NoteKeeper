package dev.robert.notekeeper.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dev.robert.notekeeper.model.Note
import dev.robert.notekeeper.utils.Resource
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore,
    private val storage: FirebaseStorage
) : NoteRepository {
    override suspend fun getNotes(): Resource<List<Note>> {
        val data = arrayListOf<Note>(

        )
       return Resource.Success(data)
    }
}