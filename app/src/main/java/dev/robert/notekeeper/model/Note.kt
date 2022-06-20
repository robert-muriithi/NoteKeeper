package dev.robert.notekeeper.model

data class Note(
    val id: String? = "",
    val title: String? = "",
    val content: String? = "",
    val date : String? = "",
)