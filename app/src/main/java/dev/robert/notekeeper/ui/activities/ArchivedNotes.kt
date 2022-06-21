package dev.robert.notekeeper.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.robert.notekeeper.R

class ArchivedNotes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archived_notes)
    }
}