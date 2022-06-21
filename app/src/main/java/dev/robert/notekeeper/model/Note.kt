package dev.robert.notekeeper.model

import com.google.firebase.firestore.ServerTimestamp

data class Note(
    val id: String? = "",
    val title: String? = "",
    val content: String? = "",
    val date : String? = "",
)