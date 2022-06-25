package dev.robert.notekeeper.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import dev.robert.notekeeper.R
import dev.robert.notekeeper.databinding.ActivityArchivedNotesBinding

@AndroidEntryPoint
class ArchivedNotes : AppCompatActivity() {
    private lateinit var binding: ActivityArchivedNotesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArchivedNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}