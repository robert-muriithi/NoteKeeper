package dev.robert.notekeeper.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import dev.robert.notekeeper.R
import dev.robert.notekeeper.databinding.ActivityMainBinding
import dev.robert.notekeeper.ui.fragments.add_notes.AddNotesActivity
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navigationView
        navView.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.notesFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.fab.setOnClickListener {
            startActivity(Intent(this, AddNotesActivity::class.java))
        }

        binding.navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_notes_archived -> {
                    Toast.makeText(this, "Notes", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, ArchivedNotes::class.java))
                    true
                }
                R.id.action_logout -> {
                    val currentUser = auth
                    auth.removeAuthStateListener {
                        currentUser.signOut()
                        startActivity(Intent(this, SignInActivity::class.java))
                        finish()
                    }
                    true
                }
                R.id.nav_calender -> {
                    Toast.makeText(this, "Calender", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_share -> {
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.type = "text/plain"
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=dev.robert.notekeeper")
                    startActivity(Intent.createChooser(shareIntent, "Share using"))

                    true
                }
                else -> {
                    false
                }
            }
        }
        val header = binding.navigationView.getHeaderView(0)
        val imageView = header.findViewById<ImageView>(R.id.imageView)
        val userImage = auth.currentUser?.photoUrl
        val userName = auth.currentUser?.displayName
        val userEmail = auth.currentUser?.email

        Glide
            .with(this)
            .load(userImage)
            .apply(RequestOptions().override(200, 200))
            .into(imageView)

        val userNameText = header.findViewById<android.widget.TextView>(R.id.username)
        val userEmailText = header.findViewById<android.widget.TextView>(R.id.useremail)
        userNameText.text = userName
        userEmailText.text = userEmail

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onResume() {
        super.onResume()
        supportActionBar?.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show()
            R.id.action_sync -> Toast.makeText(this, "syc", Toast.LENGTH_SHORT).show()
            else -> super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}