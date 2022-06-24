package dev.robert.notekeeper.data.repository

import android.util.Log
import android.widget.Toast
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
    private val TAG = "NoteRepositoryImpl"
    override suspend fun getNotes(result: (Resource<List<Note>>) -> Unit) {
        database.collection("notes")
            .get()
            .addOnSuccessListener { snapshot->
                val notes = arrayListOf<Note>()
                for (document in snapshot ){
                    val note = document.toObject(Note::class.java)
                    notes.add(note)
                }
                Log.d(TAG, "Notes: $notes")
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

    override suspend fun addNote(note: Note, result: (Resource<List<Note>>) -> Unit) {
        database.collection("note")
            .add(note)
            .addOnSuccessListener {
                Log.d(TAG, "Note added: $note")
                result.invoke(
                    Resource.Success(arrayListOf(note))
                )
            }
            .addOnFailureListener {
                Toast.makeText(
                    database.app.applicationContext,
                    it.localizedMessage,
                    Toast.LENGTH_LONG
                ).show()
                result.invoke(
                    Resource.Error(it.localizedMessage)
                )
            }
    }

    /*override suspend fun addNote(note: Note, result: (Resource<String>) -> Unit) {
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
    }*/
}