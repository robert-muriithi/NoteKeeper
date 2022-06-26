package dev.robert.notekeeper.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.robert.notekeeper.databinding.ActivityCalenderBinding


class CalenderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalenderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalenderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.calendar
            .setOnDateChangeListener { _, year, month, dayOfMonth ->

                val date = (dayOfMonth.toString() + "-"
                        + (month + 1) + "-" + year)

                // set this date in TextView for Display
                binding.dateView.text = date
            }
    }
}