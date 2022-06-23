package dev.robert.notekeeper.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dev.robert.notekeeper.model.Note
import dev.robert.notekeeper.utils.FirestoreTable
import dev.robert.notekeeper.utils.Resource
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore,
    private val storage: FirebaseStorage
) : NoteRepository {
    override suspend fun getNotes(result: (Resource<List<Note>>) -> Unit) {
        database.collection(FirestoreTable.TABLE)
            .get()
            .addOnSuccessListener { snapshot->
                val notes = arrayListOf<Note>()
                for (document in snapshot ){
                    val note = document.toObject(Note::class.java)
                    notes.add(note)
                }
                result.invoke(
                    Resource.Success(notes)
                )
            }
            .addOnFailureListener {
                result.invoke(
                    Resource.Error(it.localizedMessage)
                )
            }
    }

    override suspend fun addNote(note: Note, result: (Resource<String>) -> Unit) {
        database.collection(FirestoreTable.TABLE)
            .add(note)
            .addOnSuccessListener {
                result.invoke(
                    Resource.Success(it.id)
                )
            }
            .addOnFailureListener {
                result.invoke(
                    Resource.Error(it.localizedMessage)
                )
            }
    }
}