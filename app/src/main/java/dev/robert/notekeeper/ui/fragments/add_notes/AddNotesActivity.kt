package dev.robert.notekeeper.ui.fragments.add_notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.robert.notekeeper.R
import dev.robert.notekeeper.databinding.ActivityAddNotesBinding

class AddNotesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNotesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.addNotesToolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.addNotesToolbar.setNavigationOnClickListener {
            onBackPressed()
        }



    }
}