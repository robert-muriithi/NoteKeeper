package dev.robert.notekeeper.ui.fragments.add_notes

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import dev.robert.notekeeper.R
import dev.robert.notekeeper.databinding.ActivityAddNotesBinding
import dev.robert.notekeeper.model.Note
import dev.robert.notekeeper.ui.activities.MainActivity
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
@AndroidEntryPoint
class AddNotesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNotesBinding
    private val REQUEST_IMAGE_CAPTURE = 1
    private val RESULT_LOAD_IMAGE = 2
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var selectedImage: Uri? = null

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.addNotesToolbar)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.addNotesToolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.pickPhoto.setOnClickListener {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.select_photo_dialog)
            val cameraOption = dialog.findViewById(R.id.cameraTV) as View
            cameraOption.setOnClickListener {
                Toast.makeText(this, "Camera", Toast.LENGTH_SHORT).show()
                launchTakePictureIntent()
                dialog.dismiss()
            }
            val galleryOption = dialog.findViewById(R.id.galleryTV) as View
            galleryOption.setOnClickListener {
                Toast.makeText(this, "Gallery", Toast.LENGTH_SHORT).show()
                launchGalleryIntent()
                dialog.dismiss()
            }
            dialog.show()
        }

        binding.setReminder.setOnClickListener {
            pickDateTime()
        }

        val currentDate = SimpleDateFormat("E, MMM dd, yyyy").format(Date())
        val currentTime = SimpleDateFormat("hh:mm a").format(Date())
        binding.currentDate.text = currentDate
        binding.currentTime.text = currentTime

        binding.setReminder.setOnClickListener {
            pickDateTime()
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_delete -> {
                Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show()
                clearEntries()
            }
            R.id.action_save -> {
                Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show()
                validateEntries()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun clearEntries() {
        val dialog = MaterialAlertDialogBuilder(this)
        dialog.setTitle("Clear Entries")
        dialog.setMessage("Are you sure you want to delete the note before saving??")
        dialog.setPositiveButton("Yes") { _, _ ->
            binding.noteTitle.text.clear()
            binding.noteContent.text.clear()
            binding.noteImage.isVisible = false
            binding.notificationActive.isVisible = false
            binding.reminderDateTime.isVisible = false
        }
        dialog.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        dialog.show()
    }


    private fun validateEntries() {
        val userId = auth.currentUser?.uid
        val title = binding.noteTitle.text.toString().trim()
        val content = binding.noteContent.text.toString().trim()
        if (title.isEmpty()) {
            binding.noteTitle.error = "Title is required"
        }
        if (content.isEmpty()) {
            binding.noteContent.error = "Note is required"
        }else{
            binding.progressBar.isVisible = true
            val date = binding.currentDate.text.toString()
            saveNote(userId, title, content, date)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun pickDateTime() {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        DatePickerDialog(this, { _, year, month, day ->
            TimePickerDialog(this, { _, hour, minute ->
                val pickedDateTime = Calendar.getInstance()
                pickedDateTime.set(year, month, day, hour, minute)
                binding.notificationActive.isVisible = true
                binding.reminderDateTime.isVisible = true
                val sdf = SimpleDateFormat("E, dd/MM, yyyy HH:mm").format(pickedDateTime.time)
                binding.reminderDateTime.text = sdf.toString()
            }, startHour, startMinute, false).show()
        }, startYear, startMonth, startDay).show()
    }

    private fun launchGalleryIntent() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        try {
            startActivityForResult(intent, RESULT_LOAD_IMAGE)
        } catch (e : Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun launchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.noteImage.isVisible = true
            binding.noteImage.setImageBitmap(imageBitmap)
        }

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            val imageUri = data?.data
            binding.noteImage.isVisible = true
            binding.noteImage.setImageURI(imageUri)
        }
    }



    private fun saveNote(userId: String?, title: String, content: String, date: String) {
        val note = Note(userId, title, content, date)
        val collection = firestore.collection("notes")

        collection.add(note).addOnSuccessListener {
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
            uploadImage()
            binding.progressBar.isVisible = false
            startActivity(Intent(this, MainActivity::class.java))
        }
            .addOnFailureListener { p0 ->
                binding.progressBar.isVisible = false
                Toast.makeText(
                    this,
                    p0.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun uploadImage() {
        if (selectedImage != null) {
            val storage = FirebaseStorage.getInstance()
            val firebaseAuth = FirebaseAuth.getInstance()
            val reference = storage.reference.child("notes_image").child(firebaseAuth.uid!!)
            reference.putFile(selectedImage!!).addOnSuccessListener {
                Toast.makeText(this, "Image uploaded", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}